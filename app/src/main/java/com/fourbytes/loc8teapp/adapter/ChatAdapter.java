package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsViewHolder;
import com.fourbytes.loc8teapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatsViewHolder> {

    Context chats_context;
    List<ChatsItems> chat_items;

    public ChatAdapter(Context chats_context, List<ChatsItems> chat_items) {
        this.chats_context = chats_context;
        this.chat_items = chat_items;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatsViewHolder(LayoutInflater.from(chats_context).inflate(R.layout.fragment_chat_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        holder.chats_name.setText(chat_items.get(position).getChat_name());
        holder.chats_occupation.setText(chat_items.get(position).getChat_occupation());
        holder.chats_msgpreview.setText(chat_items.get(position).getChat_msgpreview());
        holder.chats_image.setImageResource(chat_items.get(position).getChat_image());

        holder.chats_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Chat clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_items.size();
    }
}
