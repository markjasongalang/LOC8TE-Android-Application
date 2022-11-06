package com.fourbytes.loc8teapp.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.AdminActivity2;
import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Admin_Recyclerview_Adapter extends RecyclerView.Adapter<Admin_Recyclerview_Adapter.MyViewHolder> {
    static int pos;
    static String username;
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
        username = Admin_Holder_Data.get(position).getUsername();

//        DataPasser.setProUsername(username);
        holder.TestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AdminActivity2.class);
                intent.putExtra("key", username);
                context.startActivity(intent);

//                DataPasser.setProUsername(username);
//                recyclerViewInterface.onItemClick(position);

            }
        });
        holder.textViewProfessionalName.setText(Admin_Holder_Data.get(position).getFullName() + " " + username);
        StorageReference pathReference = Admin_Holder_Data.get(position).getPathReference();
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                holder.imageView.setImageBitmap(bmp);


                Log.d("image_stats", "Image retrieved.");

            }
        });




    }

    @Override
    public int getItemCount() {

        return Admin_Holder_Data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewProfessionalName;
        Button TestButton;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.prof_imageview);
            textViewProfessionalName = itemView.findViewById(R.id.prof1);
            TestButton = itemView.findViewById(R.id.testbutton);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(recyclerViewInterface != null){
////                        int newpos = getAdapterPosition();
////                        username = "joshua";
//                        int newpos = pos;
//                        DataPasser.setProUsername(username);
//                        Log.d("joshua",username);
//
//                        if(pos != RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(newpos);
//                        }
//                    }
//                }
//            });
        }
    }

}
