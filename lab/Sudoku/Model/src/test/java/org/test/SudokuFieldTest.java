package org.test;

import org.example.SudokuField;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {

    @Test
    public void testGetSetValue() {
        SudokuField field = new SudokuField();
        field.setValue(5);
        assertEquals(5, field.getValue());
    }

    @Test
    public void testHashCode() {
        SudokuField field1 = new SudokuField(5);
        SudokuField field2 = new SudokuField(5);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testEquals() {
        SudokuField field1 = new SudokuField(5);
        SudokuField field2 = new SudokuField(5);
        SudokuField field3 = new SudokuField(6);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testToString() {
        SudokuField field = new SudokuField(5);
        String expectedString = "SudokuField{value=5}";
        assertEquals(expectedString, field.toString());
    }

    @Test
    public void testSudokuFieldClone() throws CloneNotSupportedException {
        SudokuField field1 = new SudokuField(5);
        SudokuField field2 = (SudokuField) field1.clone();
        assertNotSame(field1, field2);
        assertEquals(field1, field2);

        // Modify the state of the original field
        field1.setValue(6);

        // Check that the state of the cloned field has not changed
        assertNotEquals(field1.getValue(), field2.getValue());
    }

    @Test
    public void testSudokuFieldCompareTo() {
        SudokuField field1 = new SudokuField();
        field1.setValue(5);
        SudokuField field2 = new SudokuField();
        field2.setValue(3);
        assertTrue(field1.compareTo(field2) > 0);
    }

    @Test
    public void testSudokuFieldCompareTo1() {
        SudokuField field1 = new SudokuField();
        field1.setValue(3);
        SudokuField field2 = new SudokuField();
        field2.setValue(5);
        assertTrue(field1.compareTo(field2) < 0);
    }

    @Test
    public void testSudokuFieldCompareTo2() {
        SudokuField field1 = new SudokuField();
        field1.setValue(5);
        SudokuField field2 = new SudokuField();
        field2.setValue(5);
        assertEquals(0, field1.compareTo(field2));
    }
}
