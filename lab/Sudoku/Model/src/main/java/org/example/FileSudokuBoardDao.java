package org.example;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    private FileWriter writer = null;
    private FileReader reader = null;
    public final String filename;

    public FileSudokuBoardDao(String filename) {
        this.filename = filename;
    }

    @Override
    public void write(SudokuBoard object) throws DaoException {
        try {
            if (writer != null) {
                writer.close();
                throw new DaoException("errorWriter");
            }
        } catch (IOException e) {
            throw new DaoException("errorWriter", e);
        }

        File file = new File(filename);

        try (FileWriter writer = new FileWriter(file)) {
            this.writer = writer;
            for (int i = 0; i < SudokuBoard.SIZE; i++) {
                for (int j = 0; j < SudokuBoard.SIZE; j++) {
                    this.writer.write(object.get(i, j) + " ");
                }
                this.writer.write("\n");
            }

        } catch (IOException e) {
            throw new DaoException("errorWriting", e);
        }
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try {
            if (reader != null) {
                reader.close();
                throw new DaoException("errorWriter");
            }
        } catch (IOException e) {
            throw new DaoException("errorReader", e);
        }
        String currentDir = System.getProperty("user.dir");
        Path relativePath;
        if (System.getProperty("isTest") != null) {
            relativePath = Paths.get(currentDir, "Saves");
        } else {
            relativePath = Paths.get(currentDir, "lab", "Sudoku", "Model", "Saves");
        }

        File directory = relativePath.toFile();
        File file = new File(directory, filename);


        SudokuBoard obj = new SudokuBoard(new BacktrackingSudokuSolver());
        try {
            reader = new FileReader(file); // Initialize the reader field
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            for (int i = 0; i < SudokuBoard.SIZE; i++) {
                line = bufferedReader.readLine();
                if (line != null) {
                    String[] row = line.split(" ");
                    if (row.length != SudokuBoard.SIZE) {
                        throw new DaoException("errorFormat");
                    }
                    for (int j = 0; j < SudokuBoard.SIZE; j++) {
                        obj.set(i, j, Integer.parseInt(row[j]));
                    }
                } else {
                    throw new DaoException("errorLines");
                }
            }
        } catch (IOException e) {
            throw new DaoException("errorReading", e);
        }
        return obj;
    }

    @Override
    public List<String> names() throws IOException {
            Stream<Path> paths = Files.list(Paths.get("Saves").toAbsolutePath());
            return paths.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(".txt"))
                    .collect(Collectors.toList());
    }

    @Override
    public void close() throws ReaderOrWriterException {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new ReaderOrWriterException("errorWriter", e);
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new ReaderOrWriterException("errorReader", e);
        }
    }
}
