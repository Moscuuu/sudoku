package org.test;

import org.example.I18N;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import javafx.beans.property.ObjectProperty;

import static org.junit.jupiter.api.Assertions.*;

public class I18NTest {

    @Test
    public void testSetLocale() {
        // Arrange
        Locale expectedLocale = new Locale("pl");

        // Act
        I18N.setLocale(expectedLocale);

        // Assert
        ObjectProperty<Locale> actualLocaleProperty = I18N.localeProperty();
        assertNotNull(actualLocaleProperty);
        assertEquals(expectedLocale, actualLocaleProperty.get());
        assertEquals(expectedLocale, Locale.getDefault());
    }
}
