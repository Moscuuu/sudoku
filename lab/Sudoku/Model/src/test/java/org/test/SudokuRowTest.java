package org.test;
import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuField;
import org.example.SudokuRow;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuRowTest {

    @Test
    public void testGetFields() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuRow row = board.getRow(0);
        assertNotNull(row.getFields());
    }

    @Test
    public void testSudokuRowClone() throws CloneNotSupportedException {
        List<SudokuField> fields = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fields.add(new SudokuField(i+1));
        }
        SudokuRow row1 = new SudokuRow(fields);
        SudokuRow row2 = (SudokuRow) row1.clone();
        assertNotSame(row1, row2);

        // Modify the state of the original row
        row1.getFields().get(0).setValue(5);

        // Check that the state of the cloned row has not changed
        assertNotEquals(row1.getFields().get(0).getValue(), row2.getFields().get(0).getValue());
    }

}
