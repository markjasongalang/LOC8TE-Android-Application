package com.fourbytes.loc8teapp.connectedclientsrecycler;

import android.graphics.Bitmap;

public class ClientItem {
    private Bitmap clientImage;

    private String firstName;
    private String middleName;
    private String lastName;

    public ClientItem(Bitmap clientImage, String firstName, String middleName, String lastName) {
        this.clientImage = clientImage;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public Bitmap getClientImage() {
        return clientImage;
    }

    public void setClientImage(Bitmap clientImage) {
        this.clientImage = clientImage;
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
