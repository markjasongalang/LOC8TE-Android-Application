package com.fourbytes.loc8teapp;

public class ConnectedListItems {
    String connectedlist_name;
    String connectedlist_occupation;
    String connectedlist_distance;
    int connectedlist_image;

    public ConnectedListItems(String connectedlist_name, String connectedlist_occupation, String connectedlist_distance, int connectedlist_image) {
        this.connectedlist_name = connectedlist_name;
        this.connectedlist_occupation = connectedlist_occupation;
        this.connectedlist_distance = connectedlist_distance;
        this.connectedlist_image = connectedlist_image;
    }

    public String getConnectedlist_name() {
        return connectedlist_name;
    }

    public void setConnectedlist_name(String connectedlist_name) {
        this.connectedlist_name = connectedlist_name;
    }

    public String getConnectedlist_occupation() {
        return connectedlist_occupation;
    }

    public void setConnectedlist_occupation(String connectedlist_occupation) {
        this.connectedlist_occupation = connectedlist_occupation;
    }

    public String getConnectedlist_distance() {
        return connectedlist_distance;
    }

    public void setConnectedlist_distance(String connectedlist_distance) {
        this.connectedlist_distance = connectedlist_distance;
    }

    public int getConnectedlist_image() {
        return connectedlist_image;
    }

    public void setConnectedlist_image(int connectedlist_image) {
        this.connectedlist_image = connectedlist_image;
    }
}
