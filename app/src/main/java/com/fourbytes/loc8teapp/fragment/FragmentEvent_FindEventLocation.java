package com.fourbytes.loc8teapp.fragment;

import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourbytes.loc8teapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentEvent_FindEventLocation extends Fragment implements OnMapReadyCallback {

    private View view;
    private MapView map_view;
    private AppCompatButton btnBack;
    private FirebaseFirestore db;
    private GoogleMap map_instance;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentManager fragmentManager;

    private final double CAMERA_DEFAULT_LATITUDE = 14.603760;
    private final double CAMERA_DEFAULT_LONGITUDE = 120.989200;

    private String destinationLat;
    private String destinationLong;

    private double currentUserLat;
    private double currentUserLong;
    public FragmentEvent_FindEventLocation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_find_location, container, false);
        map_view = view.findViewById(R.id.map_view);
        btnBack = view.findViewById(R.id.btn_back);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        fragmentManager = getParentFragmentManager();

        fragmentManager.setFragmentResultListener("locationData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                destinationLat = String.valueOf(result.getDouble("destinationLatitude"));
                destinationLong = String.valueOf(result.getDouble("destinationLongitude"));
                getLastLocation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        initGoogleMap(savedInstanceState);

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
                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                        getDirections();
                    }else{
                        setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LONGITUDE);
                    }

                }else{
                    setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LONGITUDE);
                }
            }
        });
        return;
    }

    private void setCurrentLocationMarker(){
        String id = "current";
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_client_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        map_instance.addMarker(new MarkerOptions().position(new LatLng(currentUserLat, currentUserLong))
                .title("You are Here").icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

        map_instance.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(destinationLat), Double.parseDouble(destinationLong))));

    }

    public void setCurrentLocation(double latitude, double longitude){
        currentUserLat = latitude;
        currentUserLong = longitude;
    }

    public void getDirections(){
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
                            Log.d("EVENT", String.valueOf(response));

                            if(status.equals("OK")){
                                JSONArray routes = response.getJSONArray("routes");
                                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");

                                Log.d("EVENT", String.valueOf(routes));
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

    public void drawPolyline(JSONArray routes) throws JSONException {
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

            if(i == routes.length()-1){
                points.add(new LatLng(Double.parseDouble(destinationLat), Double.parseDouble(destinationLong)));
            }

            polylineOptions.addAll(points);
            polylineOptions.width(10);
            polylineOptions.color(getResources().getColor(R.color.secondaryColor));
            polylineOptions.geodesic(true);

            map_instance.addPolyline(polylineOptions);

            setCurrentLocationMarker();
        }

    }

    private void setLocationCamera(double latitude, double longitude){
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom
                (new LatLng(latitude, longitude), 18);
        map_instance.moveCamera(point);
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