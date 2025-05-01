package com.altynbekova.top;

import com.altynbekova.top.dao.DAO;
import com.altynbekova.top.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";
    private Connection connection;

    public <T extends AbstractEntity> void init(DAO<T> dao) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        dao.setConnection(connection);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection = null;
            }
        }
    }
}
