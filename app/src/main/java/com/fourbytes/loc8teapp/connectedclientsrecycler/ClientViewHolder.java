package com.fourbytes.loc8teapp.connectedclientsrecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView ivClientImage;

    public TextView tvClientName;

    public AppCompatButton btnClientRate;
    public AppCompatButton btnClientChat;
    public AppCompatButton btnClientProfile;

    public ClientViewHolder(@NonNull View itemView) {
        super(itemView);

        ivClientImage = itemView.findViewById(R.id.iv_client_image);
        tvClientName = itemView.findViewById(R.id.tv_client_name);

        btnClientRate = itemView.findViewById(R.id.btn_client_rate);
        btnClientChat = itemView.findViewById(R.id.btn_client_chat);
        btnClientProfile = itemView.findViewById(R.id.btn_client_profile);
    }
}
