package com.fourbytes.loc8teapp.connectedclientsrecycler;

import android.graphics.Bitmap;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

public class ClientItem {
    private StorageReference pathReference;

    private String clientUsername;

    public ClientItem(StorageReference pathReference, String clientUsername) {
        this.pathReference = pathReference;
        this.clientUsername = clientUsername;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }
}
