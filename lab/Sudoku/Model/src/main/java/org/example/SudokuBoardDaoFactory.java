package org.example;

public class SudokuBoardDaoFactory {
    private SudokuBoard prototype;

    public SudokuBoardDaoFactory() {
    }

    public SudokuBoardDaoFactory(SudokuBoard prototype) {
        this.prototype = prototype;
    }

    public Dao<SudokuBoard> getFileDao(String filename) throws DaoException {
        if (filename == null) {
            throw new DaoException("filenameCannotBeNull");
        }
        return new FileSudokuBoardDao(filename);
    }

    public JdbcSudokuBoardDao getDatabaseDao(String filename) throws DaoException {
        if (filename == null) {
            throw new DaoException("filenameCannotBeNull");
        }
        return new JdbcSudokuBoardDao(filename);
    }

    public SudokuBoard createInstance() throws DaoException {
        try {
            return (SudokuBoard) prototype.clone();
        } catch (CloneException e) {
            throw new DaoException("cloningNotSupported", e);
        }
    }
}
