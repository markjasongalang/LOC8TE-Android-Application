package com.fourbytes.loc8teapp;

import java.util.ArrayList;

public class PathVertex {
    double distance;
    ArrayList<String> path;

    public PathVertex(double distance, ArrayList<String> path) {
        this.distance = distance;
        this.path = path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }
}
