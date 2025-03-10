package org.exampleview;

import java.util.ListResourceBundle;

public class Authors extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"1. ", "Aleksander Moszyński ",},
                {"2. ", "Marcin Grzelak "}
        };
    }
}
