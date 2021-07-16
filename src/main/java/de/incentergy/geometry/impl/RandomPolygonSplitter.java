/*
 * Class : RandomPolygonSplitter
 * Author: Chris James
 * Email : chrisdjames1@gmail.com
 * Date  : 15 July 2021
 * Description : Splits a polygon into n roughly equal areas.
 * Based upon a comment posted by Darafei "Komяpa" Praliaskouski here:
 * https://lists.osgeo.org/pipermail/postgis-users/2018-June/042795.html
 *
 * Utilizes the KMeans algorithm by Jason Altschuler from:
 * https://github.com/JasonAltschuler/KMeansPlusPlus
 *
 */
package de.incentergy.geometry.impl;

import de.incentergy.geometry.PolygonSplitter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.generateRandomPoints;
import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.multipointFromDoubleArr;
import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.multipointToDoubleArr;
import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.multipointToVoronoi;

public class RandomPolygonSplitter implements PolygonSplitter {

    private static final int RANDOM_POINTS_COUNT = 1000;

    /**
     * Uses point cloud, K-means++ clustering and Voronoi split mask to get an approximate
     * division of a polygon into n roughly equal parts.
     * @param polygon - polygon to split
     * @param parts - number of equal area parts that must be produced
     * @return
     */
    @Override
    public List<Polygon> split(Polygon polygon, int parts) {

        if (!validate(polygon, parts)) {
            return null;
        }

        // Convert a polygon to a set of points proportional to the area using
        //  JTS RandomPointsBuilder
        MultiPoint pointsCloud = generateRandomPoints(polygon, RANDOM_POINTS_COUNT);

        // Take KMeans of the point cloud
        double[][] pointsCloudAsArr = multipointToDoubleArr(pointsCloud);
        KMeans kMeans = new KMeans.Builder(parts, pointsCloudAsArr).build();

        // Get the centroids of the clusters
        MultiPoint centroids = multipointFromDoubleArr(kMeans.getCentroids());

        // Generate a Voronoi diagram from the centroids to get a split mask
        Geometry splitMask = multipointToVoronoi(centroids);

        // Calculate the intersection of the original polygon with each cell of the
        // Voronoi split mask to acquire a division of the original into n parts
        List<Polygon> splitPolys = new ArrayList<>();
        for (int i = 0; i < splitMask.getNumGeometries(); i++) {
            Polygon splitGon = (Polygon) polygon.intersection(splitMask.getGeometryN(i));
            splitPolys.add(splitGon);
        }
        return splitPolys;
    }
}
