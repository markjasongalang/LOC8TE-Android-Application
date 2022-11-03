package com.fourbytes.loc8teapp.fragment.professional;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ConnectedClientsAdapter;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_ConnectedList;
import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome_Professional extends Fragment {
    private View view;
    private View l;
    private View l2;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;

    private AppCompatButton logoutButton;

    private RecyclerView rvConnectedClients;

    private List<ClientItem> connectedClientList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    public FragmentHome_Professional() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_professional, container, false);

        // Get views from layout
        logoutButton = view.findViewById(R.id.logout);
        home_settings_FAB = view.findViewById(R.id.home_settings_prof);
        location_settings_FAB = view.findViewById(R.id.location_settings_prof);
        l = view.findViewById(R.id.home_settings_toolbar_prof);
        l2 = view.findViewById(R.id.location_settings_toolbar_prof);
        rvConnectedClients = view.findViewById(R.id.rv_connected_clients);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getFirst();
        accountType = pair.getSecond();

        setupViews();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvConnectedClients.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                username = pair.getFirst();
                accountType = pair.getSecond();
                Log.d("hello", username);

                db.collection("pro_homes").document(username).collection("client_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        connectedClientList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            db.collection("clients").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    // Get profile picture of current user
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference pathReference = storageRef.child("profilePics/" + task.getResult().getId().toString() + "_profile.jpg");
                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Log.d("image_stats", "Image retrieved.");
                                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            connectedClientList.add(new ClientItem(
                                                    bmp,
                                                    task.getResult().getData().get("first_name").toString(),
                                                    "",
                                                    task.getResult().get("last_name").toString()
                                            ));
                                            rvConnectedClients.setAdapter(new ConnectedClientsAdapter(view.getContext(), connectedClientList));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.d("image_stats", "Image not retrieved.");
                                        }
                                    });

                                }
                            });
                        }

                    }
                });

            }
        }, 500);
    }

    private void setupViews() {
        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);

        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;

        home_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible) {
                    home_settings_FAB.extend();
                    l.setVisibility(view.VISIBLE);
                    isAllFABVisible = true;
                } else {
                    home_settings_FAB.shrink();
                    l.setVisibility(view.GONE);
                    isAllFABVisible = false;
                }
            }
        });

        location_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible2) {
                    location_settings_FAB.extend();
                    l2.setVisibility(view.VISIBLE);
                    isAllFABVisible2 = true;
                } else {
                    location_settings_FAB.shrink();
                    l2.setVisibility(view.GONE);
                    isAllFABVisible2 = false;
                }
            }
        });
    }
}