package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    private int value;

    public SudokuField(int value) {
        this.value = value;
    }

    public SudokuField() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(value, ((SudokuField) obj).value).isEquals();
    }

    @Override
    public String toString() {
        return "SudokuField{value=" + value + "}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
            return super.clone();
    }

    @Override
    public int compareTo(SudokuField o) {
        if (this.getValue() == o.getValue()) {
            return 0;
        } else if (this.getValue() > o.getValue()) {
            return 1;
        } else {
            return -1;
        }
    }
}
