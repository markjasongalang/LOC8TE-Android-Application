package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.chatsrecycler.InsideChatItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.InsideChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_InsideChat extends Fragment {
    private View view;

    private FragmentManager parentFragmentManager;

    private RecyclerView inchat_recyclerView;

    private AppCompatButton btnBack;

    public FragmentChat_InsideChat() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_inside_chat, container, false);

        parentFragmentManager = getParentFragmentManager();

        // Get views from layout
        inchat_recyclerView = view.findViewById(R.id.insidechat_recyclerview);
        btnBack = view.findViewById(R.id.btn_back);

        List<InsideChatItems> inside_chat_items = new ArrayList<>();
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        return view;
    }
}