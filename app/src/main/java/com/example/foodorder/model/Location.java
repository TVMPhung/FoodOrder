package com.example.foodorder.model;

public class Location {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String hours;

    public Location(int id, String name, String address, String phone, String hours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
