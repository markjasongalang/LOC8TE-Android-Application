package com.fourbytes.loc8teapp.admin;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.Signup2Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView nameView;
    public Button button;

    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.prof1);
        button = itemView.findViewById(R.id.check_prof);
    }
}
