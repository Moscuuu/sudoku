package org.exampleview.exception;

import org.example.I18N;

import java.io.IOException;

public class FxmlException extends IOException {
    public FxmlException(String message, Throwable cause) {
        super(I18N.get(message),cause);
    }
}
