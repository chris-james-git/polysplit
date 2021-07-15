/*
 * Class : FastGeometryFactoryUtils
 * Author: Chris James
 * Email : chrisdjames1@gmail.com
 * Date  : 15 July 2021
 * Description : Provides geometry utilities for use with the FastPolygonSplitter.
 */
package de.incentergy.geometry.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.shape.random.RandomPointsBuilder;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

public class FastGeometryFactoryUtils {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private FastGeometryFactoryUtils() {
    }

    public static MultiPoint generateRandomPoints(Geometry mask, int numPts) {
        RandomPointsBuilder pointsBuilder = new RandomPointsBuilder();
        pointsBuilder.setExtent(mask);
        pointsBuilder.setNumPoints(numPts);
        return (MultiPoint) pointsBuilder.getGeometry();
    }

    public static double[][] multipointToDoubleArr(MultiPoint mp) {
        int size = mp.getNumPoints();
        double[][] pts = new double[size][2];
        Coordinate[] coords = mp.getCoordinates();
        for (int i = 0; i < size; i++) {
            pts[i][0] = coords[i].x;
            pts[i][1] = coords[i].y;
        }
        return pts;
    }

    public static MultiPoint multipointFromDoubleArr(double[][] pts) {
        Coordinate[] coords = new Coordinate[pts.length];

        for (int i = 0; i < pts.length; i++) {
            coords[i] = new Coordinate(pts[i][0], pts[i][1]);
        }
        return GEOMETRY_FACTORY.createMultiPointFromCoords(coords);
    }

    public static Geometry multipointToVoronoi(MultiPoint mp) {
        VoronoiDiagramBuilder voronoiBuilder = new VoronoiDiagramBuilder();
        voronoiBuilder.setSites(mp);
        return voronoiBuilder.getDiagram(GEOMETRY_FACTORY);
    }
}
