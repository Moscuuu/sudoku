package org.exampleview;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.example.*;
import org.example.CloneException;
import org.example.DaoException;
import org.exampleview.exception.CustomNullPointerException;
import org.exampleview.exception.FxmlException;

import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.SudokuBoard.SIZE;


public class BoardWindowControl {

    @FXML
    private GridPane sudokuBoardGrid;

    @FXML
    private Pane linePane;

    private static final Logger logger = Logger.getLogger(BoardWindowControl.class.getName(),"Language");
    private static final double CELL_SIZE = 50;
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");
    private final SudokuBoardDaoFactory prototyp
            = new SudokuBoardDaoFactory(new SudokuBoard(new BacktrackingSudokuSolver()));
    private SudokuBoard sudokuBoard;
    private SudokuBoard sudokuBoardCopy;
    private boolean isSaved = false;

    {
        try {
            sudokuBoard = prototyp.createInstance();
            sudokuBoardCopy = prototyp.createInstance();
        } catch (DaoException e) {
            logger.log(Level.WARNING, "_instanceError", e.getMessage());
        }
    }

    private static DifficultyLevel difficultyLevel = new DifficultyLevel();
    private PopOutWindow popOutWindow = new PopOutWindow();
    private SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();


        @SuppressWarnings("PMD.UnusedPrivateMethod")
        @FXML
        private void initialize() {
            setupGame();
            fillGrid();
            drawLines();
            isSaved = false;
        }

        private void setupGame() {
            createAndSolveBoard();
            setDifficultyLevel();
        }

        private void createAndSolveBoard() {
            if (MainWindowControl.getReadSudokuBoard() != null) {
                sudokuBoard = MainWindowControl.getReadSudokuBoard();
                MainWindowControl.setReadSudokuBoard(null);
            } else {
                sudokuBoard.solveGame();
            }

            try {
                sudokuBoardCopy = (SudokuBoard) sudokuBoard.clone();
                sudokuBoardCopy.solveGame();
            } catch (CloneException e) {
                logger.log(Level.WARNING, "_cloneError", e.getMessage());
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_cloneError"), Alert.AlertType.WARNING);
            }
        }

        private void setDifficultyLevel() {
            if (MainWindowControl.getLevel() != null) {
                difficultyLevel.clearRandomPositions();
                difficultyLevel.chooseLevel(sudokuBoard, MainWindowControl.getLevel());
            }
        }


    private static class SudokuFieldStringConverter extends IntegerStringConverter {
        @Override
        public String toString(Integer value) {
            return value == 0 ? "" : value.toString();
        }

        @Override
        public Integer fromString(String string) {
            return string.isEmpty() ? 0 : Integer.parseInt(string);
        }
    }

    public void onActionReturnButton(ActionEvent actionEvent) {
            try {
                MainWindowControl.setLevel(null);
                Scene scene = FxmlStageSetup.buildScene("MainWindow.fxml", bundle);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (FxmlException e) {
                logger.log(Level.WARNING, "_IOError2", e.getMessage());
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_IOError2"), Alert.AlertType.WARNING);
            }
    }

    private void fillGrid() {
        SudokuFieldStringConverter converter = new SudokuFieldStringConverter();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                final int row = i;
                final int col = j;
                TextField textField = new TextField();
                textField.setMinSize(15, 50); // Adjust these values as needed
                textField.setFont(Font.font(18));
                SudokuField sudokuField = sudokuBoard.getField(i, j);

                // Create a TextFormatter to allow only digits 1-9
                TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, 0, change -> {
                    String newText = change.getControlNewText();
                    // Allow empty string (to clear the text field) or a single digit between 1 and 9
                    if (newText.isEmpty() || (newText.matches("[1-9]") && newText.length() == 1)) {
                        return change;
                    }
                    return null;
                });
                textField.setTextFormatter(textFormatter);

                if (sudokuField.getValue() != 0) {
                    textField.setDisable(true);
                    textField.setText(String.valueOf(sudokuField.getValue()));
                } else {
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        // Ignore empty strings
                        if (!newValue.isEmpty() && validateFieldInput(textField, row, col)) {
                                sudokuField.setValue(Integer.parseInt(newValue));
                        }
                    });
                }
                sudokuBoardGrid.add(textField, j, i);
            }
        }
    }


    public void onActionCheckButton(ActionEvent actionEvent) {
        boolean isWon = true;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                TextField textField = (TextField) getNodeByRowColumnIndex(i, j, sudokuBoardGrid);
                if (textField == null) {
                    continue;
                }
                String text = textField.getText();
                if (text.isEmpty()) {
                    if (sudokuBoardCopy.get(i, j) != 0) {
                        isWon = false;
                        break;
                    }
                } else {
                    int userValue = Integer.parseInt(text);
                    if (userValue != sudokuBoardCopy.get(i, j)) {
                        isWon = false;
                        break;
                    }
                }
            }
            if (!isWon) {
                break;
            }
        }

        if (isWon) {
            logger.info("_wonWindow");
            popOutWindow.messageBox("", bundle.getString("_wonWindow"), Alert.AlertType.INFORMATION);
        } else {
            logger.info("_lostWindow");
            popOutWindow.messageBox("", bundle.getString("_lostWindow"), Alert.AlertType.INFORMATION);
        }
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column
                    && node instanceof TextField) {
                return node;
            }
        }

        return null;
    }

    private boolean validateFieldInput(TextField textField, int row, int col) {
        SudokuFieldStringConverter converter = new SudokuFieldStringConverter();
        String text = textField.getText();
        int temp = converter.fromString(text);
        sudokuBoard.set(row, col, temp);
        if (!sudokuBoard.getRow(row).verify()
                || !sudokuBoard.getColumn(col).verify()
                || !sudokuBoard.getBox(row / 3 * 3, col / 3 * 3).verify()) {
            logger.info("_invalidInputError");
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_invalidInputError"), Alert.AlertType.ERROR);
            textField.setText(converter.toString(0));
            sudokuBoard.set(row, col, 0);
            return false;
        }
        sudokuBoard.set(row, col, temp);
        return true;
    }

    public void onActionSaveToFileButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(FxmlStageSetup.getStage());
        try {
            if (file == null) {
                throw new CustomNullPointerException();
            }
            Dao<SudokuBoard> dao = factory.getFileDao(file.getAbsolutePath());
            dao.write(sudokuBoard);

        } catch (CustomNullPointerException e) {
            logger.log(Level.WARNING, "_fileError2", e.getMessage());
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_fileError2"), Alert.AlertType.WARNING);

        }  catch (DaoException e) {
                logger.log(Level.WARNING, "_savingError", e.getMessage());
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_savingError"), Alert.AlertType.WARNING);
            }
    }

    public void onActionSaveToDatabaseButton(ActionEvent actionEvent) {
        if (isSaved) {
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_alreadySaved"), Alert.AlertType.WARNING);
            return;
        }

        if (sudokuBoard.checkBoard()) {
            try {
                // Use a FileChooser to let the user select a file name
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(FxmlStageSetup.getStage());
                if (file == null) {
                    throw new CustomNullPointerException();
                }

                // Use the selected file name as the filename for the Sudoku board
                JdbcSudokuBoardDao dao = factory.getDatabaseDao(file.getName());
                dao.write(sudokuBoard);
                isSaved = true;
                popOutWindow.messageBox(bundle.getString("_success"),
                        bundle.getString("_databaseSuccess"), Alert.AlertType.INFORMATION);
            } catch (CustomNullPointerException e) {
                logger.log(Level.WARNING, "_fileError2", e.getMessage());
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_fileError2"), Alert.AlertType.WARNING);
            } catch (DaoException e) {
                logger.log(Level.WARNING, "Cannot save to database!", e);
                popOutWindow.messageBox(bundle.getString("_warning"),
                        bundle.getString("_databaseError"), Alert.AlertType.WARNING);
            }
        } else {
            popOutWindow.messageBox(bundle.getString("_warning"),
                    bundle.getString("_validWindow"), Alert.AlertType.WARNING);
        }
    }

    private void drawLines() {
        double offset = CELL_SIZE / 2.8; // Calculate the offset
        for (int i = 0; i < SIZE + 1; i++) {
            for (int j = 0; j < SIZE + 1; j++) {
                if (j == 2) {
                    Line line = new Line();
                    line.setStartX((j + 1) * CELL_SIZE + offset); // Add offset for j >= 2 + (j == 2 ? offset : 0)
                    line.setStartY(i * CELL_SIZE);
                    line.setEndX((j + 1) * CELL_SIZE + offset); // Add offset for j >= 2 + (j == 2 ? offset : 0)
                    line.setEndY((i + 1) * CELL_SIZE);
                    line.setStrokeWidth(3);
                    line.setStroke(Color.BLACK);
                    linePane.getChildren().add(line);
                }
                if (i == 2) {
                    Line line = new Line();
                    line.setStartX(j * CELL_SIZE);
                    line.setStartY((i + 1) * CELL_SIZE + offset); // Add offset for i >= 2
                    line.setEndX((j + 1) * CELL_SIZE);
                    line.setEndY((i + 1) * CELL_SIZE + offset); // Add offset for i >= 2
                    line.setStrokeWidth(3);
                    line.setStroke(Color.BLACK);
                    linePane.getChildren().add(line);
                }
                if (j == 5) {
                    Line line = new Line();
                    line.setStartX((j + 1) * CELL_SIZE + 2 * offset); // Add offset for j >= 2 + (j == 2 ? offset : 0)
                    line.setStartY(i * CELL_SIZE);
                    line.setEndX((j + 1) * CELL_SIZE + 2 * offset); // Add offset for j >= 2 + (j == 2 ? offset : 0)
                    line.setEndY((i + 1) * CELL_SIZE);
                    line.setStrokeWidth(3);
                    line.setStroke(Color.BLACK);
                    linePane.getChildren().add(line);
                }
                if (i == 5) {
                    Line line = new Line();
                    line.setStartX(j * CELL_SIZE);
                    line.setStartY((i + 1) * CELL_SIZE + 2 * offset); // Add offset for i >= 2
                    line.setEndX((j + 1) * CELL_SIZE);
                    line.setEndY((i + 1) * CELL_SIZE + 2 * offset); // Add offset for i >= 2
                    line.setStrokeWidth(3);
                    line.setStroke(Color.BLACK);
                    linePane.getChildren().add(line);
                }
            }
        }
    }
}
