package de.incentergy.geometry.impl;

import de.incentergy.geometry.PolygonSplitter;
import de.incentergy.geometry.impl.EdgePair.EdgePairSubpolygons;
import de.incentergy.geometry.utils.GeometryFactoryUtils;
import de.incentergy.geometry.utils.GeometryUtils;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link PolygonSplitter} implementation based on the algorithm by Sumit Khetarpal
 *
 * @see http://www.khetarpal.org/polygon-splitting/
 */
public class GreedyPolygonSplitter implements PolygonSplitter {

    /**
     * Splits the polygon into parts of equal area
     *
     * @param originalPolygon - polygon to split
     * @param numPts - number of equal area parts that must be produced
     * @return
     */
    @Override
    public List<Polygon> split(Polygon originalPolygon, int numPts) {
        if (!validate(originalPolygon, numPts)) {
            return null;
        }
        // TODO: add validation - at least 4 sides, no holes

        double singlePartArea = originalPolygon.getArea() / numPts;

        List<Polygon> polygonParts = new ArrayList<>(numPts);
        Polygon remainingPoly = originalPolygon;
        for (int i = 0; i < numPts - 1; i++) {
            remainingPoly = split(remainingPoly, polygonParts, singlePartArea);
        }
        polygonParts.add(remainingPoly);

        // sanity check: total area is the same
        double totalAreaOfTheParts = polygonParts.stream().mapToDouble(Polygon::getArea).sum();
        if (!GeometryUtils.equalWithinDelta(totalAreaOfTheParts, originalPolygon.getArea())) {
            throw new IllegalStateException("Area of the parts does not match original area");
        }

        // sanity check: geometry is the same
        Polygon unionOfTheParts = (Polygon) GeometryFactoryUtils.createGeometryCollection(polygonParts).union();
        if (unionOfTheParts.equalsNorm(originalPolygon)) {
            throw new IllegalStateException("The sum of the parts is not equal to the original polygon");
        }

        return Collections.unmodifiableList(polygonParts);
    }

    private Polygon split(Polygon polygon, List<Polygon> resultList, double singlePartArea) {
        List<LineSegment> segments = GeometryUtils.getLineSegments(polygon.getExteriorRing());

        List<Cut> possibleCuts = new ArrayList<>();

        // for each unique edge pair
        for (int i = 0; i < segments.size() - 2; i++) {

            // generate unique edge pairs (e.g. 2 pairs for any rectangle)
            for (int j = i + 2; j < segments.size(); j++) {
                int segmentsCovered = j - i + 1;            // number of segments covered by a LineRing starting with edgeA and ending with edgeB (including)
                if (segments.size() == segmentsCovered) {
                    break;
                }

                LineSegment edgeA = segments.get(i);
                LineSegment edgeB = segments.get(j);
                EdgePair edgePair = new EdgePair(edgeA, edgeB);
                EdgePairSubpolygons subpolygons = edgePair.getSubpolygons();
                List<Cut> cutForCurrentEdgePair = subpolygons.getCuts(polygon, singlePartArea);
                possibleCuts.addAll(cutForCurrentEdgePair);
            }
        }

        // greedy algorithm: take minimum cut length
        Cut shortestCut = possibleCuts.stream().min(Comparator.comparing(Cut::getLength)).get();
        resultList.add(shortestCut.getCutAway());

        return (Polygon) polygon.difference(shortestCut.getCutAway());
    }
}
