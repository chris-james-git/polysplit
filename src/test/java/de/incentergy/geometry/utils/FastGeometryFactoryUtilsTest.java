/*
 * Class : FastGeometryFactoryUtilsTest
 * Author: Chris James
 * Email : chrisdjames1@gmail.com
 * Date  : 15 July 2021
 */
package de.incentergy.geometry.utils;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import static de.incentergy.geometry.utils.FastGeometryFactoryUtils.generateRandomPoints;
import static de.incentergy.geometry.utils.FastGeometryFactoryUtils.multipointToVoronoi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FastGeometryFactoryUtilsTest {

    @Test
    public void testGenerateRandomPoints() throws ParseException {
        Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");

        MultiPoint points = generateRandomPoints(polygon, 1000);

        assertEquals(1000, points.getNumPoints());

        assertTrue(points.within(polygon), "Points not within polygon");
    }

    @Test
    public void testMultipointToVoronoi() throws ParseException {
        MultiPoint multiPoint = (MultiPoint) new WKTReader().read(
                "MULTIPOINT ((81.68182477410573 13.11869464882645), " +
                        "(15.162587781111588 14.400220389088739), " +
                        "(68.51722986274106 37.128228208665675), " +
                        "(46.28867802117712 14.00229610409934), " +
                        "(28.23372284459519 37.454571090046564))");
        Geometry voronoiDiagram = multipointToVoronoi(multiPoint);
        System.out.println(voronoiDiagram.toString());
    }
}
