package org.test;

import org.example.SudokuField;
import org.junit.jupiter.api.Test;
import org.example.SudokuColumn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuColumnTest {

    @Test
    public void testSudokuColumnClone() throws CloneNotSupportedException {
        List<SudokuField> fields = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fields.add(new SudokuField(i+1));
        }
        SudokuColumn column1 = new SudokuColumn(fields);
        SudokuColumn column2 = (SudokuColumn) column1.clone();
        assertNotSame(column1, column2);

        // Modify the state of the original column
        column1.getFields().get(0).setValue(5);

        // Check that the state of the cloned column has not changed
        assertNotEquals(column1.getFields().get(0).getValue(), column2.getFields().get(0).getValue());
    }
}
