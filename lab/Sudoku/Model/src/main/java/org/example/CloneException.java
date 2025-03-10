package org.example;

public class CloneException extends CloneNotSupportedException {
    public CloneException(Throwable cause) {
        super(String.valueOf(cause));
    }
}
