package com.java.koffy.database;

import com.java.koffy.container.Container;
import com.java.koffy.database.drivers.DatabaseConnection;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao<T> {

    private Connection conn = Container.resolve(DatabaseConnection.class).getConn();

    private Class<T> entity;

    private PreparedStatement statement;

    private ResultSet resultSet;

    public Dao(Class<T> entity) {
        this.entity = entity;
    }

    public void save(Object object) throws SQLException {
        try {
            String sql = generateInsertStatement(object);
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        } finally {
            closeResources(statement, null);
        }
    }

    public T getById(int id) throws SQLException {
        try {
            String sql = generateSelectStatement(id);
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createObjectFromResult(resultSet);
            }
        } finally {
            closeResources(statement, resultSet);
        }
        return null;
    }

    public List<T> getAll() throws SQLException {
        try {
            String sql = generateSelectAllStatement();
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            List<T> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(createObjectFromResult(resultSet));
            }
            return objects;
        } finally {
            closeResources(statement, resultSet);
        }
    }

    public void delete(Object object) throws SQLException {
        try {
            String sql = generateDeleteStatement(object);
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        } finally {
            closeResources(statement, null);
        }
    }

    private String getTableName() {
        if (entity.getAnnotation(Table.class) != null) {
            return entity.getAnnotation(Table.class).name();
        }
        return entity.getSimpleName().toLowerCase();
    }

    private String generateDeleteStatement(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(getTableName()).append(" WHERE id = ").append(getIdValue(object));
        return sql.toString();
    }

    private Object getIdValue(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException();
    }

    private String generateInsertStatement(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(getTableName()).append(" (");

        Field[] fields = clazz.getDeclaredFields();
        List<String> columnNames = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            columnNames.add(field.getName());
            try {
                values.add(String.valueOf(field.get(object)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < columnNames.size(); i++) {
            sql.append(columnNames.get(i));
            if (i < columnNames.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {
            sql.append("'").append(values.get(i)).append("'");
            if (i < values.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");

        return sql.toString();
    }

    private String generateSelectStatement(int id) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(getTableName());
        sql.append(" WHERE id = ").append(id);
        return sql.toString();
    }

    private String generateSelectAllStatement() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(getTableName());
        return sql.toString();
    }

    private T createObjectFromResult(ResultSet resultSet) {
        try {
            T object = entity.getDeclaredConstructor().newInstance();

            // Retrieve the column names from the result set
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            for (Field field : entity.getDeclaredFields()) {
                field.setAccessible(true);
                String columnName = field.getName(); // Assume column name matches field name
                if (columnNames.contains(columnName)) {
                    field.set(object, resultSet.getObject(columnName));
                }
            }
            return object;
        } catch (InvocationTargetException | NoSuchMethodException | SQLException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeResources(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
//            if (conn != null) {
//                conn.close();
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
