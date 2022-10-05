package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.fourbytes.loc8teapp.ChatsItems;
import com.fourbytes.loc8teapp.ConnectedListItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_Client extends Fragment {

    private View view;
    private RecyclerView chats_recyclerView;

    public FragmentChat_Client() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_client, container, false);
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
                "Loid Forger",
                "Spy Mans",
                "Loid: Hola",
                R.drawable.loid
        ));


        chats_recyclerView.setAdapter(new ChatAdapter(view.getContext(), chats_items));
        return view;
    }
}