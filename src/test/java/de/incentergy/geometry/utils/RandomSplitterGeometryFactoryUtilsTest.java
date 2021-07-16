/*
 * Class : RandomSplitterGeometryFactoryUtilsTest
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

import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.generateRandomPoints;
import static de.incentergy.geometry.utils.RandomSplitterGeometryFactoryUtils.multipointToVoronoi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomSplitterGeometryFactoryUtilsTest {

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

        assertEquals(voronoiDiagram.toString(),
                "GEOMETRYCOLLECTION (POLYGON ((-55.66850547808112 -57.712398610366265, " +
                        "-55.66850547808112 69.79200445166691, 30.809501201028134 20.761532069874274, " +
                        "29.806269426160135 -57.712398610366265, -55.66850547808112 -57.712398610366265)), " +
                        "POLYGON ((62.20590034734066 -57.712398610366265, 29.806269426160135 -57.712398610366265, " +
                        "30.809501201028134 20.761532069874274, 48.35096708637129 34.26599509025021, " +
                        "64.12367994474745 19.105327668534137, 62.20590034734066 -57.712398610366265)), " +
                        "POLYGON ((-55.66850547808112 69.79200445166691, -55.66850547808112 108.28566434923928, " +
                        "48.95061180285434 108.28566434923928, 48.35096708637129 34.26599509025021, " +
                        "30.809501201028134 20.761532069874274, -55.66850547808112 69.79200445166691)), " +
                        "POLYGON ((48.95061180285434 108.28566434923928, 152.51291803329843 108.28566434923928, " +
                        "152.51291803329843 67.56976414164863, 64.12367994474745 19.105327668534137, " +
                        "48.35096708637129 34.26599509025021, 48.95061180285434 108.28566434923928)), " +
                        "POLYGON ((152.51291803329843 67.56976414164863, 152.51291803329843 -57.712398610366265, " +
                        "62.20590034734066 -57.712398610366265, 64.12367994474745 19.105327668534137, " +
                        "152.51291803329843 67.56976414164863)))");
    }
}
