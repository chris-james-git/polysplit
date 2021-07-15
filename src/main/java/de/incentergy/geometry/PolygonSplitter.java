package de.incentergy.geometry;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import java.util.List;


public interface PolygonSplitter {

    /**
     * Splits the polygon into parts of equal area
     *
     * @param polygon - polygon to split
     * @param parts - number of equal area parts that must be produced
     * @return
     */
    List<Polygon> split(Polygon polygon, int parts);

    /**
     * Splits the WKT-format polygon into WKT-format multipolygon with
     * parts of equal area
     *
     * @param wktPolygon
     * @param parts - number of equal area parts that must be produced
     * @return
     * @throws ParseException
     */
    String split(String wktPolygon, int parts) throws ParseException;

    default void validate(Polygon polygon, int parts) {
        if (!polygon.isValid()) {
            throw new IllegalArgumentException("Polygon is not valid!");
        }
        if (parts < 2) {
            throw new IllegalArgumentException("Number of parts should be greater than 1!");
        }
    }
}
