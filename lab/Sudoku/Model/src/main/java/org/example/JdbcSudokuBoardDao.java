package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {
    private static final String DB_NAME = "sudoku";
    private String filename;
    private static Connection connection;
    private static final Logger logger = Logger.getLogger(JdbcSudokuBoardDao.class.getName(),"Messages");


    static {
        String databaseUrl = "jdbc:postgresql://localhost:5432/" + DB_NAME;
        try {
            connection = DriverManager.getConnection(databaseUrl,"postgres","postgres");
        } catch (SQLException e) {
        logger.log(Level.WARNING,"errorConnection",e);
        }
    }

    public JdbcSudokuBoardDao(String filename) {
        if (filename == null) {
            throw new CustomNullPointerException("filenameCannotBeNull");
        }
        this.filename = filename;
    }

    @Override
    public SudokuBoard read() throws DatabaseException {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());

        try (PreparedStatement checkStatement =
                     connection.prepareStatement("SELECT filename FROM sudoku_table WHERE filename = ?")) {
            checkStatement.setString(1, this.filename);
            ResultSet checkResultSet = checkStatement.executeQuery();
            if (!checkResultSet.next()) {
                throw new DatabaseException("filenameDoesNotExist");
            }
        } catch (SQLException e) {
            throw new DatabaseException("errorConnection",e);
        }

        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT * FROM sudoku_table WHERE filename = ?")) {
            statement.setString(1, this.filename);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        sudoku.set(i, j, resultSet.getInt("cell_" + i + "_" + j));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("errorConnection",e);
        }

        return sudoku;
    }

    @Override
    public void write(SudokuBoard object) throws DatabaseException {
        if (object == null) {
            throw new CustomNullPointerException("sudokuBoardCannotBeNull");
        }
        try {
            // Check if the table exists
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "sudoku_table", null);
            if (!tables.next()) {
                // Table does not exist, create it
                StringBuilder createTableSql =
                        new StringBuilder("CREATE TABLE sudoku_table (filename VARCHAR(255) PRIMARY KEY,");
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        createTableSql.append(" cell_").append(i).append("_").append(j).append(" INT,");
                    }
                }
                createTableSql.setLength(createTableSql.length() - 1);  // Remove the last comma
                createTableSql.append(")");

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSql.toString());
                }
            }
            // Now you can insert the Sudoku board into the table
            StringBuilder insertSql = new StringBuilder("INSERT INTO sudoku_table (filename,");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    insertSql.append(" cell_").append(i).append("_").append(j).append(",");
                }
            }
            insertSql.setLength(insertSql.length() - 1);  // Remove the last comma
            insertSql.append(") VALUES (?,");
            for (int i = 0; i < 81; i++) {
                insertSql.append(" ?,");
            }
            insertSql.setLength(insertSql.length() - 1);  // Remove the last comma
            insertSql.append(")");

            try (PreparedStatement statement = connection.prepareStatement(insertSql.toString())) {
                connection.setAutoCommit(false);
                statement.setString(1, this.filename);
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        statement.setInt(2 + i * 9 + j, object.get(i, j));
                    }
                }
                statement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseException("errorConnection",e);
        }
    }

    @Override
    public List<String> names() throws DatabaseException {
        List<String> names = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT filename FROM sudoku_table");
            while (resultSet.next()) {
                names.add(resultSet.getString("filename"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("errorConnection", e);
        }
        return names;
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("errorConnection", e);
        }
    }
}