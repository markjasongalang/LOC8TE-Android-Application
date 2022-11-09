package com.fourbytes.loc8teapp.fragment.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentHome_ConnectedList extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private SharedViewModel viewModel;

    private List<ConnectedListItems> connectedList;

    private RecyclerView rvConnectedList;

    private Pair pair;

    private String username;
    private String clientName;
    private String accountType;

    private Map<String, Object> temp;

    private LayoutInflater layoutInflater;

    public FragmentHome_ConnectedList() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_connected, container, false);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        layoutInflater = getLayoutInflater();

        // Get views from layout
        rvConnectedList = view.findViewById(R.id.connected_recyclerview);

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getUsername();
        accountType = pair.getAccountType();
        clientName = pair.getName();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvConnectedList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        temp = new HashMap<>();
        temp.put("exists", true);
        connectedList = new ArrayList<>();
        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        HashSet<String> connected = new HashSet<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            Log.d("try_lang", documentSnapshot.getId() + " " + documentSnapshot.getData());
                            if(documentSnapshot.getData().get("is_connected") != null){
                                if ((boolean) documentSnapshot.getData().get("is_connected")) {
                                    connected.add(documentSnapshot.getId());

                                    db.collection("pro_homes").document(documentSnapshot.getId()).set(temp);
                                    db.collection("pro_homes").document(documentSnapshot.getId()).collection("client_list").document(username).set(temp);
                                } else {
                                    db.collection("pro_homes").document(documentSnapshot.getId()).collection("client_list").document(username).delete();
                                }
                            }
                        }

                        connectedList = new ArrayList<>();
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

                                            // Get profile picture of current user
                                            StorageReference storageRef = storage.getReference();
                                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId().toString() + "_profile.jpg");
                                            connectedList.add(new ConnectedListItems(
                                                    documentSnapshot.getId(),
                                                    fullName,
                                                    specific_job,
                                                    field,
                                                    pathReference
                                            ));

                                        }
                                    }
                                    rvConnectedList.setAdapter(new ConnectedListAdapter(view.getContext(), connectedList, parentFragmentManager, layoutInflater, username, clientName));

                                }
                            }
                        });

                    }
                });
    }
}