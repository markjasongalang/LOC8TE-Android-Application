package com.fourbytes.loc8teapp.fragment.client;

import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.fourbytes.loc8teapp.PathVertex;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.UserInfo;
import com.fourbytes.loc8teapp.VertexData;
import com.fourbytes.loc8teapp.VertexInfo;
import com.fourbytes.loc8teapp.VertexMatrix;
import com.fourbytes.loc8teapp.fragment.MarkerTag;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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

public class FragmentHome_MapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private View view;
    private View l;
    private View l2;

    private CheckBox mapViewCheckBox;
    private CheckBox listViewCheckBox;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private Button logoutButton;
    private AppCompatButton btnFind;
    private AppCompatButton btn_ok;
    private AppCompatButton btn_ok2;
    private AppCompatButton btn_ok3;
    private AppCompatButton btn_refresh;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;
    private Boolean isAllFABVisible3;

    private FragmentManager parentFragmentManager;

    private MapView map_view;

    private GoogleMap map_instance;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private DistanceMatrix matrix;

    private final double CAMERA_DEFAULT_LATITUDE = 14.603760;
    private final double CAMERA_DEFAULT_LONGITUDE = 120.989200;
    private final double BOUNDING_BOX_LAT1 = 14.600165;
    private final double BOUNDING_BOX_LONG1 = 120.984364;
    private final double BOUNDING_BOX_LAT2 = 14.607744;
    private final double BOUNDING_BOX_LONG2 = 120.993569;

    private double currentUserLat = 0;
    private double currentUserLong = 0;

    private boolean isGPSEnabled = false;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private String path;
    private String parse;
    private boolean isVertexInit = false;

    private Marker currentLocationMarker;
    private Polyline polyline;

    private VertexMatrix vertex;

    private int checkCount = 0;

    private final int width = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    public FragmentHome_MapView(String path) {
        this.path = path;
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
        l = view.findViewById(R.id.home_settings_toolbar);
        l2 = view.findViewById(R.id.location_settings_toolbar);
        logoutButton = view.findViewById(R.id.logout);
        btnFind = view.findViewById(R.id.btn_find);
        btn_refresh = view.findViewById(R.id.btn_refresh);
        map_view = view.findViewById(R.id.map_view);

        // Get parent fragment manager (from host activity)
        parentFragmentManager = getParentFragmentManager();

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);
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
                getLastLocation();
                if(isGPSEnabled){
                    System.out.println("GPS is activated");
                    findNearestUser();

                }else{
                    showGPSPopUp();
                }

                Toast.makeText(view.getContext(), "Find is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshMap();
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        initGoogleMap(savedInstanceState);
        initVertices();
        return view;
    }

    public void initVertices(){
        try {
            vertex = new VertexMatrix(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findNearestUser(){
        map_instance.clear();
        setCurrentLocationMarker(currentUserLat, currentUserLong);

        setNodeMarkers(vertex.getDistanceLatLong(currentUserLat, currentUserLong));

    }

    public void setNodeMarkers(List<VertexInfo> vertexMarker){

        for(int i = 0; i < vertexMarker.size(); i++){

            map_instance.addMarker(new MarkerOptions()
                    .position(new LatLng(vertexMarker.get(i).getLatitude(), vertexMarker.get(i).getLongitude()))
                    .title("Start Here")
                    .icon(BitmapFromVector(view.getContext(), R.drawable.icon_marker_node_green)))
                    .setTag(new MarkerTag(vertexMarker.get(i).getId(), "vertex"));
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


//    public void findNearestUser(){
//        getLastLocation();
//        //Check if current location is supported
//        if(!isLocationSupported()){
//            return;
//        }
//        refreshMap();
//        ArrayList<UserInfo> Users = new ArrayList<>();
//        db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                try {
//                                    double latitude = document.getDouble("lat");
//                                    double longitude = document.getDouble("long");
//                                    String userId = document.getId();
//
//                                    Users.add(new UserInfo(
//                                            userId,
//                                            longitude,
//                                            latitude
//                                    ));
//                                } catch (Exception e) {
//                                    Log.d("USER", document.getId() + " is Invalid User Format");
//                                    e.printStackTrace();
//                                }
//                            }
//                            //snapVertices(Users);
//                            getDistanceMatrix(Users);
//                        } else {
//                            showNoResponsePopUp();
//                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }

//    public void snapVertices(ArrayList<UserInfo> Users){
//
//        for(int i = 0; i < Users.size(); i++){
//            double latitude = Users.get(i).getLatitude();
//            double longitude = Users.get(i).getLongitude();
//
//            List<VertexInfo> distanceLongLat = vertex.getDistanceLatLong(latitude, longitude);
//
//            System.out.println(Users.get(i).getId() + "---------------------------------------");
//
//            getDistanceMatrix(distanceLongLat, latitude, longitude, Users.size());
//        }
//    }
//
//    public void snapCurrentUserVertex(){
//
//
//    }

//    public void getDistanceMatrix(List<VertexInfo> distanceLongLat, double latitude, double longitude, int user_count){
//        String origin = "";
//        String destination = "";
//        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
//        final String API_KEY = getString(R.string.google_maps_api_key);
//
//        if(isGPSEnabled){
//            destination = latitude + "," + longitude;
//
//            for(int i = 0; i < distanceLongLat.size(); i++){
//                origin += distanceLongLat.get(i).getLatitude() + "," + distanceLongLat.get(i).getLongitude() + "|";
//            }
//        }else{
//            showGPSPopUp();
//            return;
//        }
//
//        String url = Uri.parse("https://maps.googleapis.com/maps/api/distancematrix/json")
//                .buildUpon()
//                .appendQueryParameter("destinations", destination)
//                .appendQueryParameter("origins", origin)
//                .appendQueryParameter("mode", "driving")
//                .appendQueryParameter("key", API_KEY)
//                .toString();
//
//
//        String finalOrigin = origin;
//        String finalDestination = destination;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            String status = response.getString("status");
//                            System.out.println(status);
//                            if(status.equals("OK")){
//                                System.out.println(finalDestination);
//                                checkNearestNode(response, distanceLongLat);
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        showNoResponsePopUp();
//                        error.printStackTrace();
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    public void checkNearestNode(JSONObject response, List<VertexInfo> distanceLongLat) throws JSONException {
//        ArrayList<Edge> edge = vertex.getEdges();
//        JSONArray rows = response.getJSONArray("rows");
//
//        for(int i = 0; i < rows.length(); i++){
//            JSONArray elements = rows.getJSONObject(i).getJSONArray("elements");
//            JSONObject distance = elements.getJSONObject(0).getJSONObject("distance");
//            double distance_parsed = Double.parseDouble(String.valueOf(distance.get("value")));
//
//            distanceLongLat.get(i).setDistance(distance_parsed);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            distanceLongLat.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
//        }
//
//        for(int i = distanceLongLat.size() - 1; i > 0; i --){
//            System.out.println(distanceLongLat.get(i).getId());
//            System.out.println(distanceLongLat.get(i).getDistance());
//
//            for (int j = i - 1; j >= 0; j--){
//
//                //TODO: finish tom
//
//            }
//        }
//
//    }

    public void getDistanceMatrix(ArrayList<UserInfo> Users){
        String origin = "";
        String destination = "";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        final String API_KEY = getString(R.string.google_maps_api_key);

        if(isGPSEnabled){
            origin = currentUserLat + "," + currentUserLong;

            if(Users.size() == 0){
                //TODO: Show no users found popup

                return;
            }else{

                for(int i = 0; i < Users.size(); i++){
                    destination += Users.get(i).getLatitude() + "," + Users.get(i).getLongitude() + "|";
                }
            }
        }else{
            showGPSPopUp();
            return;
        }

        String url = Uri.parse("https://maps.googleapis.com/maps/api/distancematrix/json")
                .buildUpon()
                .appendQueryParameter("destinations", destination)
                .appendQueryParameter("origins", origin)
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", API_KEY)
                .toString();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            //System.out.println(status);
                            if(status.equals("OK")){
                                checkNearestUser(response, Users);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showNoResponsePopUp();
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void checkNearestUser(JSONObject response, ArrayList<UserInfo> Users) throws JSONException {
        JSONArray elements = response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

        //System.out.println(elements);
        //System.out.println(elements.length());
        for(int i = 0; i < elements.length(); i++){
            JSONObject distance = elements.getJSONObject(i).getJSONObject("distance");
            double distance_parsed = Double.parseDouble(String.valueOf(distance.get("value")));

            Users.get(i).setDistance(distance_parsed);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Users.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
        }
        String destinationLat = String.valueOf(Users.get(0).getLatitude());
        String destinationLong = String.valueOf(Users.get(0).getLongitude());
        //System.out.println(Users.get(0).getId() + " " + Users.get(0).getDistance());

        getDirections(destinationLat, destinationLong);

    }

    public void drawPolyline(JSONArray routes) throws JSONException {
        if(polyline != null){
            polyline.remove();
        }

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

            polyline = map_instance.addPolyline(polylineOptions);

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

                    try {
                        Location location = task.getResult();
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                        if(polyline != null){
                            polyline.remove();
                        }

                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocationMarker(geopoint.getLatitude(), geopoint.getLongitude());
                        isGPSEnabled = true;
                    }catch (Exception e){
                        showGPSPopUp();
                        setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LONGITUDE);
                        e.printStackTrace();
                    }

                }else{
                    isGPSEnabled = false;
                }
            }
        });
    }

    public void refreshMap(){
        map_instance.clear();
        retrieveUsers();
        getLastLocation();

    }

    private void setCurrentLocationMarker(double latitude, double longitude){
        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        String id = "current";
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_client_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        currentLocationMarker = map_instance.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .title("You are Here").icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

    }

    private boolean isLocationSupported(){

        if(BOUNDING_BOX_LAT1 <= currentUserLat
            && currentUserLat <= BOUNDING_BOX_LAT2
            && BOUNDING_BOX_LONG1 <= currentUserLong
            && currentUserLong <= BOUNDING_BOX_LONG2){
            System.out.println("SUPPORTED");
            return true;
        }else{
            showNoServicePopUp();
            return false;
        }
    }

    public void showNoServicePopUp(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View noGPS_view = getLayoutInflater().inflate(R.layout.no_service_popup, null);

        btn_ok3 = noGPS_view.findViewById(R.id.btn_ok);
        dialogBuilder.setView(noGPS_view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_ok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showGPSPopUp(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View noGPS_view = getLayoutInflater().inflate(R.layout.no_gps_popup, null);

        btn_ok = noGPS_view.findViewById(R.id.btn_ok);
        dialogBuilder.setView(noGPS_view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void showNoResponsePopUp(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View noResponse_view = getLayoutInflater().inflate(R.layout.no_response_popup, null);

        btn_ok2 = noResponse_view.findViewById(R.id.btn_ok);
        dialogBuilder.setView(noResponse_view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

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
                    try {
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                    }catch (Exception e){
                        setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LONGITUDE);
                    }

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

        if(!isVertexInit){
            retrieveNodes();
        }
        retrieveUsers();
        getLastLocation();
        googleMap.setOnMarkerClickListener(this);
    }

    public void getDirections(String destinationLat, String destinationLong){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        final String API_KEY = getString(R.string.google_maps_api_key);

        String origin = currentUserLat + ", " + currentUserLong;
        String destination = destinationLat + ", " + destinationLong;
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

                                drawPolyline(routes);
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
        String TAG = "MAP USERS";
        ArrayList<VertexInfo> U = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("professionals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<String> meet_point_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try{
                                    if(document.getString("meet_point") != null){
                                        double latitude = document.getDouble("latitude");
                                        double longitude = document.getDouble("longitude");
                                        String id = document.getId();
                                        String meet_point = document.getString("meet_point");
                                        String fname = document.getString("first_name");
                                        String lname = document.getString("last_name");
                                        String name = fname + " " +lname;

                                        setMarkers(latitude, longitude, 0, name, id);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                try{
//                                    double latitude = document.getDouble("lat");
//                                    double longitude = document.getDouble("long");
//                                    String id = document.getId();
//                                    String name = document.getString("first");
//                                    U.add(new VertexInfo(id, longitude, latitude));
//                                    setMarkers(latitude, longitude, 0, name, id);
//                                }catch (Exception e){
//                                    Log.d("NODES",  document.getId());
//                                }
//
//                            }
//
//                            initUsers(U);
//                        } else {
//                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
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

    public void setMarkers(double latitude, double longitude, double filter, String name, String id) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pro_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))).setTag(new MarkerTag(id, "users"));

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        MarkerTag tag = (MarkerTag) marker.getTag(); //Gets the object to retrieve id/infos

        if(tag != null){
            String type = tag.getType();
            String name = marker.getTitle();
            String id = tag.getId();

            Toast.makeText(view.getContext(), name + "is clicked", Toast.LENGTH_SHORT).show();
            if(type.equals("users")){
                // Open Profile Fragment here
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentProfile_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }else{
                LatLng marker_pos = marker.getPosition();
                getDirections(String.valueOf(marker_pos.latitude), String.valueOf(marker_pos.longitude));

                View node_onclick_view = getLayoutInflater().inflate(R.layout.node_onclick_popup, null);
                AppCompatButton btn_search = node_onclick_view.findViewById(R.id.btn_search);
                final PopupWindow popupWindow = new PopupWindow(node_onclick_view, width, height, true);
                popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retrieveProfessionals(id);
                        popupWindow.dismiss();
                    }
                });
            }

        }

        return false;
    }

    public void retrieveProfessionals(String start_point){
        ArrayList<UserInfo> Users = new ArrayList<>();
        db.collection("professionals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                        ArrayList<String> meet_point_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try{
                                    if(document.getString("meet_point") != null){
                                        double latitude = document.getDouble("latitude");
                                        double longitude = document.getDouble("longitude");
                                        String id = document.getId();
                                        String meet_point = document.getString("meet_point");
                                        String fname = document.getString("first_name");
                                        String lname = document.getString("last_name");

                                        String name = fname + " " +lname;
                                        Users.add(new UserInfo(
                                                id,
                                                longitude,
                                                latitude,
                                                meet_point,
                                                name
                                        ));
                                        if(!meet_point_list.contains(meet_point)){
                                            meet_point_list.add(meet_point);
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                            if(!meet_point_list.isEmpty()){
                                searchProfessionals(start_point, meet_point_list, Users);
                            }

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void searchProfessionals(String start_point, ArrayList<String> meet_point_list, ArrayList<UserInfo> Users){
        double finalLat;
        double finalLong;
        String meet_point;
        ArrayList<String> meet_point_list_filtered = new ArrayList<>();
        HashMap<String, VertexData> vertices = vertex.getVertex();

        HashMap<String, PathVertex> path = vertices.get(start_point).getPath();

        ArrayList<String> nearest_path = new ArrayList<>();

        for (int i = 0; i < meet_point_list.size(); i++){
            if(path.get(meet_point_list.get(i)) != null || start_point.equals(meet_point_list.get(i))){
                meet_point_list_filtered.add(meet_point_list.get(i));

                if(start_point.equals(meet_point_list.get(i))){
                    System.out.println("True");
                    map_instance.clear();

                    finalLat = vertices.get( meet_point_list.get(i)).getLatitude();
                    finalLong = vertices.get( meet_point_list.get(i)).getLongitude();

                    setCurrentLocationMarker(currentUserLat, currentUserLong);
                    getDirections(String.valueOf(vertices.get(start_point).getLatitude()), String.valueOf(vertices.get(start_point).getLongitude()));
                    setDestinationMarker(finalLat, finalLong);

                    getUserMeetPoint(Users, start_point);

                    return;
                }
            }else{
                System.out.println("No path for " + meet_point_list.get(i));
            }

        }

        System.out.println("Meet points to check: " + meet_point_list_filtered.size());
        double nearest_distance = path.get(meet_point_list_filtered.get(0)).getDistance();
        nearest_path = path.get(meet_point_list_filtered.get(0)).getPath();
        for(int i = 0; i <  meet_point_list_filtered.size() - 1; i++){

            double check_distance = path.get(meet_point_list_filtered.get(i + 1)).getDistance();
            System.out.println(nearest_distance);
            System.out.println(check_distance);
            if(nearest_distance > check_distance){
                System.out.println("new distance is: "+ check_distance);
                nearest_path = path.get(meet_point_list_filtered.get(i + 1)).getPath();
            }

        }

        map_instance.clear();
        setCurrentLocationMarker(currentUserLat, currentUserLong);
        getDirections(String.valueOf(vertices.get(start_point).getLatitude()), String.valueOf(vertices.get(start_point).getLongitude()));
        for(int i = 0; i < nearest_path.size() - 1; i++){
            String originLat = String.valueOf(vertices.get(nearest_path.get(i)).getLatitude());
            String originLong = String.valueOf(vertices.get(nearest_path.get(i)).getLongitude());
            String destinationLat = String.valueOf(vertices.get(nearest_path.get(i + 1)).getLatitude());
            String destinationLong = String.valueOf(vertices.get(nearest_path.get(i + 1)).getLongitude());

            getNodeDirection(destinationLat, destinationLong, originLat, originLong);
        }

        meet_point = nearest_path.get(nearest_path.size()-1);
        finalLat = vertices.get(nearest_path.get(nearest_path.size()-1)).getLatitude();
        finalLong = vertices.get(nearest_path.get(nearest_path.size()-1)).getLongitude();
        getUserMeetPoint(Users, meet_point);
        setDestinationMarker(finalLat, finalLong);

    }

    public void getUserMeetPoint(ArrayList<UserInfo> Users, String meet_point){

        for(int i = 0; i < Users.size(); i++){

            if (Users.get(i).getMeet_point().equals(meet_point)){
                setMarkers(Users.get(i).getLatitude(), Users.get(i).getLongitude(), 0,Users.get(i).getName() ,Users.get(i).getId());
            }
        }

    }

    public void setDestinationMarker(double finalLat, double finalLong){
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(finalLat, finalLong))
                .title("Nearest Meet Point")
                .icon(BitmapFromVector(view.getContext(), R.drawable.icon_meetpoint)));
    }

    public void getNodeDirection(String destinationLat, String destinationLong, String originLat, String originLong){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        final String API_KEY = getString(R.string.google_maps_api_key);

        String origin = originLat + ", " + originLong;
        String destination = destinationLat + ", " + destinationLong;
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

                                drawMultiplePolyLine(routes);
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

    public void drawMultiplePolyLine(JSONArray routes) throws JSONException {
        ArrayList<LatLng> points;
        PolylineOptions polylineOptions = null;

        for (int i=0;i<routes.length();i++){
            points = new ArrayList<>();

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
            polylineOptions.width(5);
            polylineOptions.color(getResources().getColor(R.color.secondaryColor));
            polylineOptions.geodesic(true);

            map_instance.addPolyline(polylineOptions);

        }
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

//        search_prof_FAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isAllFABVisible3) {
//                    l3.setVisibility(view.VISIBLE);
//                    isAllFABVisible3 = true;
//                } else {
//                    l3.setVisibility(view.GONE);
//                    isAllFABVisible3 = false;
//                }
//            }
//        });

//    public void getUserDistanceRoad(){
//        ArrayList<Vertex> userDistance = new ArrayList<>();
//        ArrayList<VertexInfo> userEdge;
//        userDistance = matrix.getUserDistance();
//        for(int i = 0; i < userDistance.size(); i++){
//            userEdge = userDistance.get(i).getEdge();
//
//            String desId = userDistance.get(i).getId();
//            String desLong = String.valueOf(userDistance.get(i).getLongitude());
//            String desLat = String.valueOf(userDistance.get(i).getLatitude());
//
//            System.out.println(desId);
//            for(int j = 0; j < userEdge.size(); j++){
//                String originId = userEdge.get(j).getId();
//                String originLong = String.valueOf(userEdge.get(j).getLongitude());
//                String originLat = String.valueOf(userEdge.get(j).getLatitude());
//
//                getDirections(originLat, originLong, desLat, desLong, originId, desId);
//            }
//        }
//    }

//    public void getUserDistance(){
//
//        ArrayList<VertexInfo> U = new ArrayList<>();
//        db = FirebaseFirestore.getInstance();
//
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                try{
//                                    double latitude = document.getDouble("lat");
//                                    double longitude = document.getDouble("long");
//                                    String id = document.getId();
//                                    String name = document.getString("first");
//                                    U.add(new VertexInfo(id, longitude, latitude));
//                                }catch (Exception e){
//                                    Log.d("NODES",  document.getId());
//                                }
//
//                            }
//
//                            matrix.getDistanceLongLat(U);
//                            getUserDistanceRoad();
//                        } else {
//                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }