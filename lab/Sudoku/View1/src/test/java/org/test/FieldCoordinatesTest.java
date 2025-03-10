package org.test;

import org.exampleview.FieldCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FieldCoordinatesTest {

    @Test
    void testGetAxisX() {
        FieldCoordinates coordinates = new FieldCoordinates(5, 7);
        assertEquals(5, coordinates.getAxisX());
    }

    @Test
    void testGetAxisY() {
        FieldCoordinates coordinates = new FieldCoordinates(5, 7);
        assertEquals(7, coordinates.getAxisY());
    }

    @Test
    void testEquals() {
        FieldCoordinates coordinates1 = new FieldCoordinates(5, 7);
        FieldCoordinates coordinates2 = new FieldCoordinates(5, 7);
        FieldCoordinates coordinates3 = new FieldCoordinates(7, 5);

        assertEquals(coordinates1, coordinates2);
        assertNotEquals(coordinates1, coordinates3);
    }
    @Test
    void testEquals1() {
        FieldCoordinates coordinates1 = new FieldCoordinates(5, 7);

        // Test for the condition 'this == o'
        assertEquals(coordinates1, coordinates1); // This should return true

        // Test for the condition 'o == null'
        assertNotEquals(coordinates1, null); // This should return false

        // Test for the condition 'getClass() != o.getClass()'
        assertNotEquals(coordinates1, new Object()); // This should return false
    }

    @Test
    void testHashCode() {
        FieldCoordinates coordinates1 = new FieldCoordinates(5, 7);
        FieldCoordinates coordinates2 = new FieldCoordinates(5, 7);
        FieldCoordinates coordinates3 = new FieldCoordinates(7, 5);

        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());
        assertNotEquals(coordinates1.hashCode(), coordinates3.hashCode());
    }

    @Test
    void testToString() {
        FieldCoordinates coordinates = new FieldCoordinates(5, 7);
        assertEquals("FieldCoordinates{axisX=5, axisY=7}", coordinates.toString());
    }
}
