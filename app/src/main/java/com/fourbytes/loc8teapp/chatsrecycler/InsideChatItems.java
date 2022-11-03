package com.fourbytes.loc8teapp.chatsrecycler;

import android.graphics.Bitmap;

public class InsideChatItems {

    public static final int layout_left = 1;
    public static final int layout_right = 2;

    private String inside_chat_message;
    private String inside_chat_timestamp;
    private Bitmap inside_chat_image;
    private int ViewType;

    public InsideChatItems(String inside_chat_message, String inside_chat_timestamp, Bitmap inside_chat_image, int viewType) {
        this.inside_chat_message = inside_chat_message;
        this.inside_chat_timestamp = inside_chat_timestamp;
        this.inside_chat_image = inside_chat_image;
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

    public Bitmap getInside_chat_image() {
        return inside_chat_image;
    }

    public void setInside_chat_image(Bitmap inside_chat_image) {
        this.inside_chat_image = inside_chat_image;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}


