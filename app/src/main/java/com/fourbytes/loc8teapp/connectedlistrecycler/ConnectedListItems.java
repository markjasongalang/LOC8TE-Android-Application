package com.fourbytes.loc8teapp.connectedlistrecycler;

import android.graphics.Bitmap;

public class ConnectedListItems {
    private String connectedlist_name;
    private String connectedlist_occupation;
    private String connectedlist_field;
    private Bitmap connectedlist_image;

    public ConnectedListItems(String connectedlist_name, String connectedlist_occupation, String connectedlist_field, Bitmap connectedlist_image) {
        this.connectedlist_name = connectedlist_name;
        this.connectedlist_occupation = connectedlist_occupation;
        this.connectedlist_field = connectedlist_field;
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

    public String getConnectedlist_field() {
        return connectedlist_field;
    }

    public void setConnectedlist_field(String connectedlist_field) {
        this.connectedlist_field = connectedlist_field;
    }

    public Bitmap getConnectedlist_image() {
        return connectedlist_image;
    }

    public void setConnectedlist_image(Bitmap connectedlist_image) {
        this.connectedlist_image = connectedlist_image;
    }
}
