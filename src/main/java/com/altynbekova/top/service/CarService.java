package com.altynbekova.top.service;

import com.altynbekova.top.dao.CarDAO;
import com.altynbekova.top.entity.Car;

public class CarService {
    private CarDAO carDAO = new CarDAO();

    public Car find(Long id) {
        return carDAO.find(id);
    }
}
