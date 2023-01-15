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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.fourbytes.loc8teapp.PathVertex;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.VertexData;
import com.fourbytes.loc8teapp.VertexInfo;
import com.fourbytes.loc8teapp.VertexMatrix;
import com.fourbytes.loc8teapp.fragment.MarkerTag;
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_MapView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FragmentEvent_Realtime extends Fragment implements OnMapReadyCallback{
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

    private Marker currentLocationMarker;

    private FragmentManager parentFragmentManager;

    private FirebaseFirestore db;
    private GoogleMap map_instance;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private double currentUserLat;
    private double currentUserLong;
    private double desLat;
    private double desLong;

    private final double CAMERA_DEFAULT_LATITUDE = 14.603760;
    private final double CAMERA_DEFAULT_LONGITUDE = 120.989200;

    private VertexMatrix vertex;

    private Marker clickMarker;

    private boolean isMarkerSet = false;
    private boolean isVertices = false;
    private boolean isGPSEnabled = false;
    private boolean isDesination = false;
    private boolean terminate = false;

    private Polyline polyline;
    private Polyline dotted_polyline_start;
    private Polyline dotted_polyline_end;

    LocationCallback mLocationCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_realtime_location, container, false);

        map_view = view.findViewById(R.id.map_view);

        btn_back = view.findViewById(R.id.btn_back);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        parentFragmentManager = getParentFragmentManager();
        initGoogleMap(savedInstanceState);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminate = true;
            }
        });

        parentFragmentManager.setFragmentResultListener("realtimeData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                desLat = result.getDouble("desLat");
                desLong = result.getDouble("desLong");

                isDesination = true;
            }
        });

        initVertices();
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

    public void initVertices() {
        try {
            isVertices = true;
            vertex = new VertexMatrix(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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

    public void startLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    isGPSEnabled = false;
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        isGPSEnabled = true;
                        currentUserLat = geopoint.getLatitude();
                        currentUserLong = geopoint.getLongitude();
                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocationMarker(geopoint.getLatitude(), geopoint.getLongitude());

                        if(isVertices && isDesination){
                            realtimePath();
                        }

                        if(terminate){
                            parentFragmentManager.popBackStack();

                        }
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.getFusedLocationProviderClient(view.getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    public void realtimePath(){
        List<VertexInfo> origin_vertex = vertex.getDistanceLatLong(currentUserLat, currentUserLong);
        List<VertexInfo> destination_vertex = vertex.getDistanceLatLong(desLat, desLong);
        if(origin_vertex.size() != 0 && destination_vertex.size() != 0) {

            if(!origin_vertex.get(0).getId().equals("vertex3")
            && !destination_vertex.equals("vertex3")){

                String origin_point = origin_vertex.get(0).getId();
                String destination_point = destination_vertex.get(0).getId();

                System.out.println(origin_point);
                System.out.println(destination_point);
                startPathing(origin_point, destination_point);
            }

        }
    }

    public void startPathing(String origin_point, String destination_point){

        List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(3), new Dot());
        if(polyline!= null){
            polyline.remove();
        }
        if(dotted_polyline_start!= null){
            dotted_polyline_start.remove();
        }
        if(dotted_polyline_end!= null){
            dotted_polyline_end.remove();
        }

        if (!isMarkerSet){
            setMarker(desLat, desLong);
            isMarkerSet = true;
        }


        if(origin_point.equals(destination_point)){

        }else{
            HashMap<String, VertexData> data = vertex.getVertex();
            HashMap<String, PathVertex> origin_path = data.get(origin_point).getPath();

            ArrayList<LatLng> points = new ArrayList<>();
            ArrayList<LatLng> dotted_points1 = new ArrayList<>();
            ArrayList<LatLng> dotted_points2 = new ArrayList<>();
            PolylineOptions polylineOptions = new PolylineOptions();
            PolylineOptions polylineOptionsDotted1 = new PolylineOptions();
            PolylineOptions polylineOptionsDotted2 = new PolylineOptions();
            ArrayList<String> final_path = origin_path.get(destination_point).getPath();
            System.out.println();
            int path_last_index = final_path.size() - 1;

            double start_point_latitude = data.get(origin_point).getLatitude();
            double start_point_longitude = data.get(origin_point).getLongitude();
            dotted_points1.add(new LatLng(currentUserLat, currentUserLong));
            dotted_points1.add(new LatLng(start_point_latitude, start_point_longitude));

            polylineOptionsDotted1.addAll(dotted_points1);
            polylineOptionsDotted1.width(10);
            polylineOptionsDotted1.geodesic(true);

            dotted_polyline_start = map_instance.addPolyline(polylineOptionsDotted1);

            dotted_polyline_start.setPattern(pattern);
            dotted_polyline_start.setZIndex(1);
            dotted_polyline_start.setColor(getResources().getColor(R.color.primaryColor));

            for (int i = 0; i < final_path.size(); i++) {
                points.add(new LatLng(
                        data.get(final_path.get(i)).getLatitude(),
                        data.get(final_path.get(i)).getLongitude()
                ));
            }

            dotted_points2.add(new LatLng(data.get(destination_point).getLatitude(), data.get(destination_point).getLongitude()));
            dotted_points2.add(new LatLng(desLat, desLong));

            polylineOptionsDotted2.addAll(dotted_points2);
            polylineOptionsDotted2.width(10);
            polylineOptionsDotted2.geodesic(true);

            dotted_polyline_end = map_instance.addPolyline(polylineOptionsDotted2);
            dotted_polyline_end.setPattern(pattern);
            dotted_polyline_end.setZIndex(1);
            dotted_polyline_end.setColor(getResources().getColor(R.color.primaryColor));

            polylineOptions.addAll(points);
            polylineOptions.width(5);
            polylineOptions.color(getResources().getColor(R.color.secondaryColor));
            polylineOptions.geodesic(true);

            polyline = map_instance.addPolyline(polylineOptions);
            polyline.setZIndex(0);
        }

    }

    private void setLocationCamera(double latitude, double longitude) {
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom
                (new LatLng(latitude, longitude), 18);
        map_instance.animateCamera(point);
    }

    private void setCurrentLocationMarker(double latitude, double longitude) {
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        String id = "current";
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_client_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        currentLocationMarker = map_instance.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .title("You are Here").icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

    }

    public void setMarker(double latitude, double longitude) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pro_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Destination").icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

    }


    @Override
    public void onResume() {
        super.onResume();
        map_view.onResume();
        startLocationRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        map_view.onStart();
        startLocationRequest();
    }

    @Override
    public void onStop() {
        startLocationRequest();
        map_view.onStop();
        super.onStop();

    }

    @Override
    public void onPause() {
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        map_view.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        map_view.onDestroy();
        super.onDestroy();


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_view.onLowMemory();
    }

}