package org.test;

import org.example.*;
import org.example.DaoException;
import org.example.ReaderOrWriterException;
import org.example.CloneException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

public class FileSudokuBoardDaoTest {

    private SudokuBoard board;
    private String filename;
    private SudokuBoardDaoFactory factory;

    @BeforeEach
    public void setUp() {
        System.setProperty("isTest", "true");
        filename = "test.txt";
        factory = new SudokuBoardDaoFactory();
        board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
    }

    @Test
    public void testWrite() {
        try (Dao<SudokuBoard> dao = factory.getFileDao(filename)) {
            dao.write(board);
            File file = new File(filename);
            assertTrue(file.exists());
            System.out.println("Test Write: " + (file.exists() ? "Passed" : "Failed"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRead() {
        try (Dao<SudokuBoard> dao = factory.getFileDao(filename)) {
            dao.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNames() {
        try (Dao<SudokuBoard> dao = factory.getFileDao(filename)) {
            List<String> names = dao.names();
            assertTrue(names.contains(filename));
            System.out.println("All filenames: " + names);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWriteIOException() {
        assertThrows(IOException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao("nonexistent/directory/" + filename)) {
                dao.write(board);
            }
        });
        System.out.println("Test WriteIOException: Passed");
    }

    @Test
    public void testWriteWithUsedWriter() {
        assertThrows(DaoException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao(filename)) {
                dao.write(board); // First write operation
                dao.write(board); // Second write operation should throw RuntimeException
            }
        });
    }

    @Test
    public void testReadWithUsedReader() {
        assertThrows(DaoException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao(filename)) {
                dao.read(); // First read operation
                dao.read(); // Second read operation should throw RuntimeException
            }
        });
    }

    @Test
    public void testReadInvalidFileFormat() {
        assertThrows(IOException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao("invalid_format.txt")) {
                dao.read(); // Reading a file with invalid format should throw IOException
            }
        });
    }

    @Test
    public void testReadNotEnoughLines() {
        assertThrows(IOException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao("not_enough_lines.txt")) {
                dao.read(); // Reading a file with not enough lines should throw IOException
            }
        });
    }

    @Test
    public void testReadFileNotFound() {
        assertThrows(DaoException.class, () -> {
            try (Dao<SudokuBoard> dao = factory.getFileDao("nonexistent_file")) {
                dao.read(); // Reading a non-existent file should throw FileNotFoundException
            }
        });
    }

    @Test
    public void testGetFileDaoWithNullFilename() {
        try {
            factory.getFileDao(null); // Calling getFileDao with null filename should throw RuntimeException
            fail("Expected RuntimeException to be thrown");
        } catch (DaoException e) {
            assertEquals(I18N.get("filenameCannotBeNull"), e.getMessage());
        }
    }
    @Test
    public void testCreateInstance() throws DaoException {
        board = new SudokuBoard(new BacktrackingSudokuSolver());
        factory = new SudokuBoardDaoFactory(board);
        SudokuBoard clonedBoard = factory.createInstance();
        assertNotSame(board, clonedBoard);
        assertEquals(board, clonedBoard);
    }

//    @Test
//    public void testCreateInstance2() {
//        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory(new NonCloneableSudokuBoard());
//
//        Exception exception = assertThrows(RuntimeException.class, factory::createInstance);
//        assertEquals("Cloning not supported", exception.getMessage());
//    }
//
//    private static class NonCloneableSudokuBoard extends SudokuBoard {
//        @Override
//        public Object clone() {
//            throw new DaoException("Cloning not supported");
//        }
//    }
@Test
public void testCloseIOException() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Create mock FileWriter and FileReader
    FileWriter mockWriter = Mockito.mock(FileWriter.class);
    FileReader mockReader = Mockito.mock(FileReader.class);

    // Configure the mocks to throw an IOException when close() is called
    doThrow(new IOException()).when(mockWriter).close();
    doThrow(new IOException()).when(mockReader).close();

    // Create a FileSudokuBoardDao
    FileSudokuBoardDao dao = new FileSudokuBoardDao("test.txt");

    // Use reflection to set the private fields with the mock objects
    Field writerField = FileSudokuBoardDao.class.getDeclaredField("writer");
    Field readerField = FileSudokuBoardDao.class.getDeclaredField("reader");
    writerField.setAccessible(true);
    readerField.setAccessible(true);
    writerField.set(dao, mockWriter);
    readerField.set(dao, mockReader);

    // Now the close() method should throw a ReaderOrWriterException
    assertThrows(ReaderOrWriterException.class, dao::close);
}
    @Test
    public void testCloseIOExceptionForReader() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create mock FileReader
        FileReader mockReader = Mockito.mock(FileReader.class);

        // Configure the mock to throw an IOException when close() is called
        doThrow(new IOException()).when(mockReader).close();

        // Create a FileSudokuBoardDao
        FileSudokuBoardDao dao = new FileSudokuBoardDao("test.txt");

        // Use reflection to set the private field with the mock object
        Field readerField = FileSudokuBoardDao.class.getDeclaredField("reader");
        readerField.setAccessible(true);
        readerField.set(dao, mockReader);

        // Now the close() method should throw a ReaderOrWriterException
        assertThrows(ReaderOrWriterException.class, dao::close);
    }

    @Test
    public void testCreateInstanceCloneException() throws CloneException {
        // Mock the prototype object to throw a CloneException when clone() is called
        SudokuBoard mockPrototype = Mockito.mock(SudokuBoard.class);
        Mockito.when(mockPrototype.clone()).thenThrow(new CloneException(new CloneNotSupportedException()));

        // Create a SudokuBoardDaoFactory with the mocked prototype
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory(mockPrototype);

        // Call createInstance() method, which should now throw a DaoException
        assertThrows(DaoException.class, factory::createInstance);
    }
}
