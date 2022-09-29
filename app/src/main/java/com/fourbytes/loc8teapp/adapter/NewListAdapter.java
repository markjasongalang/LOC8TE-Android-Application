package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.NewListItems;
import com.fourbytes.loc8teapp.NewListViewHolder;
import com.fourbytes.loc8teapp.R;

import java.util.List;

public class NewListAdapter extends RecyclerView.Adapter<NewListViewHolder> {

    Context newlist_context;
    private List<NewListItems> newlist_items;

    public NewListAdapter(Context newlist_context, List<NewListItems> newlist_items) {
        this.newlist_context = newlist_context;
        this.newlist_items = newlist_items;
    }

    @NonNull
    @Override
    public NewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewListViewHolder(LayoutInflater.from(newlist_context).inflate(R.layout.fragment_home_new_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewListViewHolder holder, int position) {
        holder.new_list_name.setText(newlist_items.get(position).getNewlist_name());
        holder.new_list_occupation.setText(newlist_items.get(position).getNewlist_occupation());
        holder.new_list_distance.setText(newlist_items.get(position).getNewlist_distance());
        holder.new_list_img.setImageResource(newlist_items.get(position).getNewlist_image());

        holder.new_list_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Chat button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.new_list_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Profile button clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return newlist_items.size();
    }
}
