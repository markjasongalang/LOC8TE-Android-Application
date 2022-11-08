package com.fourbytes.loc8teapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_MyEvents_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Professional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private String event_id;
    private double latitude;
    private double longitude;

    private Pair pair = null;
    private SharedViewModel viewModel;
    public FragmentEvent_Registered() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_registered, container, false);

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

        btnBack = view.findViewById(R.id.btn_back);
        btnUnregister = view.findViewById(R.id.btn_unregister);
        btnFindLocation = view.findViewById(R.id.btn_find);

        db = FirebaseFirestore.getInstance();
        fragmentManager = getParentFragmentManager();

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unregisterEvent();
                fragmentManager.popBackStack();
            }
        });

        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putDouble("destinationLatitude", latitude);
                result.putDouble("destinationLongitude", longitude);

                fragmentManager.setFragmentResult("locationData", result);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_FindEventLocation.class, null)
                        .commit();
            }
        });

        fragmentManager.setFragmentResultListener("eventData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                event_title.setText(result.getString("title"));
                event_location.setText(result.getString("location"));
                event_description.setText(result.getString("description"));
                event_participant_count.setText(String.valueOf(result.getInt("participant")));
                host_name.setText(result.getString("host"));
                host_profession.setText(result.getString("host_job"));
                event_date.setText(result.getString("date"));
                event_time.setText(result.getString("time"));
                event_parking_count.setText(String.valueOf(result.getInt("parking_limit") - result.getInt("parking_count")));
                latitude = result.getDouble("latitude");
                longitude = result.getDouble("longitude");
                event_id = result.getString("event_id");

                String host_id = result.getString("host_id");

                StorageReference storageRef = storage.getReference();
                StorageReference pathReference = storageRef.child("profilePics/" + host_id + "_profile.jpg");

                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        host_img.setImageBitmap(bmp);
                    }
                });
            }
        });
        return view;
    }

    private void unregisterEvent(){
        String type;
        String username = pair.getUsername();
        if(pair.getAccountType().equals("client")){
            type = "clients";
        }else{
            type = "professionals";
        }
        db.collection(type).document(username).collection("registered_events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals("sample_event")){
                                    if(document.getString("event_id").equals(event_id)){

                                        db.collection(type).document(username).collection("registered_events").document(document.getId()).delete();
                                        DocumentReference eventRef = db.collection("events").document(event_id);

                                        if(document.getBoolean("is_parking")){
                                            eventRef.update("event_parking_count", FieldValue.increment(-1));
                                        }
                                        eventRef.update("event_participant_count", FieldValue.increment(-1));
                                        break;
                                    }
                                }
                            }

                        }
                    }
                });
    }
}