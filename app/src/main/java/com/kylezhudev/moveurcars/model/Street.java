package com.kylezhudev.moveurcars.model;



public class Street {
    String streetName;
    String sideOfStreet;

    public Street(String streetName, String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
        this.streetName = streetName;
    }

    public String getSideOfStreet() {
        return sideOfStreet;
    }

    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
