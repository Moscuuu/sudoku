package org.test;

import org.example.BacktrackingSudokuSolver;
import org.exampleview.DifficultyLevel;
import org.example.SudokuBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DifficultyLevelTest {
    private DifficultyLevel difficultyLevel;
    private SudokuBoard sudokuBoard;
    private BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();

    @BeforeEach
    public void setup() {
        difficultyLevel = new DifficultyLevel();
        sudokuBoard = new SudokuBoard(solver);
        solver.solve(sudokuBoard);
    }

    @Test
    public void testChooseLevelFilledBoard() {
        SudokuBoard result = difficultyLevel.chooseLevel(sudokuBoard, DifficultyLevel.Level.FilledBoard);
        int zeroFields = result.countZeroFields();
        assertEquals(0, zeroFields);
    }

    @Test
    public void testChooseLevelEasy() {
        SudokuBoard result = difficultyLevel.chooseLevel(sudokuBoard, DifficultyLevel.Level.Easy);
        int zeroFields = result.countZeroFields();
        assertEquals(5, zeroFields);
    }

    @Test
    public void testChooseLevelMedium() {
        SudokuBoard result = difficultyLevel.chooseLevel(sudokuBoard, DifficultyLevel.Level.Medium);
        int zeroFields = result.countZeroFields();
        assertEquals(10, zeroFields);
    }

    @Test
    public void testChooseLevelHard() {
        SudokuBoard result = difficultyLevel.chooseLevel(sudokuBoard, DifficultyLevel.Level.Hard);
        int zeroFields = result.countZeroFields();
        assertEquals(15, zeroFields);
    }

    @Test
    public void testChooseLevelDefault() {
        SudokuBoard result = difficultyLevel.chooseLevel(sudokuBoard,null);
        int zeroFields = result.countZeroFields();
        assertEquals(0, zeroFields);
    }

}