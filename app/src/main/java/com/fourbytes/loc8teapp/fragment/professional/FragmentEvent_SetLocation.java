package com.fourbytes.loc8teapp.fragment.professional;

import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class FragmentEvent_SetLocation extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{
    private View view;
    private MapView map_view;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private AlertDialog.Builder NoMarkerdialogBuilder;
    private AlertDialog NoMarkerdialog;
    private AppCompatButton btn_set_location;
    private AppCompatButton btn_back;
    private AppCompatButton btn_ok;
    private AppCompatButton btn_marker_ok;

    private FragmentManager parentFragmentManager;

    private FirebaseFirestore db;
    private GoogleMap map_instance;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private double currentUserLat;
    private double currentUserLong;

    private final double CAMERA_DEFAULT_LATITUDE = 14.603760;
    private final double CAMERA_DEFAULT_LONGITUDE = 120.989200;

    private double clickedLat;
    private double clickedLong;

    private Marker clickMarker;

    private boolean isMarkerExist = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_setlocation_professional, container, false);
        final View popup_view = getLayoutInflater().inflate(R.layout.no_gps_popup, null);
        final View noMarker_view = getLayoutInflater().inflate(R.layout.no_marker_popup, null);

        dialogBuilder = new AlertDialog.Builder(view.getContext());
        NoMarkerdialogBuilder = new AlertDialog.Builder(view.getContext());
        map_view = view.findViewById(R.id.map_view);

        btn_set_location = view.findViewById(R.id.btn_set_location);
        btn_back = view.findViewById(R.id.btn_back);
        btn_ok = popup_view.findViewById(R.id.btn_ok);
        btn_marker_ok = noMarker_view.findViewById(R.id.btn_ok);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        parentFragmentManager = getParentFragmentManager();
        getLastLocation();
        initGoogleMap(savedInstanceState);

        dialogBuilder.setView(popup_view);
        NoMarkerdialogBuilder.setView(noMarker_view);
        dialog = dialogBuilder.create();
        NoMarkerdialog = NoMarkerdialogBuilder.create();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_marker_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoMarkerdialog.dismiss();
            }
        });

        btn_set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMarkerExist){
                    Bundle result = new Bundle();
                    result.putDouble("latitude", clickMarker.getPosition().latitude);
                    result.putDouble("longitude", clickMarker.getPosition().longitude);

                    parentFragmentManager.setFragmentResult("locationData", result);

                    parentFragmentManager.popBackStack();
                }else{
                    NoMarkerdialog.show();
                }
            }
        });

        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        map_view.onCreate(mapViewBundle);

        map_view.getMapAsync(this);
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();

                    if(location != null){
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                    }else{
                        dialog.show();
                    }

                }else{
                    dialog.show();
                }
            }
        });
        return;
    }

    public void setCurrentLocation(double latitude, double longitude){
        currentUserLat = latitude;
        currentUserLong = longitude;
    }

    private void getLastLocationCamera() {
        if (ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                    if(location != null){
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                    }else{
                        setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LATITUDE);
                    }

                }else{
                    setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LATITUDE);
                }
            }
        });
        return;
    }

    private void setLocationCamera(double latitude, double longitude){
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom
                (new LatLng(latitude, longitude), 18);
        map_instance.moveCamera(point);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            view.getContext(), R.raw.style_json));

            if (!success) {
                Log.e("MAP", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MAP", "Can't find style. Error: ", e);
        }
        map_instance = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        getLastLocationCamera();
        googleMap.setOnMapClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        map_view.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if(!isMarkerExist){
            clickMarker = map_instance.addMarker(new MarkerOptions().position(latLng));
            isMarkerExist = true;
            return;
        }

        isMarkerExist = false;
        clickMarker.remove();

    }

    @Override
    public void onResume() {
        super.onResume();
        map_view.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        map_view.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map_view.onStop();
    }

    @Override
    public void onPause() {
        map_view.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map_view.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_view.onLowMemory();
    }

}