package com.fourbytes.loc8teapp.admin;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.AdminActivity;
import com.fourbytes.loc8teapp.AdminActivity2;
import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.Signup2Activity;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminViewHolder> {

    public Context context;
    public List<AccountRequestItem> items;

    public AdminAdapter(Context context, List<AccountRequestItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminViewHolder(LayoutInflater.from(context).inflate(R.layout.accountrequestitem_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.imageView.setImageResource(items.get(position).getImage());
//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AdminActivity2.class);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
