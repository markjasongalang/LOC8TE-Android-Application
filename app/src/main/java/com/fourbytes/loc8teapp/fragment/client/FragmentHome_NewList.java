package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome_NewList extends Fragment {

    View view;
    RecyclerView new_list_recyclerView;

    public FragmentHome_NewList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_new, container, false);
        List<NewListItems> newlist_items = new ArrayList<>();
        new_list_recyclerView = view.findViewById(R.id.new_recyclerview);
        new_list_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        newlist_items.add(new NewListItems(
                "Anya Forger",
                "Batang Pasaway",
                "8 km away",
                R.drawable.anya
        ));

        newlist_items.add(new NewListItems(
                "Loid Forger",
                "Spy Mans",
                "10 km away",
                R.drawable.loid
        ));


        newlist_items.add(new NewListItems(
                "Yor Forger",
                "Definitely not an Assassin",
                "5 km away",
                R.drawable.yor
        ));

        newlist_items.add(new NewListItems(
                "Joshua Padilla",
                "Chef God",
                "20 km away",
                R.drawable.icon_profile
        ));

        newlist_items.add(new NewListItems(
                "Jason Galang",
                "Programmist",
                "15 km away",
                R.drawable.icon_profile
        ));

        newlist_items.add(new NewListItems(
                "Mary Angeline",
                "Magpapa-Survey sa buong Manila",
                "999 km away",
                R.drawable.icon_profile
        ));

        newlist_items.add(new NewListItems(
                "Allen Dela Rosa",
                "On Call Sofware Egineer",
                "13 km away",
                R.drawable.icon_profile
        ));

        new_list_recyclerView.setAdapter(new NewListAdapter(view.getContext(), newlist_items));

        return view;
    }
}