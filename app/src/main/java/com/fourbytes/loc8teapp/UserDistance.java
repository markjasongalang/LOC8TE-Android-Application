package com.fourbytes.loc8teapp;

import org.json.JSONArray;

public class UserDistance {

    private JSONArray routes;
    private double latitude;
    private double longitude;
    private double distance;

    public UserDistance(JSONArray routes, double distance) {
        this.routes = routes;
        this.distance = distance;
    }

    public JSONArray getRoutes() {
        return routes;
    }

    public void setRoutes(JSONArray routes) {
        this.routes = routes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
