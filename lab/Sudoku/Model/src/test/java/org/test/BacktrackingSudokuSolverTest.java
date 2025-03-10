package org.test;

import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BacktrackingSudokuSolverTest {

    @Test
    public void testSolve() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        assertTrue(board.solveGame());
    }

    @Test
    public void testIsValidMove() {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        assertTrue(solver.isValidMove(board, 0, 0, 1));
    }
}
