package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.GroupMemItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.GroupMemAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_GroupMembers extends Fragment {

    private View view;
    private RecyclerView groups_recyclerView;

    public FragmentChat_GroupMembers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_group_members, container, false);
        List<GroupMemItems> group_items = new ArrayList<>();

        groups_recyclerView = view.findViewById(R.id.groups_recyclerview);
        groups_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        group_items.add(new GroupMemItems(
                "Anya Forger",
                "Batang Pasaway",
                R.drawable.anya
        ));

        group_items.add(new GroupMemItems(
                "Loid Forger",
                "Spy Mans",
                R.drawable.loid
        ));

        group_items.add(new GroupMemItems(
                "Yor Forger",
                "Definitely not an Assassin",
                R.drawable.yor
        ));

        groups_recyclerView.setAdapter(new GroupMemAdapter(view.getContext(), group_items));
        return view;
    }
}