/*
 * Class : RandomPolygonSplitterTest
 * Author: Chris James
 * Email : chrisdjames1@gmail.com
 * Date  : 15 July 2021
 */
package de.incentergy.geometry.impl;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomPolygonSplitterTest {

    private final RandomPolygonSplitter polygonSplitter = new RandomPolygonSplitter();

    @Test
    public void splitTrapeziumRoughlyInHalf() throws ParseException {
        Polygon polygon = (Polygon) new WKTReader().read(
                "POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");

        List<Polygon> parts = polygonSplitter.split(polygon, 2);

        assertEquals(2, parts.size());
        MultiPolygon multiPolygon = new MultiPolygon(parts.toArray(new Polygon[0]), new GeometryFactory());
        System.out.println(multiPolygon);
    }

    @Test
    public void splitTrapeziumRoughlyInto5() throws ParseException {
        Polygon polygon = (Polygon) new WKTReader().read(
                "POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");

        List<Polygon> parts = polygonSplitter.split(polygon, 5);

        assertEquals(5, parts.size());
        MultiPolygon multiPolygon = new MultiPolygon(parts.toArray(new Polygon[0]), new GeometryFactory());
        System.out.println(multiPolygon);
    }
}
