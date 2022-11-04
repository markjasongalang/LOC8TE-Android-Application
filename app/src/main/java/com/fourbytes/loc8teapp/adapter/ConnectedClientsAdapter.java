package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientViewHolder;
import com.fourbytes.loc8teapp.fragment.client.FragmentProfile_Client;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ConnectedClientsAdapter extends RecyclerView.Adapter<ClientViewHolder> {
    private Context context;

    private List<ClientItem> clientItems;

    private FragmentManager parentFragmentManager;

    public ConnectedClientsAdapter(Context context, List<ClientItem> clientItems, FragmentManager parentFragmentManager) {
        this.context = context;
        this.clientItems = clientItems;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(context).inflate(R.layout.connected_client_item_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {

        int pos = position;

        StorageReference pathReference = clientItems.get(position).getPathReference();
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivClientImage.setImageBitmap(bmp);
            }
        });

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

                DataPasser.setUsername2(clientItems.get(pos).getUsername());

                parentFragmentManager.beginTransaction()
                        .replace(R.id.layout_home_professional, FragmentProfile_Client.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return clientItems.size();
    }
}
