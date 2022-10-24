package com.fourbytes.loc8teapp.fragment.professional;

import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_MapView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.List;

public class FragmentSetLocation_Professional extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private View view;
    private MapView map_view;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private AppCompatButton btn_set_location;
    private AppCompatButton btn_back;
    private AppCompatButton btn_ok;

    private FragmentManager parentFragmentManager;

    private FirebaseFirestore db;
    private GoogleMap map_instance;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private double currentUserLat;
    private double currentUserLong;

    private double clickedLat;
    private double clickedLong;

    private Polyline polyline;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_location_professional, container, false);
        final View popup_view = getLayoutInflater().inflate(R.layout.no_response_popup, null);

        dialogBuilder = new AlertDialog.Builder(view.getContext());
        map_view = view.findViewById(R.id.map_view);
        btn_set_location = view.findViewById(R.id.btn_set_location);
        btn_back = view.findViewById(R.id.btn_back);
        btn_ok = popup_view.findViewById(R.id.btn_ok);

        btn_set_location.setEnabled(false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        parentFragmentManager = getParentFragmentManager();
        getLastLocation();
        initGoogleMap(savedInstanceState);

        dialogBuilder.setView(popup_view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, new FragmentHome_MapView("path"), null)
                        .commit();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

    public void retrieveNodes() {
        map_instance.clear();
        db = FirebaseFirestore.getInstance();
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

                        setNodeMarkers(latitude, longitude);

                    }
                }

            }
        });

    }

    public void setNodeMarkers(double latitude, double longitude){
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .flat(true)
                .icon(BitmapFromVector(view.getContext(), R.drawable.icon_marker_node_green)));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        getLastLocation();

        LatLng position = marker.getPosition();
        String originLat = String.valueOf(currentUserLat);
        String originLong = String.valueOf(currentUserLong);
        double desLat = position.latitude;
        double desLong = position.longitude;

        getDirections(originLat, originLong, String.valueOf(desLat), String.valueOf(desLong));
        return false;
    }

    public void getDirections(String oLat, String oLong, String dLat, String dLong){

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

                            System.out.println(status);
                            if(status.equals("OK")){
                                JSONArray routes = response.getJSONArray("routes");
                                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");
                                System.out.println("SUCCESS");
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

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        retrieveNodes();

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