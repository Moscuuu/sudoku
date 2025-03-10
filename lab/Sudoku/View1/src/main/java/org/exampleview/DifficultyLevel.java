package org.exampleview;

import org.example.SudokuBoard;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DifficultyLevel {
    /*------------------------ FIELDS REGION ------------------------*/
    public static final int BASIC_LEVEL = 5;

    public enum Level {
        FilledBoard, Easy, Medium, Hard
    }

    private Random rand = new Random();

    private Set<FieldCoordinates> randomPositions = new HashSet<>();

    /*------------------------ METHODS REGION ------------------------*/


    /**
     * Method remove certain number of fields.
     *
     * @param capacity - integer number of field to remove
     */
    private void fillRandomPositionsList(int capacity) {
        for (int i = 0; i < capacity; i++) {
            boolean isElementAdded = false;

            while (!isElementAdded) {
                int axisX = rand.nextInt(9);
                int axisY = rand.nextInt(9);
                isElementAdded = randomPositions.add(new FieldCoordinates(axisX, axisY));
            }
        }
    }

    public void clearRandomPositions() {
        randomPositions.clear();
    }


    public SudokuBoard chooseLevel(SudokuBoard sudokuBoard, Level level) {

        if (level == null) {
            fillRandomPositionsList(0);
            return sudokuBoard;
        }

        switch (level) {
            case FilledBoard: {
                fillRandomPositionsList(0);
                break;
            }
            case Level.Easy: {
                fillRandomPositionsList(BASIC_LEVEL * Level.Easy.ordinal());
                break;
            }
            case Level.Medium: {
                fillRandomPositionsList(BASIC_LEVEL * Level.Medium.ordinal());
                break;
            }
            case Level.Hard: {
                fillRandomPositionsList(BASIC_LEVEL * Level.Hard.ordinal());
                break;
            }
            default: {
                fillRandomPositionsList(0);
            }
        }

        for (FieldCoordinates it : randomPositions) {
            sudokuBoard.set(it.getAxisX(), it.getAxisY(), 0); // Remove cells from the filled board
        }

        return sudokuBoard;
    }
}