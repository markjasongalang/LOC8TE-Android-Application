package com.fourbytes.loc8teapp.newadmin;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fourbytes.loc8teapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView nameView;
    Button testbutton;



    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.prof_imageview);
        nameView = itemView.findViewById(R.id.prof1);
        testbutton = itemView.findViewById(R.id.testbutton);

    }
}
