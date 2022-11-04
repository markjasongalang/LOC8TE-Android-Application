package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsViewHolder;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentChat_InsideChat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatsViewHolder> {
    private FragmentManager parentFragmentManager;

    private Context chats_context;

    private List<ChatsItems> chat_items;

    public ChatAdapter(Context chats_context, List<ChatsItems> chat_items, FragmentManager parentFragmentManager) {
        this.chats_context = chats_context;
        this.chat_items = chat_items;
        this.parentFragmentManager = parentFragmentManager;
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
        holder.chats_image.setImageBitmap(chat_items.get(position).getChat_image());

        holder.chats_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("pro_username", holder.chats_name.getText().toString());
                parentFragmentManager.setFragmentResult("data_from_prev", result);
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentChat_InsideChat.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_items.size();
    }
}
