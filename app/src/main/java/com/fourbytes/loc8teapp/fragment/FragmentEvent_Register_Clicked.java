package com.fourbytes.loc8teapp.fragment;

import static com.fourbytes.loc8teapp.Constants.MEDICAL_FIELD;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_ListView;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;

public class FragmentEvent_Register_Clicked extends Fragment {
    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnRegister;
    private FragmentManager fragmentManager;

    private TextView locationTextView;
    private TextView titleTextView;
    private EditText nameEditView;
    private CheckBox parkingCheckBox;

    private String event_id;
    private String event_title;
    private String event_location;
    private int parking_count = 0;
    private boolean isParkingChecked = false;
    private boolean isEventIdSet = false;

    private Pair pair = null;
    private FirebaseFirestore db;
    private SharedViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register_clicked, container, false);

        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        fragmentManager = getParentFragmentManager();

        titleTextView = view.findViewById(R.id.event_title);
        locationTextView = view.findViewById(R.id.event_location);
        nameEditView = view.findViewById(R.id.register_name);
        nameEditView.setEnabled(false);
        btnBack = view.findViewById(R.id.btn_back);
        btnRegister = view.findViewById(R.id.register_button);
        parkingCheckBox = view.findViewById(R.id.parking_checkbox);

        parkingCheckBox.setEnabled(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEventIdSet){
                    addRegistered();
                }

                Bundle result = new Bundle();
                result.putString("event_title", event_title);
                result.putString("event_location", event_location);
                result.putString("name", pair.getName());

                fragmentManager.setFragmentResult("successData", result);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Register_Success.class, null)
                        .commit();
            }
        });

        parkingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (parkingCheckBox.isChecked()){
                    isParkingChecked = true;
                }else{
                    isParkingChecked = false;
                }

            }
        });

        fragmentManager.setFragmentResultListener("registerData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                event_id = result.getString("event_id");
                event_title = result.getString("event_title");
                event_location = result.getString("event_location");
                parking_count = result.getInt("parking_count");
                titleTextView.setText(event_title);
                locationTextView.setText(event_location);
                nameEditView.setText(result.getString("name"));

                System.out.println(parking_count);
                if(parking_count > 0){
                    parkingCheckBox.setEnabled(true);
                }
                isEventIdSet = true;
            }
        });

        return view;
    }

    private void addRegistered(){
        String TAG = "Register";
        String type;
        DocumentReference eventRef = db.collection("events").document(event_id);
        if(pair.getAccountType().equals("client")){
            type = "clients";
        }else{
            type = "professionals";
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("event_id", event_id);
        if(isParkingChecked){
            data.put("is_parking", true);
        }
        else{
            data.put("is_parking", false);
        }
        db.collection(type).document(pair.getUsername()).collection("registered_events")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        if (isParkingChecked){
                            eventRef.update("event_parking_count", FieldValue.increment(1));
                        }
                        eventRef.update("event_participant_count", FieldValue.increment(1));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}