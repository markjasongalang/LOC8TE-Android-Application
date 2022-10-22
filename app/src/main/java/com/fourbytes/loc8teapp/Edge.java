package com.fourbytes.loc8teapp;

public class Edge {

    private String originId;
    private String destinationId;
    private String edgeId;
    private double distance;

    public Edge(String originId, String destinationId, double distance, String edgeId){
        this.originId = originId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.edgeId = edgeId;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
