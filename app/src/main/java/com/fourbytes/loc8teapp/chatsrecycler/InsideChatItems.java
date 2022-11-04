package com.fourbytes.loc8teapp.chatsrecycler;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

public class InsideChatItems {

    public static final int layout_left = 1;
    public static final int layout_right = 2;

    private String inside_chat_message;
    private String inside_chat_timestamp;

    private StorageReference pathReference;

    private int ViewType;

    public InsideChatItems(String inside_chat_message, String inside_chat_timestamp, StorageReference pathReference, int viewType) {
        this.inside_chat_message = inside_chat_message;
        this.inside_chat_timestamp = inside_chat_timestamp;
        this.pathReference = pathReference;
        ViewType = viewType;
    }

    public String getInside_chat_message() {
        return inside_chat_message;
    }

    public void setInside_chat_message(String inside_chat_message) {
        this.inside_chat_message = inside_chat_message;
    }

    public String getInside_chat_timestamp() {
        return inside_chat_timestamp;
    }

    public void setInside_chat_timestamp(String inside_chat_timestamp) {
        this.inside_chat_timestamp = inside_chat_timestamp;
    }

    public StorageReference getPathReference() {
        return pathReference;
    }

    public void setPathReference(StorageReference pathReference) {
        this.pathReference = pathReference;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}


