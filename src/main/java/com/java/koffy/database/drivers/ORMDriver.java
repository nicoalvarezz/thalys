package com.java.koffy.database.drivers;

import com.java.koffy.container.Container;

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

public class ORMDriver {

    private Connection conn = Container.resolve(DatabaseConnection.class).getConn();
    private PreparedStatement statement;
    private ResultSet resultSet;

    public void save(Object object) throws SQLException {
        try {
            String sql = generateInsertStatement(object);
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        } finally {
            closeResources(statement, null);
        }
    }

    public Object getById(Class<?> clazz, int id) throws SQLException {
        try {
            String sql = generateSelectStatement(clazz, id);
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createObjectFromResult(clazz, resultSet);
            }
        } finally {
            closeResources(statement, resultSet);
        }
        return null;
    }

    public List<Object> getAll(Class<?> clazz) throws SQLException {
        try {
            String sql = generateSelectAllStatement(clazz);
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            List<Object> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(createObjectFromResult(clazz, resultSet));
            }
            return objects;
        } finally {
            closeResources(statement, resultSet);
        }
    }


    private String generateInsertStatement(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(getTableName(clazz)).append(" (");

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

    private String getTableName(Class<?> clazz) {
        if (clazz.getAnnotation(Table.class) != null) {
            return clazz.getAnnotation(Table.class).name();
        }
        return clazz.getSimpleName().toLowerCase();
    }

    private String generateSelectStatement(Class<?> clazz, int id) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(getTableName(clazz));
        sql.append(" WHERE id = ").append(id);
        return sql.toString();
    }

    private String generateSelectAllStatement(Class<?> clazz) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(getTableName(clazz));
        return sql.toString();
    }

    private Object createObjectFromResult(Class<?> clazz, ResultSet resultSet) {
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();

            // Retrieve the column names from the result set
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            for (Field field : clazz.getDeclaredFields()) {
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
