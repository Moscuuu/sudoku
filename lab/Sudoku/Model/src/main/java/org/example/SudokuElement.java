package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface SudokuElement extends Cloneable {
    List<SudokuField> getFields();

    Object clone() throws CloneNotSupportedException;

    default boolean verify() {
        Set<Integer> values = new HashSet<>();
        for (SudokuField field : getFields()) {
            int value = field.getValue();
            if (value == 0) {
                continue;
            }
            if (values.contains(value)) {
                return false;
            }
            values.add(value);
        }
        return true;
    }
}
