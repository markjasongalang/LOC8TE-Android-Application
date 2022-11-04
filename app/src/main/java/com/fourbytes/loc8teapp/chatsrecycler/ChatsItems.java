package com.fourbytes.loc8teapp.chatsrecycler;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

public class ChatsItems {
    private String chat_username;
    private String chat_name;
    private String chat_occupation;
    private String chat_msgpreview;

    private StorageReference pathReference;

    public ChatsItems(String chat_username, String chat_name, String chat_occupation, String chat_msgpreview, StorageReference pathReference) {
        this.chat_username = chat_username;
        this.chat_name = chat_name;
        this.chat_occupation = chat_occupation;
        this.chat_msgpreview = chat_msgpreview;
        this.pathReference = pathReference;
    }

    public String getChat_username() {
        return chat_username;
    }

    public void setChat_username(String chat_username) {
        this.chat_username = chat_username;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getChat_occupation() {
        return chat_occupation;
    }

    public void setChat_occupation(String chat_occupation) {
        this.chat_occupation = chat_occupation;
    }

    public String getChat_msgpreview() {
        return chat_msgpreview;
    }

    public void setChat_msgpreview(String chat_msgpreview) {
        this.chat_msgpreview = chat_msgpreview;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }
}
