package com.altynbekova.top.service;

import com.altynbekova.top.dao.CarDAO;
import com.altynbekova.top.entity.Car;

import java.util.List;
import java.util.Map;

public class CarService {
    private CarDAO carDAO = new CarDAO();

    public Car find(Long id) {
        return carDAO.find(id);
    }

    public List<Car> findAll() {
        return carDAO.findAll();
    }

    public List<String> manufacturers(){
        return carDAO.findManufacturers();
    }

    public Map<String, Integer> manufacturersCars(){
        return carDAO.manufacturersCars();
    }
}
