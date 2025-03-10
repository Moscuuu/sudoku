package org.example;

public class CustomNullPointerException extends NullPointerException {
    public CustomNullPointerException(String message) {
        super(I18N.get(message));
    }
}
