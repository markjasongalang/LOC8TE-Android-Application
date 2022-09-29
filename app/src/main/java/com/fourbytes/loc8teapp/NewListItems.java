package com.fourbytes.loc8teapp;

public class NewListItems {
    String newlist_name;
    String newlist_occupation;
    String newlist_distance;
    int newlist_image;

    public NewListItems(String newlist_name, String newlist_occupation, String newlist_distance, int newlist_image) {
        this.newlist_name = newlist_name;
        this.newlist_occupation = newlist_occupation;
        this.newlist_distance = newlist_distance;
        this.newlist_image = newlist_image;
    }

    public String getNewlist_name() {
        return newlist_name;
    }

    public void setNewlist_name(String newlist_name) {
        this.newlist_name = newlist_name;
    }

    public String getNewlist_occupation() {
        return newlist_occupation;
    }

    public void setNewlist_occupation(String newlist_occupation) {
        this.newlist_occupation = newlist_occupation;
    }

    public String getNewlist_distance() {
        return newlist_distance;
    }

    public void setNewlist_distance(String newlist_distance) {
        this.newlist_distance = newlist_distance;
    }

    public int getNewlist_image() {
        return newlist_image;
    }

    public void setNewlist_image(int newlist_image) {
        this.newlist_image = newlist_image;
    }
}
