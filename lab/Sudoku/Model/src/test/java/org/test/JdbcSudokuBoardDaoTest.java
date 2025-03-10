package org.test;

import org.example.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {

    @Test
    void getDatabaseDaoWithNullFilename() {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        assertThrows(DaoException.class, () -> factory.getDatabaseDao(null));
    }

    @Test
    void getDatabaseDaoWithValidFilename() {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        try {
            JdbcSudokuBoardDao dao = factory.getDatabaseDao("test");
            assertNotNull(dao);
        } catch (DaoException e) {
            fail("Exception should not be thrown with valid filename");
        }
    }

    @Test
    void constructorWithValidFilename() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("test");
        assertNotNull(dao);
    }

    @Test
    void constructorWithNullFilename() {
        assertThrows(CustomNullPointerException.class, () -> new JdbcSudokuBoardDao(null));
    }

    @Test
    void readWithValidFilename() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("test");
        try {
            SudokuBoard board = dao.read();
            assertNotNull(board);
            // You can add more assertions here to check the contents of the board
        } catch (DatabaseException e) {
            fail("Exception should not be thrown with valid filename", e);
            e.printStackTrace();
        }
    }

    @Test
    void readWithInvalidFilename() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("invalid_test");
        assertThrows(DatabaseException.class, dao::read);
    }

    @Test
    void writeWithValidSudokuBoard() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("test");
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
        try {
            dao.write(board);
            SudokuBoard readBoard = dao.read();
            assertEquals(board, readBoard);
        } catch (DatabaseException e) {
            fail("Exception should not be thrown with valid SudokuBoard", e);
        }
    }

    @Test
    void writeWithNullSudokuBoard() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("test");
        assertThrows(CustomNullPointerException.class, () -> dao.write(null));
    }

    @Test
    void namesWithExistingData() {
        JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("test");
        try {
            List<String> names = dao.names();
            assertNotNull(names);
            // If there are known filenames in the database, you can add assertions here to check them
            // For example:
            // assertTrue(names.contains("known_filename"));
        } catch (DatabaseException e) {
            fail("Exception should not be thrown when there is data in the database", e);
        }
    }
}
