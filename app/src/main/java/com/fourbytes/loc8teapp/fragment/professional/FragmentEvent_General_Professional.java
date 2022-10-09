package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.GeneralEventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_General_Professional extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;
    private AppCompatButton btnView;
    private RecyclerView recyclerView;
    public FragmentEvent_General_Professional() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_general_professional, container, false);
        btnBack = view.findViewById(R.id.btn_back);
        btnView = view.findViewById(R.id.btn_view);
        List <GeneralEventsItems> items = new ArrayList<GeneralEventsItems>();

        recyclerView = view.findViewById(R.id.general_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        items.add(new GeneralEventsItems(
                "Cooking Show",
                "Sampaloc, Manila",
                "Joshua Padilla",
                "7:00AM - 9:00AM",
                "11/03/2022",
                "Master Chef",
                R.drawable.anya
        ));
        items.add(new GeneralEventsItems(
                "Cooking Show",
                "Sampaloc, Manila",
                "Joshua Padilla",
                "7:00AM - 9:00AM",
                "11/03/2022",
                "Master Chef",
                R.drawable.anya
        ));

        Log.d("EVENTS", "List size" + items.size());

        recyclerView.setAdapter(new GeneralEventsAdapter(view.getContext(), items));

        fragmentManager = getParentFragmentManager();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

//        btnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }


}