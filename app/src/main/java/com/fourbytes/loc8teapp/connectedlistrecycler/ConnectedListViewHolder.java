package com.fourbytes.loc8teapp.connectedlistrecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ConnectedListViewHolder extends RecyclerView.ViewHolder {

    public ImageView connected_list_img;
    public TextView connected_list_name, connected_list_occupation, connected_list_field;
    public AppCompatButton connected_list_rate, connected_list_chat, connected_list_profile;

    public ConnectedListViewHolder(@NonNull View itemView) {
        super(itemView);

        connected_list_img = itemView.findViewById(R.id.connectedlist_img);
        connected_list_name = itemView.findViewById(R.id.connectedlist_name);
        connected_list_occupation = itemView.findViewById(R.id.connectedlist_occupation);
        connected_list_field = itemView.findViewById(R.id.tv_connected_field);

        connected_list_rate = itemView.findViewById(R.id.connectedlist_rate);
        connected_list_chat = itemView.findViewById(R.id.connectedlist_chat);
        connected_list_profile = itemView.findViewById(R.id.connectedlist_profile);
    }
}
