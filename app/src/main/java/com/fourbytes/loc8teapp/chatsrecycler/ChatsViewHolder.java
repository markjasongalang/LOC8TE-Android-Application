package com.fourbytes.loc8teapp.chatsrecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ChatsViewHolder extends RecyclerView.ViewHolder {

    public ImageView chats_image;
    public TextView chats_name, chats_occupation, chats_msgpreview;
    public RelativeLayout chats_container;

    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);

        chats_image = itemView.findViewById(R.id.chat_img);
        chats_name = itemView.findViewById(R.id.chat_name);
        chats_occupation = itemView.findViewById(R.id.chat_occupation);
        chats_msgpreview = itemView.findViewById(R.id.chat_msgprev);

        chats_container = itemView.findViewById(R.id.chat_container);
    }

}
