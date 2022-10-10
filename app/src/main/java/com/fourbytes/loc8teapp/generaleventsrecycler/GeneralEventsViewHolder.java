package com.fourbytes.loc8teapp.generaleventsrecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class GeneralEventsViewHolder extends RecyclerView.ViewHolder{
    public ImageView host_image;
    public TextView event_title;
    public TextView event_location;
    public TextView event_time;
    public TextView event_date;
    public TextView host_name;
    public TextView host_job;

    public AppCompatButton btn_view;

    public GeneralEventsViewHolder(@NonNull View itemView) {
        super(itemView);

        event_title = itemView.findViewById(R.id.general_item_title);
        event_location = itemView.findViewById(R.id.general_item_location);
        event_time = itemView.findViewById(R.id.general_item_time);
        event_date = itemView.findViewById(R.id.general_item_date);
        host_name = itemView.findViewById(R.id.general_item_hostname);
        host_image = itemView.findViewById(R.id.host_img);
        host_job = itemView.findViewById(R.id.general_item_hostJob);

        btn_view = itemView.findViewById(R.id.btn_view);

    }
}
