package com.fourbytes.loc8teapp.fragment.professional;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ConnectedClientsAdapter;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_ConnectedList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome_Professional extends Fragment {
    private View view;
    private View l;
    private View l2;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;

    private AppCompatButton logoutButton;

    private RecyclerView rvConnectedClients;

    private List<ClientItem> connectedClientList;

    private SharedViewModel viewModel;

    private String username;

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
        mAuth = FirebaseAuth.getInstance();

        setupViews();

        username = "";
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            username = data;
        });

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

//        connectedClientList.add(new ClientItem(
//                R.drawable.loid,
//                "Loid",
//                "Secret",
//                "Forger"
//        ));
//
//        connectedClientList.add(new ClientItem(
//                R.drawable.yor,
//                "Yor",
//                "Secret",
//                "Forger"
//        ));
//
//        rvConnectedClients.setAdapter(new ConnectedClientsAdapter(view.getContext(), connectedClientList));



//        connectedClientList.add(new ClientItem(
//                R.drawable.icon_profile,
//                task.getResult().getData().get("first_name").toString(),
//                task.getResult().getData().get("middle_name").toString(),
//                task.getResult().get("last_name").toString()
//        ));
//        rvConnectedClients.setAdapter(new ConnectedClientsAdapter(view.getContext(), connectedClientList));

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