package org.test;

import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuBox;
import org.example.SudokuField;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoxTest {

    @Test
    public void testGetFields() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBox box = board.getBox(0, 0);
        assertNotNull(box.getFields());
    }

    @Test
    public void testVerify() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBox box = board.getBox(0, 0);
        assertTrue(box.verify());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        List<SudokuField> fields = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fields.add(new SudokuField(i+1));
        }
        SudokuBox box1 = new SudokuBox(fields);
        SudokuBox box2 = (SudokuBox) box1.clone();
        assertNotSame(box1, box2);

        // Modify the state of the original box
        box1.getFields().get(0).setValue(5);

        // Check that the state of the cloned box has not changed
        assertNotEquals(box1.getFields().get(0).getValue(), box2.getFields().get(0).getValue());
    }

}
