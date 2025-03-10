package org.exampleview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.exampleview.exception.FxmlException;

import java.io.IOException;
import java.util.ResourceBundle;

public class FxmlStageSetup {

    /*------------------------ FIELDS REGION ------------------------*/
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static Stage stage;

    /*------------------------ METHODS REGION ------------------------*/

    public static Stage getStage() {
        return stage;
    }


    private static void setStage(Stage stage) {
        FxmlStageSetup.stage = stage;
    }

    private static Parent loadFxml(String fxml, ResourceBundle bundle) throws FxmlException {
        try {
            return new FXMLLoader(FxmlStageSetup.class.getResource("/" + fxml), bundle).load();

        } catch (IOException e) {
            throw new FxmlException("_loadFxmlError",e);
        }
    }

    public static Scene buildScene(String filePath, ResourceBundle bundle) throws FxmlException {
        try {
            return new Scene(loadFxml(filePath, bundle));

        } catch (IOException e) {
            throw new FxmlException("_buildSceneError",e);
        }
    }

    public static void buildStage(Stage stage, String filePath, String title, ResourceBundle bundle)
            throws FxmlException {
        setStage(stage);
        stage.setScene(new Scene(loadFxml(filePath, bundle)));
        stage.setTitle(title);
        stage.setMinWidth(500);
        stage.setMinHeight(733);
        stage.sizeToScene();
        stage.show();
    }
}


