package com.java.koffy.database;

import com.java.koffy.container.Container;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLQueryHelpers {

    private Class<?> entity;

    public void setEntity(Class<?> entity) {
        this.entity = entity;
    }

    public String getTableName() {
        if (entity.getAnnotation(Table.class) != null) {
            return entity.getAnnotation(Table.class).name();
        }
        return entity.getSimpleName().toLowerCase();
    }

    public PreparedStatement prepareInsertStatement(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
        List<String> fieldValues = new ArrayList<>();
        StringBuilder auxBuilder = new StringBuilder();
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(getTableName()).append(" (");

        for (Field field : fields) {
            field.setAccessible(true);
            query.append(field.getName());
            auxBuilder.append("?");
            try {
                fieldValues.add(String.valueOf(field.get(object)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (field != fields[fields.length - 1]) {
                query.append(", ");
                auxBuilder.append(", ");
            }
        }
        query.append(") VALUES (").append(auxBuilder).append(")");
        return constructStatement(query.toString(), fieldValues);
    }

    public PreparedStatement prepareSelectStatement(Object id) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(getTableName());
        query.append(" WHERE id = ?");
        return constructStatement(query.toString(), new ArrayList<>() {{ add(String.valueOf(id)); }});
    }

    public PreparedStatement prepareSelectAllStatement() throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(getTableName());
        return constructStatement(query.toString(), new ArrayList<>());
    }

    public PreparedStatement prepareDeleteStatement(Object object) throws SQLException {
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(getTableName()).append(" WHERE id = ?");
        return constructStatement(query.toString(), new ArrayList<>() {{ add(String.valueOf(getIdValue(object))); }});
    }

    public PreparedStatement constructStatement(String query, List<String> params) throws SQLException {
        Connection conn = Container.resolve(DatabaseConnection.class).getConn();
        PreparedStatement statement = conn.prepareStatement(query);
        if (!params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
        }
        return statement;
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
}
