package com.fourbytes.loc8teapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class NewListViewHolder extends RecyclerView.ViewHolder {

    public ImageView new_list_img;
    public TextView new_list_name, new_list_occupation, new_list_distance;
    public AppCompatButton new_list_chat, new_list_profile;

    public NewListViewHolder(@NonNull View itemView) {
        super(itemView);

        new_list_img = itemView.findViewById(R.id.newlist_img);
        new_list_name = itemView.findViewById(R.id.newlist_name);
        new_list_occupation = itemView.findViewById(R.id.newlist_occupation);
        new_list_distance = itemView.findViewById(R.id.newlist_distance);

        new_list_chat = itemView.findViewById(R.id.newlist_chat);
        new_list_profile = itemView.findViewById(R.id.newlist_profile);
    }
}
