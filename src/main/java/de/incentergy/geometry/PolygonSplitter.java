package de.incentergy.geometry;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;


public interface PolygonSplitter {

    List<Polygon> split(Polygon polygon, int parts);

    /**
     * Splits the WKT-format polygon into WKT-format multipolygon with
     * parts of equal area.
     *
     * @param originalWktPolygon - polygon to split.
     * @param numPts - number of equal area parts that must be produced.
     * @return WKT string of polygon split into equal parts.
     * @throws ParseException
     */
    default String split(String originalWktPolygon, int numPts) throws ParseException {
        Polygon polygon = (Polygon) new WKTReader().read(originalWktPolygon);
        List<Polygon> parts = split(polygon, numPts);
        if (parts == null) {
            System.out.println("An error occurred.");
            return "Invalid";
        }
        MultiPolygon multiPolygon = new MultiPolygon(parts.toArray(new Polygon[0]), new GeometryFactory());
        return multiPolygon.toString();
    }

    default boolean validate(Polygon polygon, int parts) {
        if (!polygon.isValid()) {
            System.out.println("Polygon is not valid!");
            return false;
        }
        if (parts < 2) {
            throw new IllegalArgumentException("Number of parts should be greater than 1!");
        }
        return true;
    }
}
