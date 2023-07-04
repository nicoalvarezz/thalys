package com.java.koffy.database;

import com.java.koffy.database.drivers.DaoDriver;
import com.java.koffy.exception.DatabaseException;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLDao<T> implements DaoDriver<T> {

    private Class<T> entity;

    private final SQLQueryHelpers queryHelpers = new SQLQueryHelpers();

    public SQLDao(Class<T> entity) {
        if (entity.getAnnotation(Entity.class) != null) {
            this.entity = entity;
            queryHelpers.setEntity(entity);
        } else {
           throw new RuntimeException("Not a valid entity: Missing entity annotation");
        }
    }

    @Override
    public void save(Object object) {
        try {
            queryHelpers.prepareInsertStatement(object).executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during insertion", e);
        }
    }

    @Override
    public T getById(Object id) {
        try {
            ResultSet resultSet = queryHelpers.prepareSelectStatement(id).executeQuery();
            if (resultSet.next()) {
                return createObjectFromResult(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during selection", e);
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        try {
            ResultSet resultSet = queryHelpers.prepareSelectAllStatement().executeQuery();
            List<T> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(createObjectFromResult(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during selection", e);
        }
    }

    @Override
    public void delete(Object object) {
        try {
            queryHelpers.prepareDeleteStatement(object).executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during delition", e);
        }
    }

    private T createObjectFromResult(ResultSet resultSet) {
        try {
            T object = entity.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            for (Field field : entity.getDeclaredFields()) {
                field.setAccessible(true);
                String columnName = field.getName();
                if (columnNames.contains(columnName)) {
                    field.set(object, resultSet.getObject(columnName));
                }
            }
            return object;
        } catch (ReflectiveOperationException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}