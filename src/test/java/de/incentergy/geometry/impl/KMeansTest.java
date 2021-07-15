package de.incentergy.geometry.impl;

import de.incentergy.geometry.utils.FastGeometryFactoryUtils;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import static de.incentergy.geometry.utils.FastGeometryFactoryUtils.generateRandomPoints;
import static de.incentergy.geometry.utils.FastGeometryFactoryUtils.multipointFromDoubleArr;
import static de.incentergy.geometry.utils.FastGeometryFactoryUtils.multipointToDoubleArr;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KMeansTest {

    @Test
    public void testKMeans() throws ParseException {
        Polygon polygon = (Polygon) new WKTReader().read(
                "POLYGON ((0 0, 100 0, 90 50, 10 50, 0 0))");
        MultiPoint mp = generateRandomPoints(polygon, 1000);
        double[][] pts = multipointToDoubleArr(mp);
        KMeans kMeans = new KMeans.Builder(5, pts).build();
        assertEquals(5, kMeans.getCentroids().length);
        MultiPoint centroids = multipointFromDoubleArr(kMeans.getCentroids());
        System.out.println(centroids.toString());
    }
}
