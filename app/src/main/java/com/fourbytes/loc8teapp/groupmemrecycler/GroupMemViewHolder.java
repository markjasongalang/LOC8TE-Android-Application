package com.fourbytes.loc8teapp.groupmemrecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class GroupMemViewHolder extends RecyclerView.ViewHolder {

    public ImageView groups_img;
    public TextView groups_name, groups_occupation;
    public AppCompatButton groups_report_button;

    public GroupMemViewHolder(@NonNull View itemView) {
        super(itemView);

        groups_img = itemView.findViewById(R.id.group_img);
        groups_name = itemView.findViewById(R.id.group_name);
        groups_occupation = itemView.findViewById(R.id.group_occupation);

        groups_report_button = itemView.findViewById(R.id.groups_report);
    }
}
