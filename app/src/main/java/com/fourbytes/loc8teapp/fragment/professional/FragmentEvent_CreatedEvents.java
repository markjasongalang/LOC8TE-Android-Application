package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.CreatedEventsAdapter;
import com.fourbytes.loc8teapp.adapter.RegisteredEventsAdapter;
import com.fourbytes.loc8teapp.myeventsrecycler.MyEventsItems;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_CreatedEvents extends Fragment {

    private View view;

    private RecyclerView recyclerView;

    private CardView btnCreate;

    private FragmentManager fragmentManager;

    private FirebaseFirestore db;

    List<MyEventsItems> items = new ArrayList<MyEventsItems>();
    ArrayList<String> created_events = new ArrayList<>();

    private Pair pair = null;
    private SharedViewModel viewModel;

    private String username;
    private String accountType;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_createdevents, container, false);

        db = FirebaseFirestore.getInstance();
        btnCreate = view.findViewById(R.id.btn_create);
        recyclerView = view.findViewById(R.id.created_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        fragmentManager = getParentFragmentManager();

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        updateEvents();

        username = pair.getUsername();
        accountType = pair.getUsername();

        getCreatedEvents();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: function for Save changes
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Create.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;

    }

    public void getCreatedEvents(){

        db.collection("professionals").document(username).collection("created_events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }

                created_events.clear();
                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {
                        created_events.add(document.getString("event_id"));
                    }
                }

                if (!created_events.isEmpty()){
                    fetchEvents();
                }
            }
        });
    }

    public void fetchEvents(){

        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }

                items.clear();
                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {
                        if(created_events.contains(document.getId())){
                            String start_time = document.getString("event_start_time");
                            String end_time = document.getString("event_end_time");
                            String event_time = start_time + "-" + end_time;
                            items.add(new MyEventsItems(
                                    document.getString("event_title"),
                                    document.getString("event_location"),
                                    document.getString("event_host"),
                                    event_time,
                                    document.getString("event_date"),
                                    document.getString("event_host_job"),
                                    document.getId(),
                                    document.getString("event_host_id"),
                                    document.getString("event_description"),
                                    document.getDouble("event_participant_count").intValue(),
                                    document.getDouble("event_parking_limit").intValue(),
                                    document.getDouble("event_parking_count").intValue(),
                                    document.getDouble("event_latitude"),
                                    document.getDouble("event_longitude")
                            ));
                        }
                    }
                }

                updateEvents();
            }
        });
    }

    public void updateEvents(){

        recyclerView.setAdapter(new CreatedEventsAdapter(view.getContext(), items, fragmentManager));

    }

}
