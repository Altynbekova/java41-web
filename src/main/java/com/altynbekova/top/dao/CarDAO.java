package com.altynbekova.top.dao;

import com.altynbekova.top.TransactionManager;
import com.altynbekova.top.entity.Car;
import com.altynbekova.top.entity.CarType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarDAO extends DAO<Car> {
    private static final String SELECT_BY_ID = "select * from cars where id=?";
    private static final String SELECT_ALL = "select * from cars";
    private static final String SELECT_MANUFACTURERS = "select manufacturer from cars";
    private static final String COUNT_CARS_BY_MANUFACTURERS = "select manufacturer, count(*) from cars group by manufacturer";
    private static final String TOP_OR_LOW_MANUFACTURERS = "select manufacturer from cars group by manufacturer " +
                                                           "ORDER  BY count(*) %s FETCH FIRST 1 ROWS WITH TIES";
    private static final String SELECT_BY_YEAR = "select * from cars where year=?";
    private static final String SELECT_IN_PERIOD = "select * from cars where year>=? and year<=?";
    private TransactionManager txManager = new TransactionManager();

    @Override
    public Car find(Long id) {
        Car car = new Car();
        try {
            txManager.init(this);
            try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return convert(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }

        return car;
    }

    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(SELECT_ALL);
                while (resultSet.next()) {
                    cars.add(convert(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    public List<String> findManufacturers() {
        List<String> manufacturers = new ArrayList<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(SELECT_MANUFACTURERS);
                while (resultSet.next()) {
                    manufacturers.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }
        return manufacturers;
    }

    public Map<String, Integer> manufacturersCars() {
        Map<String, Integer> manufacturersCars = new HashMap<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(COUNT_CARS_BY_MANUFACTURERS);
                while (resultSet.next()) {
                    manufacturersCars.put(resultSet.getString(1), resultSet.getInt(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }
        return manufacturersCars;
    }

    public List<String> manufacturer(boolean topCarsAmount) {
        List<String> manufacturers = new ArrayList<>();
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(
                    String.format(TOP_OR_LOW_MANUFACTURERS, topCarsAmount ? "desc" : "asc"))) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    manufacturers.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }
        return manufacturers;
    }

    public List<Car> find(int year) {
        List<Car> cars = new ArrayList<>();
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_YEAR)) {
                statement.setInt(1, year);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    cars.add(convert(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    public List<Car> find(int from, int to) {
        List<Car> cars = new ArrayList<>();
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(SELECT_IN_PERIOD)) {
                statement.setInt(1, from);
                statement.setInt(2, to);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    cars.add(convert(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    private static Car convert(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getLong("id"));
        car.setName(resultSet.getString("name"));
        car.setManufacturer(resultSet.getString("manufacturer"));
        car.setYear(resultSet.getInt("year"));
        car.setVolume(resultSet.getDouble("volume"));
        car.setColour(resultSet.getString("colour"));
        car.setType(CarType.valueOf(resultSet.getString("type")));
        return car;
    }
}
