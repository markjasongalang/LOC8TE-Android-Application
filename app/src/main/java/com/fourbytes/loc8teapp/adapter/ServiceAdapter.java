package com.fourbytes.loc8teapp.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceItem;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceViewHolder;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolder> {
    private Context context;
    private List<ServiceItem> serviceItems;

    public ServiceAdapter(Context context, List<ServiceItem> serviceItems) {
        this.context = context;
        this.serviceItems = serviceItems;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(context).inflate(R.layout.service_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.tvServiceName.setText(serviceItems.get(position).getServiceName());
        holder.tvPrice.setText(String.valueOf(serviceItems.get(position).getPrice()));
        holder.tvRateType.setText(serviceItems.get(position).getRateType());
        holder.tvDescription.setText(serviceItems.get(position).getDescription());
        
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You've clicked edit!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }
}
