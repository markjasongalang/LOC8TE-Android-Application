package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FragmentHome_ConnectedList extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private SharedViewModel viewModel;

    private List<ConnectedListItems> connectedList;

    private RecyclerView rvConnectedList;

    private String username;

    public FragmentHome_ConnectedList() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_connected, container, false);

        // Initialize values
        db = FirebaseFirestore.getInstance();

        // Get views from layout
        rvConnectedList = view.findViewById(R.id.connected_recyclerview);

        // Get username of current user
        username = "";
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            username = data;
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvConnectedList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        HashSet<String> connected = new HashSet<>();
                        for (QueryDocumentSnapshot document : value) {
                            Log.d("try_lang", document.getId() + " " + document.getData());
                            if ((boolean) document.getData().get("is_connected")) {
                                connected.add(document.getId());
                            }
                        }

                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    connectedList = new ArrayList<>();
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if (connected.contains(documentSnapshot.getId())) {
                                            String fullName = documentSnapshot.getData().get("first_name") + " " + documentSnapshot.getData().get("last_name");
                                            String specific_job = documentSnapshot.getData().get("specific_job").toString();
                                            String field = documentSnapshot.getData().get("field").toString();
                                            connectedList.add(new ConnectedListItems(
                                                    fullName,
                                                    specific_job,
                                                    field,
                                                    R.drawable.icon_profile
                                            ));
                                        }
                                    }
                                    rvConnectedList.setAdapter(new ConnectedListAdapter(view.getContext(), connectedList));
                                }
                            }
                        });

                    }
                });
    }
}