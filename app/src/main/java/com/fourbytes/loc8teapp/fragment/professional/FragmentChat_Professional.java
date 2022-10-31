package com.fourbytes.loc8teapp.fragment.professional;

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

public class FragmentChat_Professional extends Fragment {
    private FragmentManager parentFragmentManager;

    private View view;
    private RecyclerView chats_recyclerView1, chats_recyclerView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_professional, container, false);

        parentFragmentManager = getParentFragmentManager();

        List<ChatsItems> chats_items_industry = new ArrayList<>();
        List<ChatsItems> chats_items_clients = new ArrayList<>();

        chats_recyclerView1 = view.findViewById(R.id.chats_recyclerview1);
        chats_recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chats_recyclerView2 = view.findViewById(R.id.chats_recyclerview2);
        chats_recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //industry chat
        chats_items_industry.add(new ChatsItems(
                "IT Industry",
                "Industry Chat",
                "Juswa: Gawa tayo software guys",
                R.drawable.juswa_hearts
        ));

        //clients chat
        chats_items_clients.add(new ChatsItems(
                "Anya Forger",
                "Client",
                "Anya: Waku waku!",
                R.drawable.anya
        ));

        chats_items_clients.add(new ChatsItems(
                "Yor Forger",
                "Client",
                "Yor: Henlo",
                R.drawable.yor
        ));

        chats_recyclerView1.setAdapter(new ChatAdapter(view.getContext(), chats_items_industry, parentFragmentManager));
        chats_recyclerView2.setAdapter(new ChatAdapter(view.getContext(), chats_items_clients, parentFragmentManager));

        return view;
    }
}