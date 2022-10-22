package com.fourbytes.loc8teapp;

import java.util.ArrayList;

public class Vertex {
    private String id;
    private double longitude;
    private double latitude;

    private ArrayList<VertexInfo> edge = new ArrayList<>();

    public Vertex(String id, double longitude, double latitude, ArrayList<VertexInfo> edge){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.edge = edge;
    }

    public String getId() {
        return id;
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

    public ArrayList<VertexInfo> getEdge() {
        return edge;
    }

    public void setEdge(ArrayList<VertexInfo> edge) {
        this.edge = edge;
    }
}
