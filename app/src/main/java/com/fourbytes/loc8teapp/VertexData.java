package com.fourbytes.loc8teapp;

import java.util.HashMap;

public class VertexData {
    double latitude;
    double longitude;
    HashMap<String, PathVertex> path;

    public VertexData(double latitude, double longitude, HashMap<String, PathVertex> path) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.path = path;
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

    public HashMap<String, PathVertex> getPath() {
        return path;
    }

    public void setPath(HashMap<String, PathVertex> path) {
        this.path = path;
    }
}
