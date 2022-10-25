package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_Client extends Fragment {
    private FragmentManager parentFragmentManager;

    private View view;
    private RecyclerView chats_recyclerView;

    public FragmentChat_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_client, container, false);

        parentFragmentManager = getParentFragmentManager();

        List<ChatsItems> chats_items = new ArrayList<>();
        chats_recyclerView = view.findViewById(R.id.chats_recyclerview);
        chats_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chats_items.add(new ChatsItems(
                "Anya Forger",
                "Batang Pasaway",
                "Anya: Waku waku!",
                R.drawable.anya
        ));

        chats_items.add(new ChatsItems(
                "Yor Forger",
                "Assassin",
                "Yor: Good day!",
                R.drawable.yor
        ));

        chats_items.add(new ChatsItems(
                "Loid Forger",
                "Spy Mans",
                "Loid: Hola",
                R.drawable.loid
        ));

        chats_recyclerView.setAdapter(new ChatAdapter(view.getContext(), chats_items, parentFragmentManager));

        return view;
    }
}