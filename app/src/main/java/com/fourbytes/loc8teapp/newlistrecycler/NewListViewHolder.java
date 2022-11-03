package com.fourbytes.loc8teapp.newlistrecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class NewListViewHolder extends RecyclerView.ViewHolder {

    public ImageView new_list_img;
    public TextView new_list_name, new_list_occupation, new_list_field;
    public AppCompatButton new_list_profile;

    public NewListViewHolder(@NonNull View itemView) {
        super(itemView);

        new_list_img = itemView.findViewById(R.id.newlist_img);
        new_list_name = itemView.findViewById(R.id.newlist_name);
        new_list_occupation = itemView.findViewById(R.id.newlist_occupation);
        new_list_field = itemView.findViewById(R.id.tv_new_list_field);

        new_list_profile = itemView.findViewById(R.id.newlist_profile);
    }
}
