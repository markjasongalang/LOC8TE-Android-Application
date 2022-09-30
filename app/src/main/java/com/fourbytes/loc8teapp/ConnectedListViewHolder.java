package com.fourbytes.loc8teapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectedListViewHolder extends RecyclerView.ViewHolder {

    public ImageView connected_list_img;
    public TextView connected_list_name, connected_list_occupation, connected_list_distance;
    public AppCompatButton connected_list_rate, connected_list_chat, connected_list_profile;

    public ConnectedListViewHolder(@NonNull View itemView) {
        super(itemView);

        connected_list_img = itemView.findViewById(R.id.connectedlist_img);
        connected_list_name = itemView.findViewById(R.id.connectedlist_name);
        connected_list_occupation = itemView.findViewById(R.id.connectedlist_occupation);
        connected_list_distance = itemView.findViewById(R.id.connectedlist_distance);

        connected_list_rate = itemView.findViewById(R.id.connectedlist_rate);
        connected_list_chat = itemView.findViewById(R.id.connectedlist_chat);
        connected_list_profile = itemView.findViewById(R.id.connectedlist_profile);
    }
}
