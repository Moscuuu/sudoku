package org.example;

import java.io.IOException;

public class BaseIOexception extends IOException {
    public BaseIOexception(String message) {
        super(I18N.get(message));
    }

    public BaseIOexception(String message, Throwable cause) {
        super(I18N.get(message), cause);
    }
}
