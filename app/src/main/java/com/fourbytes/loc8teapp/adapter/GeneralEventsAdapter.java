package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.GeneralEventsItems;
import com.fourbytes.loc8teapp.GeneralEventsViewHolder;
import com.fourbytes.loc8teapp.R;

import java.util.List;

public class GeneralEventsAdapter extends RecyclerView.Adapter<GeneralEventsViewHolder> {

    Context context;
    private List<GeneralEventsItems> general_items;

    public GeneralEventsAdapter(Context context, List<GeneralEventsItems> general_items) {

        this.context = context;
        this.general_items = general_items;
    }

    @NonNull
    @Override
    public GeneralEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralEventsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_event_general_professional_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralEventsViewHolder holder, int position) {
        holder.event_title.setText(general_items.get(position).getEvent_title());
        holder.event_location.setText(general_items.get(position).getEvent_location());
        holder.event_time.setText(general_items.get(position).getTime());
        holder.event_date.setText(general_items.get(position).getDate());
        holder.host_name.setText(general_items.get(position).getHosted_by());
        holder.host_job.setText(general_items.get(position).getJob_title());
        holder.host_image.setImageResource(general_items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return general_items.size();
    }
}
