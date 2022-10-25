package com.fourbytes.loc8teapp;

public class UserInfo {

    private String id;
    private double longitude;
    private double latitude;
    private double distance;
    public UserInfo(String id, double longitude, double latitude){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = 0;
    }

    public String getId() {
        return id;
    }

    public UserInfo clone(String id, double longitude, double latitude){

        UserInfo userClone = new UserInfo(id, longitude, latitude);
        userClone.id = id;
        userClone.longitude = longitude;
        userClone.latitude = latitude;
        userClone.distance = 0;

        return userClone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
