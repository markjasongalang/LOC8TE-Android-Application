package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.ChatsViewHolder;
import com.fourbytes.loc8teapp.GroupMemItems;
import com.fourbytes.loc8teapp.GroupMemViewHolder;
import com.fourbytes.loc8teapp.R;

import java.util.List;

public class GroupMemAdapter extends RecyclerView.Adapter<GroupMemViewHolder> {

    Context group_context;
    List<GroupMemItems> group_items;

    public GroupMemAdapter(Context group_context, List<GroupMemItems> group_items) {
        this.group_context = group_context;
        this.group_items = group_items;
    }

    @NonNull
    @Override
    public GroupMemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupMemViewHolder(LayoutInflater.from(group_context).inflate(R.layout.fragment_chat_group_members_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemViewHolder holder, int position) {

        holder.groups_name.setText(group_items.get(position).getGroups_name());
        holder.groups_occupation.setText(group_items.get(position).getGroups_occupation());
        holder.groups_img.setImageResource(group_items.get(position).getGroups_image());

        holder.groups_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Profile picture clicked", Toast.LENGTH_SHORT).show();
            }
        });


        holder.groups_report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Report button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return group_items.size();
    }
}
