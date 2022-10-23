package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.GeneralEventsAdapter;
import com.fourbytes.loc8teapp.adapter.IndustryEventsAdapter;
import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsItems;
import com.fourbytes.loc8teapp.industryeventsrecycler.IndustryEventsItems;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_Industry_Professional extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private FirebaseFirestore db;

    private RecyclerView recyclerView;

    private AppCompatButton btnBack;
    private AppCompatButton btnView;

    List<IndustryEventsItems> items = new ArrayList<IndustryEventsItems>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_industry_professional, container, false);

        fragmentManager = getParentFragmentManager();

        btnBack = view.findViewById(R.id.btn_back);
        btnView = view.findViewById(R.id.btn_view);


        recyclerView = view.findViewById(R.id.industry_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db = FirebaseFirestore.getInstance();

        fetchEvents("general");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void fetchEvents(String industry){
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                items.clear();
                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {
                        if((document.getString("event_industry")).equals(industry)){
                            items.add(new IndustryEventsItems(
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
    public void updateEvents(){
        recyclerView.setAdapter(new IndustryEventsAdapter(view.getContext(), items, fragmentManager));
    }
}