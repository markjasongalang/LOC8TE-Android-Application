package com.fourbytes.loc8teapp.chatsrecycler;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

public class ChatsItems {
    private String chatUsername;

    private StorageReference pathReference;

    public ChatsItems(String chatUsername, StorageReference pathReference) {
        this.chatUsername = chatUsername;
        this.pathReference = pathReference;
    }

    public String getChatUsername() {
        return chatUsername;
    }

    public void setChatUsername(String chatUsername) {
        this.chatUsername = chatUsername;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }
}
