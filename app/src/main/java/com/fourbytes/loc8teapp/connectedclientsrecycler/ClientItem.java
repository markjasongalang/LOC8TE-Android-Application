package com.fourbytes.loc8teapp.connectedclientsrecycler;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

public class ClientItem {
    private StorageReference pathReference;

    private String username;
    private String firstName;
    private String middleName;
    private String lastName;

    public ClientItem(StorageReference pathReference, String username, String firstName, String middleName, String lastName) {
        this.pathReference = pathReference;
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
