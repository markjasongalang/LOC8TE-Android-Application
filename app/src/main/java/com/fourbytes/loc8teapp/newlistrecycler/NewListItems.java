package com.fourbytes.loc8teapp.newlistrecycler;

import android.graphics.Bitmap;

public class NewListItems {
    private String username;
    private String newlist_name;
    private String newlist_occupation;
    private String newlist_field;

    private Bitmap newlist_image;

    public NewListItems(String username, String newlist_name, String newlist_occupation, String newlist_field, Bitmap newlist_image) {
        this.username = username;
        this.newlist_name = newlist_name;
        this.newlist_occupation = newlist_occupation;
        this.newlist_field = newlist_field;
        this.newlist_image = newlist_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getNewlist_field() {
        return newlist_field;
    }

    public void setNewlist_field(String newlist_field) {
        this.newlist_field = newlist_field;
    }

    public Bitmap getNewlist_image() {
        return newlist_image;
    }

    public void setNewlist_image(Bitmap newlist_image) {
        this.newlist_image = newlist_image;
    }
}
