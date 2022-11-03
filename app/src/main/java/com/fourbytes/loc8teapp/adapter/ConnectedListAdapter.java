package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListViewHolder;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;

import java.util.List;

public class ConnectedListAdapter extends RecyclerView.Adapter<ConnectedListViewHolder> {
    private Context connectedlist_context;
    private List<ConnectedListItems> connectedlist_items;
    private FragmentManager parentFragmentManager;

    public ConnectedListAdapter(Context connectedlist_context, List<ConnectedListItems> connectedlist_items, FragmentManager parentFragmentManager) {
        this.connectedlist_context = connectedlist_context;
        this.connectedlist_items = connectedlist_items;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public ConnectedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConnectedListViewHolder(LayoutInflater.from(connectedlist_context).inflate(R.layout.fragment_home_connected_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedListViewHolder holder, int position) {
        holder.connected_list_name.setText(connectedlist_items.get(position).getConnectedlist_name());
        holder.connected_list_occupation.setText(connectedlist_items.get(position).getConnectedlist_occupation());
        holder.connected_list_field.setText(connectedlist_items.get(position).getConnectedlist_field());
        holder.connected_list_img.setImageBitmap(connectedlist_items.get(position).getConnectedlist_image());

        holder.connected_list_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Chat button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Get username of chosen professional
        String proUsername = connectedlist_items.get(position).getConnectedlist_username();

        holder.connected_list_profile.setOnClickListener(new View.OnClickListener() {
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
        return connectedlist_items.size();
    }
}
