package com.fourbytes.loc8teapp.fragment.professional;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentEvent_Edit extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnSave;
    private TextView btnCancel;
    private TextView btnRemove;
    private EditText titleEditView;
    private EditText locationEditView;
    private EditText descriptionEditView;



    private double event_latitude;
    private double event_longitude;
    private String event_location;
    private boolean isLocationSet = false;

    private FirebaseFirestore db;
    private Pair pair = null;
    private SharedViewModel viewModel;
    private FragmentManager fragmentManager;

    private String event_id;
    public FragmentEvent_Edit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_edit, container, false);

        db = FirebaseFirestore.getInstance();
        titleEditView = view.findViewById(R.id.edit_title);
        locationEditView = view.findViewById(R.id.edit_location);
        descriptionEditView = view.findViewById(R.id.edit_description);

        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnRemove = view.findViewById(R.id.btn_remove);

        fragmentManager = getParentFragmentManager();

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        locationEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_SetLocation.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: function for Save changes
                String event_title = titleEditView.getText().toString().trim();
                String event_description = descriptionEditView.getText().toString().trim();
                String event_loc = locationEditView.getText().toString().trim();


                if(!event_title.isEmpty() && !event_description.isEmpty()
                        && !event_loc.isEmpty()){

                    updateEvent(event_title, event_loc, event_description);
                    System.out.println("Data complete");
                    fragmentManager.popBackStack();
                }else{
                    System.out.println("Not Complete");
                }
                System.out.println(event_title);
                System.out.println(event_description);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.popBackStack();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEvent();
                fragmentManager.popBackStack();
            }
        });

        fragmentManager.setFragmentResultListener("editData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                event_latitude = result.getDouble("latitude");
                event_longitude = result.getDouble("longitude");
                titleEditView.setText(result.getString("title"));
                locationEditView.setText(result.getString("location"));
                descriptionEditView.setText(result.getString("description"));
                event_id = result.getString("event_id");
            }
        });

        fragmentManager.setFragmentResultListener("locationData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                event_latitude = result.getDouble("latitude");
                event_longitude = result.getDouble("longitude");
                setLocationText(result.getDouble("latitude"), result.getDouble("longitude"));
            }
        });


        return view;
    }

    private void removeEvent(){
        DocumentReference eventRef = db.collection("events").document(event_id);
        ArrayList<String> pro_ref = new ArrayList<>();
        ArrayList<String> clients_ref = new ArrayList<>();
        db.collection("professionals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            pro_ref.add(document.getId());
                        }

                        removeProRegistered(pro_ref);
                    }
                });

        db.collection("clients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            clients_ref.add(document.getId());
                        }
                        removeClientRegistered(clients_ref);
                    }
                });

        db.collection("professionals")
                .document(pair.getUsername())
                .collection("created_events")
                .whereEqualTo("event_id", event_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference delete_ref = db.collection("prfessionals").document(pair.getUsername()).collection("created_events").document(document.getId());
                            delete_ref.delete();
                        }
                    }
                });
        eventRef.delete();

    }

    private void removeProRegistered(ArrayList<String> id){

        for(int i = 0; i < id.size(); i++){

            try {
                CollectionReference delete_ref = db.collection("professionals").document(id.get(i)).collection("registered_events");

                delete_ref.whereEqualTo("event_id", event_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists()){
                                delete_ref.document(document.getId()).delete();
                            }
                        }
                    }
                });

            }catch (Exception e){

            }
        }
    }

    private void removeClientRegistered(ArrayList<String> id){

        for(int i = 0; i < id.size(); i++){

            try {
                CollectionReference delete_ref = db.collection("clients").document(id.get(i)).collection("registered_events");

                delete_ref.whereEqualTo("event_id", event_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists()){
                                delete_ref.document(document.getId()).delete();
                            }
                        }
                    }
                });

            }catch (Exception e){

            }
        }
    }


    private void updateEvent(String title, String location, String description){
        DocumentReference eventRef = db.collection("events").document(event_id);

        eventRef.update("event_title", title);
        eventRef.update("event_location", location);
        eventRef.update("event_description", description);
        eventRef.update("event_latitude", event_latitude);
        eventRef.update("event_longitude", event_longitude);
    }

    private void setLocationText(double latitude, double longitude){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        final String API_KEY = getString(R.string.google_maps_api_key);

        String location_string = latitude + ", " + longitude;

        System.out.println(location_string);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/geocode/json")
                .buildUpon()
                .appendQueryParameter("latlng", location_string)
                .appendQueryParameter("key", API_KEY)
                .toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");

                            if(status.equals("OK")){
                                JSONArray results = response.getJSONArray("results");
                                String formatted_address = results.getJSONObject(0).getString("formatted_address");
                                locationEditView.setText(formatted_address);
                                event_location = formatted_address;
                                event_latitude = latitude;
                                event_longitude = longitude;
                                isLocationSet = true;
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}