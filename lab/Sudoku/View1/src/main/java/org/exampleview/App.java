package org.exampleview;

import javafx.application.Application;
import javafx.stage.Stage;
import org.exampleview.exception.FxmlException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger logger = Logger.getLogger(App.class.getName(), "Language");
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");


    static {
            String configPath = App.class.getClassLoader().getResource("logging.properties").getFile();
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.log(Level.WARNING,"_configError", e.getMessage());
        }
        }

    public static void main(String[] args) {
        logger.info("_appStarted");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws FxmlException {
        try {
            FxmlStageSetup.buildStage(stage, "MainWindow.fxml", bundle.getString("_windowTitle"), bundle);
        } catch (IOException e) {
            throw new FxmlException("_startError", e);
        }
    }
}
