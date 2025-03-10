package org.example;

import java.io.IOException;
import java.util.List;

public interface Dao<T> extends AutoCloseable {

    T read() throws DaoException;

    void write(T object) throws DaoException;

    List<String> names() throws IOException;

    @Override
    void close() throws Exception;
}
