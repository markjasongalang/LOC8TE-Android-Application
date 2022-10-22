package com.fourbytes.loc8teapp;

public class VertexInfo {

    private String id;
    private double longitude;
    private double latitude;
    private double distance;

    public VertexInfo(String id, double longitude, double latitude){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = 0;
    }

    public String getId() {
        return id;
    }

    public VertexInfo clone(String id, double longitude, double latitude){

        VertexInfo vertexClone = new VertexInfo(id, longitude, latitude);
        vertexClone.id = id;
        vertexClone.longitude = longitude;
        vertexClone.latitude = latitude;
        vertexClone.distance = 0;

        return vertexClone;
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
