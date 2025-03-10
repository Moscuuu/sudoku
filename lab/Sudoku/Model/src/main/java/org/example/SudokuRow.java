package org.example;


import java.util.ArrayList;
import java.util.List;

public class SudokuRow implements SudokuElement {
    private List<SudokuField> fields;

    public SudokuRow(List<SudokuField> fields) {
        this.fields = fields;
    }

    @Override
    public List<SudokuField> getFields() {
        return fields;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
            SudokuRow clone = (SudokuRow) super.clone();
            clone.fields = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                clone.fields.add((SudokuField) this.fields.get(i).clone());
            }
            return clone;
    }
}
