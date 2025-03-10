package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SudokuBoard implements Serializable, Cloneable {
    List<SudokuField> board = Arrays.asList(new SudokuField[81]);
    public static final int SIZE = 9;
    public static final int NO_OF_CELLS = SIZE * SIZE;

    private SudokuSolver solver;

    public SudokuBoard(SudokuSolver solver) {
        this.solver = solver;
        for (int i = 0; i < NO_OF_CELLS; i++) {
            board.set(i, new SudokuField(0));
        }
    }

    public boolean solveGame() {
        if (!checkBoard()) {
            return false;
        }
        return solver.solve(this);
    }

    public SudokuField getField(int x, int y) {
        return board.get(x * SIZE + y);
    }

    public int get(int x, int y) {
        int calculatedIndex = x * SIZE + y;
        return board.get(calculatedIndex).getValue();
    }

    public void set(int x, int y, int value) {
        int calculatedIndex = x * SIZE + y;
        SudokuField field = board.get(calculatedIndex);
        field.setValue(value);
    }

    public boolean checkBoard() {
        for (int i = 0; i < SIZE; i++) {
            if (!getRow(i).verify() || !getColumn(i).verify() || !getBox(i % 3 * 3, i / 3 * 3).verify()) {
                return false;
            }
        }
        return true;
    }

    public SudokuRow getRow(int x) {
        List<SudokuField> rowFields = Arrays.asList(new SudokuField[SIZE]);
        List<SudokuField> unmodifiableRowFields = Collections.unmodifiableList(rowFields);
        for (int y = 0; y < SIZE; y++) {
            rowFields.set(y, board.get(x * SIZE + y));
        }
        return new SudokuRow(unmodifiableRowFields);
    }

    public SudokuColumn getColumn(int y) {
        List<SudokuField> columnFields = Arrays.asList(new SudokuField[SIZE]);
        List<SudokuField> unmodifiableColumnFields = Collections.unmodifiableList(columnFields);
        for (int x = 0; x < SIZE; x++) {
            columnFields.set(x, board.get(x * SIZE + y));
        }
        return new SudokuColumn(unmodifiableColumnFields);
    }

    public SudokuBox getBox(int x, int y) {
        List<SudokuField> boxFields = new ArrayList<>();
        for (int i = 0; i < SudokuBox.BOX_SIZE; i++) {
            for (int j = 0; j < SudokuBox.BOX_SIZE; j++) {
                boxFields.add(board.get((x + i) * SIZE + y + j));
            }
        }
        return new SudokuBox(Collections.unmodifiableList(boxFields));
    }

    @Override
    public boolean equals(final Object obj) {
        return new EqualsBuilder().append(board, ((SudokuBoard) obj).board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(get(i, j));
                if (j < SIZE - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int countZeroFields() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (get(i, j) == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isFilled() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (get(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Object clone() throws CloneException {
        SudokuBoard clone = new SudokuBoard(this.solver);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                clone.set(i, j, this.get(i, j));
            }
        }
        return clone;
    }
}