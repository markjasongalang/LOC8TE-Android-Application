package com.fourbytes.loc8teapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.fragment.professional.FragmentHome_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.fourbytes.loc8teapp.newlistrecycler.NewListViewHolder;
import com.fourbytes.loc8teapp.R;

import java.util.List;

public class NewListAdapter extends RecyclerView.Adapter<NewListViewHolder> {

    private Context newlist_context;
    private List<NewListItems> newlist_items;
    private FragmentManager parentFragmentManager;

    public NewListAdapter(Context newlist_context, List<NewListItems> newlist_items, FragmentManager parentFragmentManager) {
        this.newlist_context = newlist_context;
        this.newlist_items = newlist_items;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public NewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewListViewHolder(LayoutInflater.from(newlist_context).inflate(R.layout.fragment_home_new_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewListViewHolder holder, int position) {
        holder.new_list_name.setText(newlist_items.get(position).getNewlist_name());
        holder.new_list_occupation.setText(newlist_items.get(position).getNewlist_occupation());
        holder.new_list_field.setText(newlist_items.get(position).getNewlist_field());
        holder.new_list_img.setImageBitmap(newlist_items.get(position).getNewlist_image());

        // Get username of chosen professional
        String proUsername = newlist_items.get(position).getUsername();

        holder.new_list_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPasser.setUsername(proUsername);
                parentFragmentManager.beginTransaction()
                        .replace(R.id.layout_client_home, FragmentProfile_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return newlist_items.size();
    }
}
