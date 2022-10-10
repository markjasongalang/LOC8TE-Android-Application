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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private TextView event_participant_count;
    private TextView event_parking_count;
    private ImageView host_img;

    private String doc_event_title;
    private String doc_event_location;
    private String doc_event_desc;
    private String doc_event_date;
    private String doc_event_time;
    private String doc_host;
    private String doc_host_profession;
    private String doc_event_id;
    private String doc_event_participant_count;
    private String doc_event_parking_count;
    private FirebaseFirestore db;
    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;
    private AppCompatButton btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register, container, false);

        fragmentManager = getParentFragmentManager();

        db = FirebaseFirestore.getInstance();

        btnBack = view.findViewById(R.id.btn_back);
        btnRegister = view.findViewById(R.id.btn_register);
        event_title = view.findViewById(R.id.event_title);
        event_location = view.findViewById(R.id.event_location);
        event_description = view.findViewById(R.id.event_desc);
        event_date = view.findViewById(R.id.event_date);
        event_time = view.findViewById(R.id.event_time);
        host_name = view.findViewById(R.id.host_name);
        host_profession = view.findViewById(R.id.host_profession);
        event_participant_count = view.findViewById(R.id.participants_count);
        event_parking_count = view.findViewById(R.id.parking_count);
        host_img = view.findViewById(R.id.host_img);


        doc_event_id = "A9yfcf5G1asBbfc1iw5h"; //TODO: Change this later

        fetchEvent(doc_event_id);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Register_Clicked.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    //TODO: Change to more complex queries later :D
    public void fetchEvent(String event_id){
        final DocumentReference docRef = db.collection("events").document(event_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("DATABASE EVENT", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("DATABASE EVENT", "Current data: " + snapshot.getData());
                    doc_event_title = snapshot.getData().get("event_title").toString();
                    doc_event_location = snapshot.getData().get("event_location").toString();
                    doc_event_desc = snapshot.getData().get("event_description").toString();
                    doc_event_date = snapshot.getData().get("event_date").toString();
                    doc_event_time = snapshot.getData().get("event_time").toString();
                    doc_host = snapshot.getData().get("event_host").toString();
                    doc_host_profession = snapshot.getData().get("event_host_job").toString();
                    doc_event_participant_count = snapshot.getData().get("event_participant_no").toString();
                    doc_event_parking_count = snapshot.getData().get("event_parking_count").toString();


                } else {
                    Log.d("DATABASE EVENT", "Current data: null");
                }

                setValue();
            }
        });
        return;
    }

    public void setValue(){
        event_title.setText(doc_event_title);
        event_location.setText(doc_event_location);
        event_description.setText(doc_event_desc);
        event_participant_count.setText(doc_event_participant_count);
        event_date.setText(doc_event_date);
        event_time.setText(doc_event_time);
        event_parking_count.setText(doc_event_parking_count);
        host_name.setText(doc_host);
        host_profession.setText(doc_host_profession);
        return;
    }
}