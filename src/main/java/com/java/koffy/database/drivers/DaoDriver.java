package com.java.koffy.database.drivers;

import java.util.List;

public interface DaoDriver<T> {

    void save(T object);

    T getById(T id);

    List<T> getAll();

    void delete(T object);
}
