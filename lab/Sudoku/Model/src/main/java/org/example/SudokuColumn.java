package org.example;

import java.util.ArrayList;
import java.util.List;

public class SudokuColumn implements SudokuElement {
    private List<SudokuField> fields;

    public SudokuColumn(List<SudokuField> fields) {
        this.fields = fields;
    }

    @Override
    public List<SudokuField> getFields() {
        return fields;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuColumn clone = (SudokuColumn) super.clone();
        clone.fields = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            clone.fields.add((SudokuField) this.fields.get(i).clone());
        }
        return clone;
    }
}

