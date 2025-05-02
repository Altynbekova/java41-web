package com.altynbekova.top.service;

import com.altynbekova.top.dao.CarDAO;
import com.altynbekova.top.entity.Car;
import com.altynbekova.top.exception.DaoException;
import com.altynbekova.top.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CarService {
    private static final Logger LOG = LoggerFactory.getLogger(CarService.class);
    private CarDAO carDAO = new CarDAO();

    public Car find(Long id) throws ServiceException {
        try {
            return carDAO.find(id);
        } catch (DaoException e) {
            LOG.error("Cannot find car by id={}. {}", id, e.getMessage());
            throw new ServiceException(e);
        }
    }

    public List<Car> findAll() throws ServiceException{
        try {
            return carDAO.findAll();
        } catch (DaoException e) {
            LOG.error("Cannot find cars. {}", e.getMessage());
            throw new ServiceException(e);
        }
    }

    public List<String> manufacturers() throws ServiceException{
        try {
            return carDAO.findManufacturers();
        } catch (DaoException e) {
            LOG.error("Cannot find cars manufacturers. {}", e.getMessage());
            throw new ServiceException(e);
        }
    }

    public Map<String, Integer> manufacturersCars() throws ServiceException{
        try {
            return carDAO.manufacturersCars();
        } catch (DaoException e) {
            LOG.error("Cannot define cars amount for every manufacturer. {}", e.getMessage());
            throw new ServiceException(e);
        }
    }

    public List<String> manufacturers(boolean top) throws ServiceException{
        try {
            return carDAO.manufacturer(top);
        } catch (DaoException e) {
            LOG.error("Cannot find manufacturers of {} cars amount. {}",
                    top ? "highest" : "lowest", e.getMessage());
            throw new ServiceException(e);
        }
    }

    public List<Car> find(int year) throws ServiceException {
        try {
            return carDAO.find(year);
        } catch (DaoException e) {
            LOG.error("Cannot find cars manufactured in {}. {}", year, e.getMessage());
            throw new ServiceException(e);
        }
    }
    public List<Car> find(int from, int to) throws ServiceException{
        try {
            return carDAO.find(from, to);
        } catch (DaoException e) {
            LOG.error("Cannot find cars manufactured in period {}-{}. {}", from, to, e.getMessage());
            throw new ServiceException(e);
        }
    }

    public void save(Car car) throws ServiceException{
        try {
            carDAO.save(car);
        } catch (DaoException e) {
            LOG.error("Cannot save {}. {}", car, e.getMessage());
            throw new ServiceException(e);
        }
    }

    public Car update(Car car) throws ServiceException{
        try {
            return carDAO.update(car);
        } catch (DaoException e) {
            LOG.error("Cannot update {}. {}", car, e.getMessage());
            throw new ServiceException(e);
        }
    }

    public void delete(int id) throws ServiceException{
        try {
            carDAO.delete(id);
        } catch (DaoException e) {
            LOG.error("Cannot delete car by id={}. {}", id, e.getMessage());
            throw new ServiceException(e);
        }
    }
}
