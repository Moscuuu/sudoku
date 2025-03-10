package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {
    @Override
    public boolean solve(SudokuBoard board) {
        return solve(board, 0, 0);
    }

    private boolean solve(SudokuBoard board, int x, int y) {
        if (x == 9) {
            // We've reached the end of the board, so it must be solved
            return true;
        }

        if (board.get(x, y) != 0) {
            // The current cell is already filled, so move on to the next cell
            if (y == 8) {
                return solve(board, x + 1, 0);
            } else {
                return solve(board, x, y + 1);
            }
        }


        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            values.add(i);
        }
        Collections.shuffle(values);

        for (int value : values) {
            if (isValidMove(board, x, y, value)) {
                board.set(x, y, value);
                if (y == 8) {
                    if (solve(board, x + 1, 0)) {
                        return true;
                    }
                } else {
                    if (solve(board, x, y + 1)) {
                        return true;
                    }
                }
                board.set(x, y, 0); // undo the move if it leads to no solution
            }
        }

        // If we've reached this point, then there are no valid moves for the current cell
        return false;
    }


    public boolean isValidMove(SudokuBoard board, int x, int y, int value) {
        // Check row
        for (int i = 0; i < 9; i++) {
            if (board.get(x, i) == value && i != y) {
                return false;
            }
        }
        // Check column
        for (int i = 0; i < 9; i++) {
            if (board.get(i, y) == value && i != x) {
                return false;
            }
        }
        // Check box
        int boxX = x - x % 3;
        int boxY = y - y % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.get(boxX + i, boxY + j) == value && (boxX + i != x || boxY + j != y))  {
                    return false;
                }
            }
        }
        return true;
    }
}