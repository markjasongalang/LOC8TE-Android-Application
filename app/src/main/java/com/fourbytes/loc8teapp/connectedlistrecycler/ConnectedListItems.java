package com.fourbytes.loc8teapp.connectedlistrecycler;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

public class ConnectedListItems {
    private String connectedlist_username;
    private String connectedlist_name;
    private String connectedlist_occupation;
    private String connectedlist_field;

    private StorageReference pathReference;

    public ConnectedListItems(String connectedlist_username, String connectedlist_name, String connectedlist_occupation, String connectedlist_field, StorageReference pathReference) {
        this.connectedlist_username = connectedlist_username;
        this.connectedlist_name = connectedlist_name;
        this.connectedlist_occupation = connectedlist_occupation;
        this.connectedlist_field = connectedlist_field;
        this.pathReference = pathReference;
    }

    public String getConnectedlist_username() {
        return connectedlist_username;
    }

    public void setConnectedlist_username(String connectedlist_username) {
        this.connectedlist_username = connectedlist_username;
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

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }
}
