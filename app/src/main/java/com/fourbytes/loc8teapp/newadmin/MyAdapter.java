package com.fourbytes.loc8teapp.newadmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fourbytes.loc8teapp.AdminActivity2;
import com.fourbytes.loc8teapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
   Context context;
   List<Item> items;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.accountrequestitem_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getFullName());

        StorageReference pathReference = items.get(position).getPathReference();
        String username = items.get(position).getUsername();
        holder.testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminActivity2.class);
                intent.putExtra("key", username);
                context.startActivity(intent);
            }
        });
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
        return items.size();
    }
}
