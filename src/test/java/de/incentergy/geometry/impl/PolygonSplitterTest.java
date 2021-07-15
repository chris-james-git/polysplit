package de.incentergy.geometry.impl;

import de.incentergy.geometry.PolygonSplitter;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolygonSplitterTest {

    private final PolygonSplitter polygonSplitter = new GreedyPolygonSplitter();

    @Test
    public void splitTrapeziumInHalf() throws Exception {
        Polygon polygon = (Polygon) new WKTReader().read("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");

        List<Polygon> parts = polygonSplitter.split(polygon, 2);

        assertEquals(2, parts.size());
        assertPolygonEquals("POLYGON ((50 0, 100 0, 90 50, 50 50, 50 0))", parts.get(0));
        assertPolygonEquals("POLYGON ((50 0, 0 0, 10 50, 50 50, 50 0))", parts.get(1));
    }

    @Test
    public void splitLShapedPolygonIn4Parts() throws Exception {
        WKTReader wktReader = new WKTReader();
        Polygon polygon = (Polygon) wktReader.read("POLYGON ((0 0, 0 30, 10 30, 10 10, 20 10, 20 0, 0 0))");

        List<Polygon> parts = polygonSplitter.split(polygon, 4);

        assertEquals(4, parts.size());
        assertPolygonEquals("POLYGON ((0 20, 0 30, 10 30, 10 20, 0 20))", parts.get(0));
        assertPolygonEquals("POLYGON ((0 10, 0 20, 10 20, 10 10, 0 10))", parts.get(1));
        assertPolygonEquals("POLYGON ((10 10, 20 10, 20 0, 10 0, 10 10))", parts.get(2));
        assertPolygonEquals("POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))", parts.get(3));
    }

    @Test
    public void testSplittingPrecision() throws Exception {
        WKTReader wktReader = new WKTReader();
        Polygon polygon = (Polygon) wktReader.read("POLYGON ((0 0, 50 -10, 100 0, 90 50, 50 60, 10 50, 0 0))");

        List<Polygon> parts = polygonSplitter.split(polygon, 3);
        assertEquals(3, parts.size());

        double expectedPartArea = polygon.getArea() / 3;
        double expectedDelta = expectedPartArea / 100 * 5;      // allow 5% delta
        assertEquals(expectedPartArea, parts.get(0).getArea(), expectedDelta);
        assertEquals(expectedPartArea, parts.get(1).getArea(), expectedDelta);
        assertEquals(expectedPartArea, parts.get(2).getArea(), expectedDelta);
    }

    @Test
    public void testSplitWkt4gonInto5EqualPartsTrapezoid1() throws ParseException {

        // POLYGON ((50 0, 100 0, 90 50, 50 50, 50 0))
        String wktMultipolygon = polygonSplitter.split("POLYGON ((50 0, 100 0, 90 50, 50 50, 50 0))", 5);

        // MULTIPOLYGON (((86 0, 100 0, 90 50, 86 50, 86 0)), ((50 37.5, 50 50, 86 50, 86 37.5, 50 37.5)), ((50 25, 50 37.5, 86 37.5, 86 25, 50 25)), ((68 0, 50 0, 50 25, 68 25, 68 0)), ((86 0, 68 0, 68 25, 86 25, 86 0)))
        assertEquals("MULTIPOLYGON (" +
                "((86 0, 100 0, 90 50, 86 50, 86 0)), " +
                "((50 37.5, 50 50, 86 50, 86 37.5, 50 37.5)), " +
                "((50 25, 50 37.5, 86 37.5, 86 25, 50 25)), " +
                "((68 0, 50 0, 50 25, 68 25, 68 0)), " +
                "((86 0, 68 0, 68 25, 86 25, 86 0)))", wktMultipolygon);
    }

    @Test
    public void testSplitWkt4gonInto5EqualPartsTrapezium() throws ParseException {

        // POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))
        String wktMultipolygon = polygonSplitter.split("POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))", 5);

        // MULTIPOLYGON (((77 0, 100 0, 90 50, 77 50, 77 0)), ((23 0, 0 0, 10 50, 23 50, 23 0)), ((41 0, 23 0, 23 50, 41 50, 41 0)), ((41 25, 41 50, 77 50, 77 25, 41 25)), ((77 0, 41 0, 41 25, 77 25, 77 0)))
        assertEquals("MULTIPOLYGON (" +
                "((77 0, 100 0, 90 50, 77 50, 77 0)), " +
                "((23 0, 0 0, 10 50, 23 50, 23 0)), " +
                "((41 0, 23 0, 23 50, 41 50, 41 0)), " +
                "((41 25, 41 50, 77 50, 77 25, 41 25)), " +
                "((77 0, 41 0, 41 25, 77 25, 77 0)))", wktMultipolygon);
    }

    // TODO: CONTINUE ADDING A FEW MORE TESTS, THEN CREATE THE USER INPUT FLOW - ask the user for the four coordinates
    //  of the input polygon. Also, what assumptions have I made in implementing this algorithm?

    // TODO: Time a huge number of runs to see how slow it is. Compare with my "fast" solution
    private static void assertPolygonEquals(String expectedPolygonWkt, Polygon actual) {
        assertEquals(expectedPolygonWkt, actual.toString());
    }
}
