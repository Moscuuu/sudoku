package org.test;

import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuField;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardTest {

    @Test
    public void testGetSet() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 5);
        assertEquals(5, board.get(0, 0));
    }

    @Test
    public void testCheckBoard() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
        assertTrue(board.checkBoard());
    }

    @Test
    public void testEqualsAndHashCode() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());

        // Test equals
        assertThat(board1).isEqualTo(board2);

        // Test hashCode
        assertThat(board1.hashCode()).isEqualTo(board2.hashCode());

        // Change a value in board2 and test again
        board2.set(0, 0, 5);
        assertThat(board1).isNotEqualTo(board2);
        assertThat(board1.hashCode()).isNotEqualTo(board2.hashCode());
    }

    @Test
    public void testCountZeroFields() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        // Initially, all fields should be zero
        assertEquals(81, board.countZeroFields());

        // Set a field to a non-zero value
        board.set(0, 0, 5);
        assertEquals(80, board.countZeroFields());
    }

    @Test
    public void testIsFilled() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        // Initially, the board should not be filled
        assertFalse(board.isFilled());

        // Fill the board
        for (int i = 0; i < SudokuBoard.SIZE; i++) {
            for (int j = 0; j < SudokuBoard.SIZE; j++) {
                board.set(i, j, 1); // Assuming 1 is a valid value
            }
        }

        assertTrue(board.isFilled());
    }

    @Test
    public void testGetField() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        int x = 0;
        int y = 0;
        int value = 5;
        board.set(x, y, value);
        SudokuField field = board.getField(x, y);
        assertEquals(value, field.getValue());
    }

    @Test
    public void testToString() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 5);
        board.set(0, 1, 3);
        board.set(1, 0, 2);
        board.set(1, 1, 4);

        String expected = "5 3\n2 4\n";
        for (int i = 2; i < SudokuBoard.SIZE; i++) {
            expected += "0 0\n";
        }

        String actual = board.toString();
        String[] actualLines = actual.split("\n");
        String actualFirstTwoColumns = "";
        for (String line : actualLines) {
            String[] values = line.split(" ");
            actualFirstTwoColumns += values[0] + " " + values[1] + "\n";
        }

        assertEquals(expected, actualFirstTwoColumns);
    }

    @Test
    public void testSudokuBoardClone() throws CloneNotSupportedException {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard board2 = (SudokuBoard) board1.clone();
        assertNotSame(board1, board2);
        assertEquals(board1, board2);

        // Modify the state of the original board
        board1.set(0, 0, 5);

        // Check that the state of the cloned board has not changed
        assertNotEquals(board1.get(0, 0), board2.get(0, 0));
    }
}

