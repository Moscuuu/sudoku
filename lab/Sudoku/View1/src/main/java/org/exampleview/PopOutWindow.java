package org.exampleview;

import javafx.scene.control.Alert;


public class PopOutWindow {

    /*------------------------ METHODS REGION ------------------------*/

    /**
     * Method pop out message box.
     *
     * @param title     - Title of box
     * @param message   - message shown in box
     * @param alertType - type od shown alert
     */
    public void messageBox(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
