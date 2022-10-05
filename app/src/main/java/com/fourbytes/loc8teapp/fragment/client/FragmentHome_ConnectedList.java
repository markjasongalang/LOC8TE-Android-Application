package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.ConnectedListItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome_ConnectedList extends Fragment {

    private View view;
    private RecyclerView connected_list_recyclerView;

    public FragmentHome_ConnectedList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_connected, container, false);
        List<ConnectedListItems> connectedlist_items = new ArrayList<>();
        connected_list_recyclerView = view.findViewById(R.id.connected_recyclerview);
        connected_list_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        connectedlist_items.add(new ConnectedListItems(
                "Loid Forger",
                "Spy Mans",
                "10 km away",
                R.drawable.loid
        ));

        connectedlist_items.add(new ConnectedListItems(
                "Yor Forger",
                "Definitely not an Assassin",
                "5 km away",
                R.drawable.yor
        ));

        connected_list_recyclerView.setAdapter(new ConnectedListAdapter(view.getContext(), connectedlist_items));
        return view;
    }
}