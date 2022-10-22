package com.fourbytes.loc8teapp.fragment.client;

import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourbytes.loc8teapp.DistanceMatrix;
import com.fourbytes.loc8teapp.Edge;
import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.UserDistance;
import com.fourbytes.loc8teapp.Vertex;
import com.fourbytes.loc8teapp.VertexInfo;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.internal.JsonParser;

public class FragmentHome_MapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private View view;
    private View l;
    private View l2;
    private View l3;

    private CheckBox mapViewCheckBox;
    private CheckBox listViewCheckBox;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private FloatingActionButton search_prof_FAB;

    private Button logoutButton;
    private AppCompatButton btnFind;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;
    private Boolean isAllFABVisible3;

    private FragmentManager parentFragmentManager;

    private MapView map_view;

    private Location location;

    private GoogleMap map_instance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private DistanceMatrix matrix;

    private ArrayList<UserDistance> userDistanceList;

    private final double CAMERA_DEFAULT_LATITUDE = 14.603760;
    private final double CAMERA_DEFAULT_LONGITUDE = 120.989200;

    private double currentUserLat = 0;
    private double currentUserLong = 0;

    private int userCount = 0;
    private int checkCount = 0;
    public FragmentHome_MapView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_map_view, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        // Get views from layout
        mapViewCheckBox = view.findViewById(R.id.map_view_checkbox);
        listViewCheckBox = view.findViewById(R.id.list_view_checkbox);
        home_settings_FAB = view.findViewById(R.id.home_settings);
        location_settings_FAB = view.findViewById(R.id.location_settings);
        search_prof_FAB = view.findViewById(R.id.search_prof_button);
        l = view.findViewById(R.id.home_settings_toolbar);
        l2 = view.findViewById(R.id.location_settings_toolbar);
        l3 = view.findViewById(R.id.search_prof_field);
        logoutButton = view.findViewById(R.id.logout);
        btnFind = view.findViewById(R.id.btn_find);
        map_view = view.findViewById(R.id.map_view);

        // Get parent fragment manager (from host activity)
        parentFragmentManager = getParentFragmentManager();

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);
        l3.setVisibility(view.GONE);
        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        mapViewCheckBox.setChecked(true);
        mapViewCheckBox.setEnabled(false);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNearestUser();
                //getUserDistance();
                Toast.makeText(view.getContext(), "Find is clicked", Toast.LENGTH_SHORT).show();

            }
        });
        listViewCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mapViewCheckBox.setChecked(false);
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentHome_ListView.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

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

        search_prof_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible3) {
                    l3.setVisibility(view.VISIBLE);
                    isAllFABVisible3 = true;
                } else {
                    l3.setVisibility(view.GONE);
                    isAllFABVisible3 = false;
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
                matrix.printUserDistance();
            }
        });

        initGoogleMap(savedInstanceState);

        return view;
    }

    public void findNearestUser(){

        userDistanceList = new ArrayList<>();
        getLastLocation();

        //retrieve users
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try{
                                    double latitude = document.getDouble("lat");
                                    double longitude = document.getDouble("long");
                                    String findId = document.getId();

                                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                    final String API_KEY = getString(R.string.google_maps_api_key);

                                    String origin = currentUserLat + ", " + currentUserLong;
                                    String destination = latitude + ", " + longitude;
                                    String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                                            .buildUpon()
                                            .appendQueryParameter("destination", destination)
                                            .appendQueryParameter("origin", origin)
                                            .appendQueryParameter("mode", "driving")
                                            .appendQueryParameter("key", API_KEY)
                                            .toString();


                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try{
                                                        String status = response.getString("status");

                                                        if(status.equals("OK")){

                                                            JSONArray routes = response.getJSONArray("routes");
                                                            JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                                            JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");
                                                            double distance_parsed = Double.parseDouble(String.valueOf(distance.get("value")));

                                                            Log.d("DISTANCE", "ADDED" + routes);
                                                            userDistanceList.add(new UserDistance(
                                                                    routes,
                                                                    distance_parsed
                                                            ));
                                                            System.out.println(userCount);
                                                            if(userCount == userDistanceList.size()){
                                                                try {
                                                                    drawPolyline();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
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

                                }catch (Exception e){
                                    Log.d("NODES",  document.getId());
                                }

                            }

                            System.out.println(userDistanceList.size());

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    public void drawPolyline() throws JSONException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            userDistanceList.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
        }

        System.out.println(userDistanceList.size());

        JSONArray routes = userDistanceList.get(0).getRoutes();

        ArrayList<LatLng> points;
        PolylineOptions polylineOptions = null;

        for (int i=0;i<routes.length();i++){
            points = new ArrayList<>();
            if(i == 0){
                points.add(new LatLng(currentUserLat, currentUserLong));
            }
            polylineOptions = new PolylineOptions();
            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

            for (int j=0;j<legs.length();j++){
                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                for (int k=0;k<steps.length();k++){
                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                    List<LatLng> list = PolyUtil.decode(polyline);

                    for (int l=0;l<list.size();l++){
                        LatLng position = new LatLng((list.get(l)).latitude, (list.get(l)).longitude);
                        points.add(position);
                    }
                }
            }
            polylineOptions.addAll(points);
            polylineOptions.width(10);
            polylineOptions.color(getResources().getColor(R.color.secondaryColor));
            polylineOptions.geodesic(true);

            map_instance.addPolyline(polylineOptions);

        }

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
                    GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
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
                    GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                }
            }
        });
        return;
    }

    private void setLocationCamera(double latitude, double longitude){
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom
                (new LatLng(latitude, longitude), 20);
        map_instance.moveCamera(point);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map_instance = googleMap;
        retrieveNodes();
        retrieveUsers();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getLastLocationCamera();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
    }

    public void getUserDistanceRoad(){
        ArrayList<Vertex> userDistance = new ArrayList<>();
        ArrayList<VertexInfo> userEdge;
        userDistance = matrix.getUserDistance();
        for(int i = 0; i < userDistance.size(); i++){
            userEdge = userDistance.get(i).getEdge();

            String desId = userDistance.get(i).getId();
            String desLong = String.valueOf(userDistance.get(i).getLongitude());
            String desLat = String.valueOf(userDistance.get(i).getLatitude());

            System.out.println(desId);
            for(int j = 0; j < userEdge.size(); j++){
                String originId = userEdge.get(j).getId();
                String originLong = String.valueOf(userEdge.get(j).getLongitude());
                String originLat = String.valueOf(userEdge.get(j).getLatitude());

                getDirections(originLat, originLong, desLat, desLong, originId, desId);
            }
        }
    }

    public void getUserDistance(){

        ArrayList<VertexInfo> U = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try{
                                    double latitude = document.getDouble("lat");
                                    double longitude = document.getDouble("long");
                                    String id = document.getId();
                                    String name = document.getString("first");
                                    U.add(new VertexInfo(id, longitude, latitude));
                                }catch (Exception e){
                                    Log.d("NODES",  document.getId());
                                }

                            }

                            matrix.getDistanceLongLat(U);
                            getUserDistanceRoad();
                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void getDirections(String oLat, String oLong, String dLat, String dLong, String originId, String desId){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        final String API_KEY = getString(R.string.google_maps_api_key);

        String origin = oLat + ", " + oLong;
        String destination = dLat + ", " + dLong;
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", destination)
                .appendQueryParameter("origin", origin)
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", API_KEY)
                .toString();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");


                            if(status.equals("OK")){
                                JSONArray routes = response.getJSONArray("routes");
                                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");

                                String distance_parsed = String.valueOf(distance.get("value"));
//                                matrix.setUserEdgeValue(originId, desId, Double.parseDouble(distance_parsed));
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

    public void retrieveUsers() {
        String lat;
        int longtitude;
        String TAG = "MAP USERS";
        userCount = 0;
        ArrayList<VertexInfo> U = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try{
                                    double latitude = document.getDouble("lat");
                                    double longitude = document.getDouble("long");
                                    String id = document.getId();
                                    String name = document.getString("first");
                                    U.add(new VertexInfo(id, longitude, latitude));
                                    userCount++;
                                    setMarkers(latitude, longitude, 0, name, id);
                                }catch (Exception e){
                                    Log.d("NODES",  document.getId());
                                }

                            }

                            initUsers(U);
                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void retrieveNodes() {
        String TAG = "MAP Markers";
        db = FirebaseFirestore.getInstance();
        ArrayList<VertexInfo> V = new ArrayList<>();
        ArrayList<Edge> E = new ArrayList<>();
        db.collection("vertex").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                int count = 0;
                if (e != null) {

                    return;
                }

                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {
                            double latitude = document.getDouble("lat");
                            double longitude = document.getDouble("long");

                            V.add(new VertexInfo(
                                    document.getId(),
                                    longitude,
                                    latitude
                            ));
                    }
                }

                initMatrix(V);
            }
        });

        db.collection("edges").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                int count = 0;
                if (e != null) {
                    return;
                }

                for (QueryDocumentSnapshot document : value) {
                    if (document != null) {

                        try{
                            String id = document.getId();
                            String origin = document.getString("start");
                            String destination = document.getString("end");
                            double distance = document.getDouble("distance");
                            E.add(new Edge(origin, destination, distance, id));

                        }catch (Exception error){
                            Log.d("ERROR", document.getId());
                        }

                    }
                }

                initEdges(E);
            }
        });
    }

    public void initMatrix(ArrayList<VertexInfo> V){
        matrix = new DistanceMatrix(V);
    }

    public void initEdges(ArrayList<Edge> E){
        matrix.initEdgeValue(E);
    }

    public void initUsers(ArrayList<VertexInfo> U){
        matrix.initUsers(U);
    }

//    public void setNodeMarkers(double latitude, double longitude){
//        map_instance.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude)).title("nodes"));
//    }

    public void setMarkers(double latitude, double longitude, double filter, String name, String id) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.juswa_hearts);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 100, 100, false);
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))).setTag(new MarkerTag(id));

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        MarkerTag tag = (MarkerTag) marker.getTag(); //Gets the object to retrieve id/infos

        String TAG = "Marker";
        String name = marker.getTitle();
        String id = tag.getId(); //id from database documents("users")

        // Delete this later
        Toast.makeText(view.getContext(), name + "is clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, id);

        // Open Profile Fragment here
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment, FragmentProfile_Professional.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

        return false;
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