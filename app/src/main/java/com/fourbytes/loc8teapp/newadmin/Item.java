package com.fourbytes.loc8teapp.newadmin;

import com.google.firebase.storage.StorageReference;

public class Item {
    private String username;
    private String fullName;

    private StorageReference pathReference;

    public Item(String username, String fullName, StorageReference pathReference) {
        this.username = username;
        this.fullName = fullName;
        this.pathReference = pathReference;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }}
