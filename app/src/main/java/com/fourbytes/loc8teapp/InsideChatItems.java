package com.fourbytes.loc8teapp;

public class InsideChatItems {

    public static final int layout_left = 1;
    public static final int layout_right = 2;

    String inside_chat_message;
    int inside_chat_image;
    int ViewType;

    public InsideChatItems(String inside_chat_message, int inside_chat_image, int viewType) {
        this.inside_chat_message = inside_chat_message;
        this.inside_chat_image = inside_chat_image;
        ViewType = viewType;
    }

    public String getInside_chat_message() {
        return inside_chat_message;
    }

    public int getInside_chat_image() {
        return inside_chat_image;
    }

    public int getViewType() {
        return ViewType;
    }
}


