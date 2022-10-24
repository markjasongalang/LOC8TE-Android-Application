package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.GeneralEventsAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_General extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;
    private AppCompatButton btnView;
    private RecyclerView recyclerView;
    private final String event_general = "general";
    private FirebaseFirestore db;
    List <GeneralEventsItems> items = new ArrayList<GeneralEventsItems>();
    public FragmentEvent_General() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_general_professional, container, false);

        fragmentManager = getParentFragmentManager();

        btnBack = view.findViewById(R.id.btn_back);
        btnView = view.findViewById(R.id.btn_view);


        recyclerView = view.findViewById(R.id.general_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db = FirebaseFirestore.getInstance();

        fetchGeneralEvents();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchGeneralEvents() {
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                items.clear();
                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {
                        if((document.getString("event_industry")).equals(event_general)){
                            items.add(new GeneralEventsItems(
                                    document.getString("event_title"),
                                    document.getString("event_location"),
                                    document.getString("event_host"),
                                    document.getString("event_time"),
                                    document.getString("event_date"),
                                    document.getString("event_host_job"),
                                    document.getId(),
                                    R.drawable.anya
                            ));
                        }
                    }
                }
                updateEvents();
            }

        });
    }

    private void updateEvents(){
        recyclerView.setAdapter(new GeneralEventsAdapter(view.getContext(), items, fragmentManager));
    }
}