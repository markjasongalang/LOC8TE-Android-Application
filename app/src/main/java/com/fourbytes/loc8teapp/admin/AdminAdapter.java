package com.fourbytes.loc8teapp.admin;

import android.accounts.Account;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminViewHolder> {

    Context context;
    List<AccountRequestItem> items;

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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
