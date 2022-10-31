package com.fourbytes.loc8teapp;

public class UserInfo {

    private String id;
    private String meet_point;
    private String name;

    private double longitude;
    private double latitude;
    private double distance;

    public UserInfo(String id, double longitude, double latitude){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = 0;
    }

    public UserInfo(String id, double longitude, double latitude, String meet_point, String name){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.meet_point = meet_point;
        this.name = name;
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

    public String getMeet_point() {
        return meet_point;
    }

    public void setMeet_point(String meet_point) {
        this.meet_point = meet_point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
