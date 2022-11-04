package com.fourbytes.loc8teapp.chatsrecycler;

import android.graphics.Bitmap;

public class ChatsItems {
    private String chat_name;
    private String chat_occupation;
    private String chat_msgpreview;
    private Bitmap chat_image;

    public ChatsItems(String chat_name, String chat_occupation, String chat_msgpreview, Bitmap chat_image) {
        this.chat_name = chat_name;
        this.chat_occupation = chat_occupation;
        this.chat_msgpreview = chat_msgpreview;
        this.chat_image = chat_image;
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

    public Bitmap getChat_image() {
        return chat_image;
    }

    public void setChat_image(Bitmap chat_image) {
        this.chat_image = chat_image;
    }
}
