package com.altynbekova.top.entity;

public class Car extends AbstractEntity {
    private String name;
    private String manufacturer;
    private double volume;
    private int year;
    private String colour;
    private CarType type;

    public Car() {
    }

    public Car(String name, String manufacturer, double volume, int year, String colour) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.volume = volume;
        this.year = year;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Car{");
        sb.append("id='").append(getId()).append('\'');
        sb.append("name='").append(name).append('\'');
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", volume=").append(volume);
        sb.append(", year=").append(year);
        sb.append(", colour='").append(colour).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
