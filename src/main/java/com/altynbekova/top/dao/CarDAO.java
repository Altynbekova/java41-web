package com.altynbekova.top.dao;

import com.altynbekova.top.exception.DaoException;
import com.altynbekova.top.exception.TransactionException;
import com.altynbekova.top.util.TransactionManager;
import com.altynbekova.top.entity.Car;
import com.altynbekova.top.entity.CarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarDAO extends DAO<Car> {
    private static final Logger LOG = LoggerFactory.getLogger(CarDAO.class);
    private static final String SELECT_BY_ID = "select * from cars where id=?";
    private static final String SELECT_ALL = "select * from cars";
    private static final String SELECT_MANUFACTURERS = "select manufacturer from cars";
    private static final String COUNT_CARS_BY_MANUFACTURERS = "select manufacturer, count(*) from cars group by manufacturer";
    private static final String TOP_OR_LOW_MANUFACTURERS = "select manufacturer from cars group by manufacturer " +
                                                           "ORDER  BY count(*) %s FETCH FIRST 1 ROWS WITH TIES";
    private static final String SELECT_BY_YEAR = "select * from cars where year=?";
    private static final String SELECT_IN_PERIOD = "select * from cars where year>=? and year<=?";
    private static final String INSERT = "insert into cars (name, manufacturer, volume, year, colour, type) " +
                                         "values (?, ?, ?, ?, ?, ?::car_type);";
    private static final String DELETE_BY_ID = "delete from cars where id=?";
    private static final String UPDATE_BY_ID = "update cars set " +
                                               "name=?, manufacturer = ?, volume=?, year=?, colour=?, type=?::car_type " +
                                               "where id=?;";
    private TransactionManager txManager = new TransactionManager();

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

    @Override
    public Car find(Long id) throws DaoException {
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
                LOG.error("Cannot create statement to find car by id={}", id, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find car by id={}.", id, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }

        return car;
    }

    public List<Car> findAll() throws DaoException {
        List<Car> cars = new ArrayList<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(SELECT_ALL);
                while (resultSet.next()) {
                    cars.add(convert(resultSet));
                }
            } catch (SQLException e) {
                LOG.error("Cannot create statement to find cars.", e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find cars.", e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    public List<String> findManufacturers() throws DaoException {
        List<String> manufacturers = new ArrayList<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(SELECT_MANUFACTURERS);
                while (resultSet.next()) {
                    manufacturers.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                LOG.error("Cannot create statement to find cars manufacturers.", e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find cars manufacturers.", e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }
        return manufacturers;
    }

    public Map<String, Integer> manufacturersCars() throws DaoException {
        Map<String, Integer> manufacturersCars = new HashMap<>();
        try {
            txManager.init(this);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(COUNT_CARS_BY_MANUFACTURERS);
                while (resultSet.next()) {
                    manufacturersCars.put(resultSet.getString(1), resultSet.getInt(2));
                }
            } catch (SQLException e) {
                LOG.error("Cannot create statement to define cars amount for every manufacturer.", e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot define cars amount for every manufacturer.", e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }
        return manufacturersCars;
    }

    public List<String> manufacturer(boolean top) throws DaoException {
        List<String> manufacturers = new ArrayList<>();
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(
                    String.format(TOP_OR_LOW_MANUFACTURERS, top ? "desc" : "asc"))) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    manufacturers.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                LOG.error("Cannot create statement to find manufacturers of {} cars amount.",
                        top ? "highest" : "lowest", e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find manufacturers of {} cars amount.",
                    top ? "highest" : "lowest", e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }
        return manufacturers;
    }

    public List<Car> find(int year) throws DaoException {
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
                LOG.error("Cannot create statement to find cars manufactured in {}.", year, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find cars manufactured in {}.", year, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    public List<Car> find(int from, int to) throws DaoException {
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
                LOG.error("Cannot create statement to find cars manufactured in period {}-{}.", from, to, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot find cars manufactured in period {}-{}.", from, to, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }

        return cars;
    }

    @Override
    public void save(Car car) throws DaoException {
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
                statement.setString(1, car.getName());
                statement.setString(2, car.getManufacturer());
                statement.setDouble(3, car.getVolume());
                statement.setInt(4, car.getYear());
                statement.setString(5, car.getColour());
                statement.setString(6, car.getType().name());
                statement.execute();
            } catch (SQLException e) {
                LOG.error("Cannot create statement to save {}.", car, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot save {}.", car, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }
    }

    @Override
    public Car update(Car car) throws DaoException {
        try {
            txManager.init(this);
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID)) {
                statement.setString(1, car.getName());
                statement.setString(2, car.getManufacturer());
                statement.setDouble(3, car.getVolume());
                statement.setInt(4, car.getYear());
                statement.setString(5, car.getColour());
                statement.setString(6, car.getType().name());
                statement.setLong(7, car.getId());
                statement.execute();
            } catch (SQLException e) {
                LOG.error("Cannot create statement to update {}.", car, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot update {}.", car, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }

        return find(car.getId());
    }

    @Override
    public void delete(int id) throws DaoException {
        try {
            txManager.init(this);

            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
                statement.setInt(1, id);
                boolean updated = statement.execute();
                if (!updated) {
                    System.out.println(statement.getUpdateCount());
                }
            } catch (SQLException e) {
                LOG.error("Cannot create statement to delete car by id={}.", id, e);
                throw new DaoException(e);
            }
        } catch (TransactionException e) {
            LOG.error("Cannot delete car by id={}.", id, e);
            throw new DaoException(e);
        } finally {
            txManager.end();
        }
    }
}
