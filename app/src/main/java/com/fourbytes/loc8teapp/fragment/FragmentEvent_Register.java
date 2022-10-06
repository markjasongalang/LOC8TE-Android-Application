package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentEvent_Register extends Fragment {
    private View view;
    private TextView event_title;
    private TextView event_location;
    private TextView event_description;
    private TextView event_date;
    private TextView event_time;
    private TextView host_name;
    private TextView host_profession;
    private ImageView host_img;

    private String doc_event_title;
    private String doc_event_location;
    private String doc_event_desc;
    private String doc_event_date;
    private String doc_event_time;
    private String doc_host_id;
    private String doc_host_profession;

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register, container, false);

        fragmentManager = getParentFragmentManager();

        db = FirebaseFirestore.getInstance();

        btnBack = view.findViewById(R.id.btn_back);
        event_title = view.findViewById(R.id.event_title);
        event_location = view.findViewById(R.id.event_location);
        event_description = view.findViewById(R.id.event_desc);
        event_date = view.findViewById(R.id.event_date);
        event_time = view.findViewById(R.id.event_time);
        host_name = view.findViewById(R.id.host_name);
        host_profession = view.findViewById(R.id.host_profession);
        host_img = view.findViewById(R.id.host_img);

        //TODO: Make separate function later for database fetch
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getId().equals("A9yfcf5G1asBbfc1iw5h")){
                                    //TODO: Make separate function later to set component texts/value
                                    Log.d("EVENTS", "---------------SUCESS-----------------");
                                    doc_event_title = document.getString("event_title");
                                    doc_event_location = document.getString("event_location");
                                    doc_event_desc = document.getString("event_description");
                                    doc_event_date = document.getString("event_date");
                                    doc_event_time = document.getString("event_time");
                                    doc_host_id = document.getString("event_host");

                                    event_title.setText(doc_event_title);
                                    event_location.setText(doc_event_location);
                                    event_description.setText(doc_event_desc);

                                    break;

                                }

                            }
                        } else {
                            Toast.makeText(getActivity(), "There are no Events", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if ((document.getId()).equals(doc_host_id)){
                                    doc_host_profession = document.getString("job_title");
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "User not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        // Place code here

        return view;
    }
}