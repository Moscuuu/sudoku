package org.exampleview;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Dao;
import org.example.DaoException;
import org.example.I18N;
import org.example.SudokuBoard;
import org.example.SudokuBoardDaoFactory;
import org.exampleview.exception.*;

import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindowControl {

    @FXML
    private ComboBox comboBoxSystemLang;

    private static final Logger logger = Logger.getLogger(MainWindowControl.class.getName(),"Language");

    private Authors authors = new Authors();
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");
    private PopOutWindow popOutWindow = new PopOutWindow();
    static DifficultyLevel.Level level;
    private FileChooser fileChooser;
    private static SudokuBoard ReadSudokuBoard;
    private SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

    private String language;

    public static DifficultyLevel.Level getLevel() {
        return level;
    }

    public static void setLevel(DifficultyLevel.Level level) {
        MainWindowControl.level = level;
    }

    public static SudokuBoard getReadSudokuBoard() {
        return ReadSudokuBoard;
    }

    public static void setReadSudokuBoard(SudokuBoard readSudokuBoard) {
       ReadSudokuBoard = readSudokuBoard;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @FXML
    private void initialize() {
        comboBoxSystemLang.getItems().addAll(
                bundle.getString("_comboLang1"),
                bundle.getString("_comboLang2")
        );
    }

    public void onActionHardLevel(ActionEvent actionEvent) {
        try {
            level = DifficultyLevel.Level.Hard;
            Scene scene = FxmlStageSetup.buildScene("BoardWindow.fxml", bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (FxmlException e) {
            logger.log(Level.WARNING, "_levelWindowError", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_levelWindowError"), Alert.AlertType.WARNING);
        }
    }

    public void onActionEasyLevel(ActionEvent actionEvent) {
        try {
            level = DifficultyLevel.Level.Easy;
            Scene scene = FxmlStageSetup.buildScene("BoardWindow.fxml", bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (FxmlException e) {
            logger.log(Level.WARNING, "_levelWindowError", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_levelWindowError"), Alert.AlertType.WARNING);
        }
    }

    public void onActionMediumLevel(ActionEvent actionEvent) {
        try {
            level = DifficultyLevel.Level.Medium;
            Scene scene = FxmlStageSetup.buildScene("BoardWindow.fxml", bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (FxmlException e) {
            logger.log(Level.WARNING, "_levelWindowError", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_levelWindowError"), Alert.AlertType.WARNING);
        }
    }

    public void onActionButtonFile(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(FxmlStageSetup.getStage());

        if (selectedFile != null) {
            if (selectedFile.exists() && selectedFile.canRead() && selectedFile.isFile() && selectedFile.length() > 0) {
                try {
                    String filename = selectedFile.getName();
                    Dao<SudokuBoard> dao = factory.getFileDao(filename);
                    ReadSudokuBoard = dao.read();

                    Scene scene = FxmlStageSetup.buildScene("BoardWindow.fxml", bundle);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                } catch (DaoException | FxmlException e) {
                    logger.log(Level.WARNING, "_IOError", e.getMessage());
                    popOutWindow.messageBox(bundle.getString("_warning"),
                            bundle.getString("_IOError"), Alert.AlertType.WARNING);
                }
            } else {
                logger.warning("_fileError");
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_fileError"), Alert.AlertType.WARNING);
            }
        } else {
            logger.warning("_fileError2");
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_fileError2"), Alert.AlertType.WARNING);
        }
    }

    public void onActionConfirmLang(ActionEvent actionEvent) {
        try {
            Object selectedItem = comboBoxSystemLang.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                throw new CustomNullPointerException();
            }
            this.language = selectedItem.toString();

            if (language.equals(bundle.getString("_comboLang1"))) {
                I18N.setLocale(new Locale("en"));
            } else if (language.equals(bundle.getString("_comboLang2"))) {
                I18N.setLocale(new Locale("pl"));
            }

            bundle = ResourceBundle.getBundle("Language", Locale.getDefault());

            Scene scene = FxmlStageSetup.buildScene("MainWindow.fxml", bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);

            stage.setTitle(bundle.getString("_windowTitle"));

        } catch (CustomNullPointerException e) {
            logger.log(Level.WARNING, "_langWindow", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_langWindow"), Alert.AlertType.WARNING);
        } catch (FxmlException e) {
            logger.log(Level.WARNING, "_IOError2", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_IOError2"), Alert.AlertType.WARNING);
        }
    }

    public void onActionDatabase(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("_fileName"));
        dialog.setHeaderText(bundle.getString("_enterFileName"));
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                String filename = result.get();
                Dao<SudokuBoard> dao = factory.getDatabaseDao(filename);
                ReadSudokuBoard = dao.read();

                Scene scene = FxmlStageSetup.buildScene("BoardWindow.fxml", bundle);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (DaoException | FxmlException e) {
                logger.log(Level.WARNING, "_IOError", e);
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_IOError"), Alert.AlertType.WARNING);
            }
        } else {
            logger.warning("_fileError2");
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_fileError2"), Alert.AlertType.WARNING);
        }
    }

    public void onActionButtonAuthors(ActionEvent actionEvent) {
        popOutWindow.messageBox("",
                authors.getObject("1. ") + "\n" + authors.getObject("2. "),
                Alert.AlertType.INFORMATION);
    }
}
