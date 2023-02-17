package org.heigit.ors.api.responses.matrix.json;

import com.vividsolutions.jts.geom.Coordinate;
import org.heigit.ors.matrix.ResolvedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class JSON2DDestinationsTest {
    private ResolvedLocation resolvedLocation;

    @BeforeEach
    void setUp() {
        Coordinate coordinate = new Coordinate(8.681495, 49.41461);
        resolvedLocation = new ResolvedLocation(coordinate, "foo", 0.0);
    }

    @Test
    void getLocation() {
        JSON2DDestinations json2DDestinationsWithLocation = new JSON2DDestinations(resolvedLocation, true);
        JSON2DDestinations json2DDestinationsWoLocation = new JSON2DDestinations(resolvedLocation, false);
        assertArrayEquals(new Double[]{8.681495, 49.41461}, json2DDestinationsWithLocation.getLocation());
        assertArrayEquals(new Double[]{8.681495, 49.41461}, json2DDestinationsWoLocation.getLocation());
    }
}