package com.altynbekova.top;

import com.altynbekova.top.service.CarService;

public class Main {
    public static void main(String[] args) {
        System.out.println(new CarService().find(1L));

    }
}