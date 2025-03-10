package org.example;

import java.util.ArrayList;
import java.util.List;

public class SudokuBox implements SudokuElement, Cloneable {
    private List<SudokuField> fields;
    public static final int BOX_SIZE = 3;

    public SudokuBox(List<SudokuField> fields) {
        this.fields = fields;
    }

    @Override
    public List<SudokuField> getFields() {
        return fields;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuBox clone = (SudokuBox) super.clone();
        clone.fields = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            clone.fields.add((SudokuField) this.fields.get(i).clone());
        }
        return clone;
    }
}
