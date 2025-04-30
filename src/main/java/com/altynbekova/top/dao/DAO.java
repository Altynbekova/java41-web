package com.altynbekova.top.dao;

import com.altynbekova.top.entity.AbstractEntity;

import java.sql.Connection;

public abstract class DAO<T extends AbstractEntity> {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract T find(Long id);
}
