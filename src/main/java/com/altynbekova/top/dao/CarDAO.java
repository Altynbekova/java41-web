package com.altynbekova.top.dao;

import com.altynbekova.top.TransactionManager;
import com.altynbekova.top.entity.Car;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarDAO extends DAO<Car> {
    private static final String SELECT_BY_ID = "select * from cars where id=?";
    private TransactionManager txManager = new TransactionManager();

    @Override
    public Car find(Long id) {
        Car car = new Car();
        try {
            txManager.init(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                car.setId(resultSet.getLong("id"));
                car.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        return new Car("Car #1", "Cars & Co.", 2.4, 2020, "red");
        return car;
    }
}
