package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_MyEvents_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Professional;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class FragmentEvent_Registered extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnUnregister;
    private AppCompatButton btnFindLocation;

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    public FragmentEvent_Registered() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_registered, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnUnregister = view.findViewById(R.id.btn_unregister);
        btnFindLocation = view.findViewById(R.id.btn_find);

        db = FirebaseFirestore.getInstance();
        fragmentManager = getParentFragmentManager();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putDouble("originLatitude", 0.0);
                result.putDouble("originLongitude", 0.0);
                result.putDouble("destinationLatitude", 0.0);
                result.putDouble("destinationLongitude", 0.0);

                fragmentManager.setFragmentResult("findLocationData", result);


            }
        });
        return view;
    }
}