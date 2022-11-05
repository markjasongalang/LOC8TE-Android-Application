package com.fourbytes.loc8teapp.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

import java.util.ArrayList;

public class Admin_Recyclerview_Adapter extends RecyclerView.Adapter<Admin_Recyclerview_Adapter.MyViewHolder> {
   public final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Admin_Holder_Data> Admin_Holder_Data;


    public Admin_Recyclerview_Adapter(Context context,ArrayList<Admin_Holder_Data> AdminHolderData,RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.Admin_Holder_Data = AdminHolderData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Admin_Recyclerview_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.accountrequestitem_view,parent,false);


        return new Admin_Recyclerview_Adapter.MyViewHolder(view, recyclerViewInterface);


    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Recyclerview_Adapter.MyViewHolder holder, int position) {

        holder.textViewProfessionalName.setText(Admin_Holder_Data.get(position).getProfessional_name());
        holder.imageView.setImageResource(Admin_Holder_Data.get(position).getImage());
    }

    @Override
    public int getItemCount() {

        return Admin_Holder_Data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewProfessionalName;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.prof_imageview);
            textViewProfessionalName = itemView.findViewById(R.id.prof1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
