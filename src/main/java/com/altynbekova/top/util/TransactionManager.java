package com.altynbekova.top.util;

import com.altynbekova.top.dao.DAO;
import com.altynbekova.top.entity.AbstractEntity;
import com.altynbekova.top.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionManager {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin2";
    private Connection connection;

    public <T extends AbstractEntity> void init(DAO<T> dao) throws TransactionException{
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                LOG.error("Cannot connect to database.", e);
                throw new TransactionException(e);

            }
        }
        dao.setConnection(connection);
    }

    public void commit() throws TransactionException{
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public void end() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Cannot close connection.", e);
            } finally {
                connection = null;
            }
        }
    }
}
