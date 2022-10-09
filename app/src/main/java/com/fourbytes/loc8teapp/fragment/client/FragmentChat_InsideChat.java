package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.ChatsItems;
import com.fourbytes.loc8teapp.InsideChatItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;
import com.fourbytes.loc8teapp.adapter.InsideChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_InsideChat extends Fragment {

    private View view;
    private RecyclerView inchat_recyclerView;

    public FragmentChat_InsideChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_inside_chat, container, false);
        List<InsideChatItems> inside_chat_items = new ArrayList<>();
        inchat_recyclerView = view.findViewById(R.id.insidechat_recyclerview);
        inchat_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        inside_chat_items.add(new InsideChatItems(
                "Anya?",
                R.drawable.yor,
                InsideChatItems.layout_right
        ));

        inside_chat_items.add(new InsideChatItems(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                R.drawable.yor,
                InsideChatItems.layout_right
        ));

        inside_chat_items.add(new InsideChatItems(
                "Waku waku!",
                R.drawable.anya,
                InsideChatItems.layout_left
        ));


        inchat_recyclerView.setAdapter(new InsideChatAdapter(view.getContext(), inside_chat_items));
        return view;
    }
}