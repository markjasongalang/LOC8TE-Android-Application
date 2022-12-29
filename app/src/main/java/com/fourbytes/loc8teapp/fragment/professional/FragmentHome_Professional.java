package com.fourbytes.loc8teapp.fragment.professional;

import static com.fourbytes.loc8teapp.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ConnectedClientsAdapter;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FragmentHome_Professional extends Fragment {
    private View view;
    private View l;
    private View l2;

    private Pair pair = null;
    private SharedViewModel viewModel;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;

    private AppCompatButton logoutButton;
    private AppCompatButton updateLocationButton;

    private RecyclerView rvConnectedClients;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private List<ClientItem> connectedClientList;


    private String username;
    private String accountType;
    private String professionalName;

    private LayoutInflater layoutInflater;

    private LocationCallback mLocationCallback;

    private boolean isLocationSet = false;
    public FragmentHome_Professional() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_professional, container, false);

        // Get views from layout
        logoutButton = view.findViewById(R.id.logout);
        updateLocationButton = view.findViewById(R.id.btn_set_location);
        home_settings_FAB = view.findViewById(R.id.home_settings_prof);
        location_settings_FAB = view.findViewById(R.id.location_settings_prof);
        l = view.findViewById(R.id.home_settings_toolbar_prof);
        l2 = view.findViewById(R.id.location_settings_toolbar_prof);
        rvConnectedClients = view.findViewById(R.id.rv_connected_clients);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        layoutInflater = getLayoutInflater();

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        setupViews();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPasser.setUsername2(null);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startLocationRequest();
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
                username = pair.getUsername();
                accountType = pair.getAccountType();
                professionalName = pair.getName();
                Log.d("hello", username);

                db.collection("pro_homes").document(username).collection("client_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        connectedClientList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            // Get profile picture of current user
                            StorageReference storageRef = storage.getReference();
                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId().toString() + "_profile.jpg");
                            connectedClientList.add(new ClientItem(
                                    pathReference,
                                    documentSnapshot.getId()
                            ));
                        }
                        rvConnectedClients.setAdapter(new ConnectedClientsAdapter(
                                view.getContext(),
                                connectedClientList,
                                parentFragmentManager,
                                layoutInflater,
                                professionalName,
                                username
                        ));

                    }
                });

            }
        }, 300);
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

    public void startLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        if(!isLocationSet){
                            GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                            updateLocation(geopoint.getLatitude(), geopoint.getLongitude());
                            isLocationSet = false;
                        }
                        isLocationSet = true;
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showGPSPopUp();
            return;
        }
        LocationServices.getFusedLocationProviderClient(view.getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void updateLocation(double latitude, double longitude){
        String type = "professionals";
        if(pair.getAccountType().equals("client")){
            type = "clients";
        }

        DocumentReference update_ref = db.collection(type).document(pair.getUsername());
        update_ref.update("latitude", latitude);
        update_ref.update("longitude", longitude);


        showChangeLocationComplete(latitude, longitude);
    }

    public void showGPSPopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View noGPS_view = getLayoutInflater().inflate(R.layout.no_gps_popup, null);

        AppCompatButton btn = noGPS_view.findViewById(R.id.btn_ok);
        dialogBuilder.setView(noGPS_view);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    public void showChangeLocationComplete(double latitude, double longitude) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View locationUpdate_view = getLayoutInflater().inflate(R.layout.update_location_popup, null);

        AppCompatButton btn = locationUpdate_view.findViewById(R.id.btn_ok);
        TextView locationTextView = locationUpdate_view.findViewById(R.id.location_text);
        setLocationText(latitude, longitude, locationTextView);
        dialogBuilder.setView(locationUpdate_view);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void setLocationText(double latitude, double longitude, TextView locationTextView){
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
                                locationTextView.setText(formatted_address);
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