package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientViewHolder;

import java.util.List;

public class ConnectedClientsAdapter extends RecyclerView.Adapter<ClientViewHolder> {
    private Context context;
    private List<ClientItem> clientItems;

    public ConnectedClientsAdapter(Context context, List<ClientItem> clientItems) {
        this.context = context;
        this.clientItems = clientItems;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(context).inflate(R.layout.connected_client_item_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        holder.ivClientImage.setImageBitmap(clientItems.get(position).getClientImage());

        String firstName = clientItems.get(position).getFirstName().toString();
        String middleName = clientItems.get(position).getMiddleName().toString();
        String lastName = clientItems.get(position).getLastName().toString();

        holder.tvClientName.setText(firstName + " " + middleName + " " + lastName);

        holder.btnClientRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Rate has been clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnClientChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Chat has been clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnClientProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Profile has been clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientItems.size();
    }
}
