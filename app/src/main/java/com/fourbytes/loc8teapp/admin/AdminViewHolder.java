package com.fourbytes.loc8teapp.admin;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fourbytes.loc8teapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView nameView;


    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.prof1);
        
    }
}
