package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fourbytes.loc8teapp.GeneralEventsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.GeneralEventsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_General_Professional extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;
    private AppCompatButton btnView;
    private RecyclerView recyclerView;
    private final String event_general = "general";
    private FirebaseFirestore db;
    public FragmentEvent_General_Professional() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_general_professional, container, false);

        fragmentManager = getParentFragmentManager();

        btnBack = view.findViewById(R.id.btn_back);
        btnView = view.findViewById(R.id.btn_view);
        List <GeneralEventsItems> items = new ArrayList<GeneralEventsItems>();

        recyclerView = view.findViewById(R.id.general_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db = FirebaseFirestore.getInstance();

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String event_id = document.getId();
                        String event_title = document.getString("event_title");
                        String event_industry = document.getString("event_industry");
                        String event_location = document.getString("event_location");
                        String event_host = document.getString("hosted_by"); //change later by event_host
                        String event_time = document.getString("time");
                        String event_date = document.getString("date");


                        if(event_industry.equals(event_general)){
                            //add to list for recycler view
                            //Remove Logs Later
                            Log.d("EVENTS", "Event ID: " + event_id);
                            Log.d("EVENTS", "Event Title: " + event_title);
                            Log.d("EVENTS", "Event Industry: " + event_industry);
                            Log.d("EVENTS", "Event Location: " + event_location);
                            Log.d("EVENTS", "Event Date: " + event_date);
                            Log.d("EVENTS", "Event Host: " + event_host);
                            Log.d("EVENTS", "Event Time: " + event_time);
                            Log.d("EVENTS", "--------------------------------");
                            //Remove Logs Later

                        }
                    }
                } else {
                    Log.d("EVENTS", "List size");
                    Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                }
            }
        });
        items.add(new GeneralEventsItems(
                "Cooking Show",
                "Sampaloc, Manila",
                "Joshua Padilla",
                "7:00AM - 9:00AM",
                "11/03/2022",
                "Master Chef",
                "",
                R.drawable.anya
        ));
        items.add(new GeneralEventsItems(
                "Cooking Show",
                "Sampaloc, Manila",
                "Joshua Padilla",
                "7:00AM - 9:00AM",
                "11/03/2022",
                "Master Chef",
                "",
                R.drawable.anya
        ));

        Log.d("EVENTS", "List size" + items.size());

        recyclerView.setAdapter(new GeneralEventsAdapter(view.getContext(), items));

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