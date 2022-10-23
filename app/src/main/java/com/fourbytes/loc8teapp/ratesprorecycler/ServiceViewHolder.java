package com.fourbytes.loc8teapp.ratesprorecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ServiceViewHolder extends RecyclerView.ViewHolder {
    public TextView tvServiceName;
    public TextView tvPrice;
    public TextView tvRateType;
    public TextView tvDescription;

    public AppCompatButton btnEdit;

    public ServiceViewHolder(@NonNull View itemView) {
        super(itemView);

        tvServiceName = itemView.findViewById(R.id.tv_service_name);
        tvPrice = itemView.findViewById(R.id.tv_price);
        tvRateType = itemView.findViewById(R.id.tv_rate_type);
        tvDescription = itemView.findViewById(R.id.tv_description);

        btnEdit = itemView.findViewById(R.id.btn_edit);
    }
}
