package com.altynbekova.top.dao;

import com.altynbekova.top.entity.AbstractEntity;
import com.altynbekova.top.exception.DaoException;

import java.sql.Connection;

public abstract class DAO<T extends AbstractEntity> {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract T find(Long id) throws DaoException;

    public abstract void save(T entity) throws DaoException;

    public abstract T update(T entity) throws DaoException;

    public abstract void delete(int id) throws DaoException;
}
