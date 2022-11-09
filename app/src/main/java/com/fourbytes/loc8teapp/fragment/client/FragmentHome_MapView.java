package com.fourbytes.loc8teapp.fragment.client;

import static com.fourbytes.loc8teapp.Constants.ARTS_FIELD;
import static com.fourbytes.loc8teapp.Constants.BUSINESS_FIELD;
import static com.fourbytes.loc8teapp.Constants.EDUCATION_FIELD;
import static com.fourbytes.loc8teapp.Constants.FOOD_FIELD;
import static com.fourbytes.loc8teapp.Constants.LAW_FIELD;
import static com.fourbytes.loc8teapp.Constants.MAPVIEW_BUNDLE_KEY;
import static com.fourbytes.loc8teapp.Constants.MEDICAL_FIELD;
import static com.fourbytes.loc8teapp.Constants.SKILLED_TRADE_FIELD;
import static com.fourbytes.loc8teapp.Constants.TECHNOLOGY_FIELD;

import android.Manifest;

import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Realtime;
import com.google.android.gms.location.LocationRequest;

import android.app.Activity;
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
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import com.fourbytes.loc8teapp.DataPasser;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FragmentHome_MapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private View view;
    private View l;

    //private View l2;

    private FusedLocationProviderClient mFusedLocationClient;
    private TextView txtLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private CheckBox mapViewCheckBox;
    private CheckBox listViewCheckBox;
    private CheckBox medicalCheckBox;
    private CheckBox technologyCheckBox;
    private CheckBox skilledCheckBox;
    private CheckBox businessCheckBox;
    private CheckBox educationCheckBox;
    private CheckBox lawCheckBox;
    private CheckBox foodCheckBox;
    private CheckBox artsCheckBox;

    private NumberPicker radiusNumberPicker;

    private ExtendedFloatingActionButton home_settings_FAB;
    //private ExtendedFloatingActionButton location_settings_FAB;

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
    private LocationCallback mLocationCallback;

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
    private Polyline dotted_polyline;

    private VertexMatrix vertex;

    private int checkCount = 0;

    private final int width = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int height = LinearLayout.LayoutParams.WRAP_CONTENT;

    private ArrayList<String> professional_filters = new ArrayList<>();
    private int radius_filter = 0;

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
        medicalCheckBox = view.findViewById(R.id.medical_checkbox);
        technologyCheckBox = view.findViewById(R.id.technology_checkbox);
        skilledCheckBox = view.findViewById(R.id.skilled_trade_checkbox);
        businessCheckBox = view.findViewById(R.id.business_checkbox);
        educationCheckBox = view.findViewById(R.id.education_checkbox);
        lawCheckBox = view.findViewById(R.id.law_checkbox);
        foodCheckBox = view.findViewById(R.id.food_checkbox);
        artsCheckBox = view.findViewById(R.id.arts_checkbox);
        radiusNumberPicker = view.findViewById(R.id.radius_numberpicker);

        home_settings_FAB = view.findViewById(R.id.home_settings);
//        location_settings_FAB = view.findViewById(R.id.location_settings);

        l = view.findViewById(R.id.home_settings_toolbar);
        //l2 = view.findViewById(R.id.location_settings_toolbar);

        logoutButton = view.findViewById(R.id.logout);
        btnFind = view.findViewById(R.id.btn_find);
        btn_refresh = view.findViewById(R.id.btn_refresh);
        map_view = view.findViewById(R.id.map_view);

        // Get parent fragment manager (from host activity)
        parentFragmentManager = getParentFragmentManager();

        radiusNumberPicker.setDisplayedValues(numberPickerValues());
        radiusNumberPicker.setMaxValue(100);
        l.setVisibility(view.GONE);
        //l2.setVisibility(view.GONE);
        home_settings_FAB.shrink();
        //location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        mapViewCheckBox.setChecked(true);
        mapViewCheckBox.setEnabled(false);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                if (isGPSEnabled) {
                    if(isLocationSupported()){
                        findNearestUser();
                    }
                } else {
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPasser.setUsername1(null);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        medicalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (medicalCheckBox.isChecked()) {
                    professional_filters.add(MEDICAL_FIELD);
                } else {
                    professional_filters.remove(MEDICAL_FIELD);
                }

                refreshMap();
            }
        });

        technologyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (technologyCheckBox.isChecked()) {
                    professional_filters.add(TECHNOLOGY_FIELD);
                } else {
                    professional_filters.remove(TECHNOLOGY_FIELD);
                }

                refreshMap();
            }
        });

        skilledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (skilledCheckBox.isChecked()) {
                    professional_filters.add(SKILLED_TRADE_FIELD);
                } else {
                    professional_filters.remove(SKILLED_TRADE_FIELD);
                }

                refreshMap();
            }
        });

        businessCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (businessCheckBox.isChecked()) {
                    professional_filters.add(BUSINESS_FIELD);
                } else {
                    professional_filters.remove(BUSINESS_FIELD);
                }

                refreshMap();
            }
        });

        educationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (educationCheckBox.isChecked()) {
                    professional_filters.add(EDUCATION_FIELD);
                } else {
                    professional_filters.remove(EDUCATION_FIELD);
                }

                refreshMap();
            }
        });

        lawCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (lawCheckBox.isChecked()) {
                    professional_filters.add(LAW_FIELD);
                } else {
                    professional_filters.remove(LAW_FIELD);
                }

                refreshMap();
            }
        });

        foodCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (foodCheckBox.isChecked()) {
                    professional_filters.add(FOOD_FIELD);
                } else {
                    professional_filters.remove(FOOD_FIELD);
                }

                refreshMap();
            }
        });

        artsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (artsCheckBox.isChecked()) {
                    professional_filters.add(ARTS_FIELD);
                } else {
                    professional_filters.remove(ARTS_FIELD);
                }

                refreshMap();
            }
        });

        radiusNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                radius_filter = radiusNumberPicker.getValue() * 10;
                refreshMap();
            }
        });

        initGoogleMap(savedInstanceState);
        initVertices();
        return view;
    }


    public String[] numberPickerValues() {
        String[] numberPickerArray = new String[101];
        int count = 0;
        for (int i = 0; i <= 100; i++) {
            numberPickerArray[i] = String.valueOf(count);
            count += 10;
        }

        return numberPickerArray;
    }

    public void initVertices() {
        try {
            vertex = new VertexMatrix(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findNearestUser() {
        setCurrentLocationMarker(currentUserLat, currentUserLong);

        if (isGPSEnabled) {
            List<VertexInfo> nearest_vertex = vertex.getDistanceLatLong(currentUserLat, currentUserLong);
            String start_point;
            double start_distance;
            if (!nearest_vertex.get(0).getId().equals("vertex3")) {
                start_point = nearest_vertex.get(0).getId();
                start_distance = nearest_vertex.get(0).getDistance();
            } else {
                start_point = nearest_vertex.get(1).getId();
                start_distance = nearest_vertex.get(1).getDistance();
            }

            checkUserNearestNode(start_point, start_distance);
        } else {
            showGPSPopUp();
            return;
        }

    }

    public void checkUserNearestNode(String start_point, double start_distance) {

        ArrayList<UserInfo> Users = new ArrayList<>();
        db.collection("professionals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<String> meet_point_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {
                                    if (document.getDouble("latitude") != null && document.getDouble("longitude") != null) {
                                        if(document.getDouble("latitude") != null
                                                && document.getDouble("longitude") != null
                                                && document.getBoolean("verified")){
                                            String nearest_point;
                                            double nearest_distance;
                                            double latitude = document.getDouble("latitude");
                                            double longitude = document.getDouble("longitude");

                                            String field = document.getString("field");

                                            if (professional_filters.size() == 0 || professional_filters.contains(field)) {
                                                String id = document.getId();
                                                String fname = document.getString("first_name");
                                                String lname = document.getString("last_name");
                                                String name = fname + " " + lname;

                                                if (vertex.getDistanceLatLong(latitude, longitude).get(0).getId() != null) {
                                                    nearest_point = vertex.getDistanceLatLong(latitude, longitude).get(0).getId();
                                                    nearest_distance = vertex.getDistanceLatLong(latitude, longitude).get(0).getDistance();
                                                } else {
                                                    nearest_point = vertex.getDistanceLatLong(latitude, longitude).get(1).getId();
                                                    nearest_distance = vertex.getDistanceLatLong(latitude, longitude).get(1).getDistance();
                                                }

                                                Users.add(new UserInfo(
                                                        id,
                                                        longitude,
                                                        latitude,
                                                        nearest_point,
                                                        name,
                                                        nearest_distance
                                                ));
                                            }

                                        }


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (!Users.isEmpty()) {
                                checkNearestUser(start_point, start_distance, Users);
                            } else {
                                System.out.println("No users");
                            }

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void checkNearestUser(String start_point, double start_distance, ArrayList<UserInfo> Users) {
        final double MAX = 99999;

        if (dotted_polyline != null) {
            dotted_polyline.remove();
        }
        List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(3), new Dot());
        HashMap<String, VertexData> data = vertex.getVertex();
        HashMap<String, PathVertex> origin_path = data.get(start_point).getPath();

        ArrayList<LatLng> points = new ArrayList<>();
        ArrayList<LatLng> dotted_points = new ArrayList<>();
        PolylineOptions polylineOptions = new PolylineOptions();
        PolylineOptions polylineOptionsDotted = new PolylineOptions();
        System.out.println(start_point);
        System.out.println(data.get(start_point).getLatitude());
        System.out.println(data.get(start_point).getLongitude());

        for (int i = 0; i < Users.size(); i++) {
            double path_distance;

            double currentUserDistance = Users.get(i).getDistance();
            if (!start_point.equals(Users.get(i).getMeet_point())) {

                if (origin_path.get(Users.get(i).getMeet_point()) != null) {
                    path_distance = origin_path.get(Users.get(i).getMeet_point()).getDistance();
                    Users.get(i).setDistance(path_distance);
                } else {
                    path_distance = MAX;
                    Users.get(i).setDistance(path_distance);
                }

            } else {
                points.add(new LatLng(currentUserLat, currentUserLong));
                points.add(new LatLng(data.get(start_point).getLatitude(), data.get(start_point).getLongitude()));
//                points.add(new LatLng(data.get(start_point).getLatitude(), data.get(start_point).getLongitude()));
//                points.add(new LatLng(Users.get(i).getLatitude(), Users.get(i).getLongitude()));
                dotted_points.add(new LatLng(data.get(start_point).getLatitude(), data.get(start_point).getLongitude()));
                dotted_points.add(new LatLng(Users.get(i).getLatitude(), Users.get(i).getLongitude()));

                polylineOptionsDotted.addAll(dotted_points);
                polylineOptionsDotted.width(10);
                polylineOptionsDotted.geodesic(true);

                polylineOptions.addAll(points);
                polylineOptions.width(5);
                polylineOptions.color(getResources().getColor(R.color.secondaryColor));
                polylineOptions.geodesic(true);

                map_instance.addPolyline(polylineOptions).setZIndex(0);

                dotted_polyline = map_instance.addPolyline(polylineOptionsDotted);
                dotted_polyline.setZIndex(1);
                dotted_polyline.setColor(getResources().getColor(R.color.primaryColor));
                dotted_polyline.setPattern(pattern);

                setMarkers(Users.get(i).getLatitude(), Users.get(i).getLongitude(), 0, Users.get(i).getName(), Users.get(i).getId());

                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Users.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
        }

        if (origin_path.get(Users.get(0).getMeet_point()) == null) {
            showNoRoutesPopup();
            System.out.println("no route available");

        } else {
            String nearest_user = Users.get(0).getId();
            String nearest_user_name = Users.get(0).getName();
            String nearest_meet_point = Users.get(0).getMeet_point();
            String nearest_path = Users.get(0).getMeet_point();
            double nearest_distance = Users.get(0).getDistance();
            double nearest_user_latitude = Users.get(0).getLatitude();
            double nearest_path_longitude = Users.get(0).getLongitude();

            double start_point_latitude = data.get(start_point).getLatitude();
            double start_point_longitude = data.get(start_point).getLongitude();

            ArrayList<String> final_path = origin_path.get(nearest_path).getPath();
            int path_last_index = final_path.size() - 1;
            double final_point_latitude = data.get(final_path.get(path_last_index)).getLatitude();
            double final_point_longitude = data.get(final_path.get(path_last_index)).getLongitude();
            points.add(new LatLng(currentUserLat, currentUserLong));
            points.add(new LatLng(start_point_latitude, start_point_longitude));
            for (int i = 0; i < final_path.size(); i++) {
                points.add(new LatLng(
                        data.get(final_path.get(i)).getLatitude(),
                        data.get(final_path.get(i)).getLongitude()
                ));
            }

            dotted_points.add(new LatLng(
                    data.get(nearest_meet_point).getLatitude(),
                    data.get(nearest_meet_point).getLongitude()
            ));
            dotted_points.add(new LatLng(
                    nearest_user_latitude,
                    nearest_path_longitude
            ));

            polylineOptionsDotted.addAll(dotted_points);
            polylineOptionsDotted.width(10);
            polylineOptionsDotted.geodesic(true);

            dotted_polyline = map_instance.addPolyline(polylineOptionsDotted);
            dotted_polyline.setColor(getResources().getColor(R.color.primaryColor));
            dotted_polyline.setPattern(pattern);
            dotted_polyline.setZIndex(1);

            polylineOptions.addAll(points);
            polylineOptions.width(5);
            polylineOptions.color(getResources().getColor(R.color.secondaryColor));
            polylineOptions.geodesic(true);

            setMarkers(nearest_user_latitude, nearest_path_longitude, 0, nearest_user_name, nearest_user);
            map_instance.addPolyline(polylineOptions).setZIndex(0);

        }

    }

    private void showNoRoutesPopup() {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View noGPS_view = getLayoutInflater().inflate(R.layout.no_route_popup, null);

        AppCompatButton btn_ok_route = noGPS_view.findViewById(R.id.btn_ok);
        dialogBuilder.setView(noGPS_view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_ok_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void checkNearestUser(JSONObject response, ArrayList<UserInfo> Users) throws JSONException {
        JSONArray elements = response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
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
        if (polyline != null) {
            polyline.remove();
        }

        ArrayList<LatLng> points;
        PolylineOptions polylineOptions = null;

        for (int i = 0; i < routes.length(); i++) {
            points = new ArrayList<>();
            if (i == 0) {
                points.add(new LatLng(currentUserLat, currentUserLong));
            }
            polylineOptions = new PolylineOptions();
            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

            for (int j = 0; j < legs.length(); j++) {
                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                for (int k = 0; k < steps.length(); k++) {
                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                    List<LatLng> list = PolyUtil.decode(polyline);

                    for (int l = 0; l < list.size(); l++) {
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
                if (task.isSuccessful()) {

                    try {
                        Location location = task.getResult();
                        GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                        if (polyline != null) {
                            polyline.remove();
                        }

                        setLocationCamera(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocationMarker(geopoint.getLatitude(), geopoint.getLongitude());
                        isGPSEnabled = true;
                    } catch (Exception e) {
                        setLocationCamera(CAMERA_DEFAULT_LATITUDE, CAMERA_DEFAULT_LONGITUDE);
                        e.printStackTrace();
                    }

                } else {
                    isGPSEnabled = false;
                }
            }
        });
    }

    public void refreshMap() {
        map_instance.clear();
        retrieveUsers();
        getLastLocation();

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

    private boolean isLocationSupported() {

        if (BOUNDING_BOX_LAT1 <= currentUserLat
                && currentUserLat <= BOUNDING_BOX_LAT2
                && BOUNDING_BOX_LONG1 <= currentUserLong
                && currentUserLong <= BOUNDING_BOX_LONG2) {
            System.out.println("SUPPORTED");
            return true;
        } else {
            showNoServicePopUp();
            return false;
        }
    }

    public void showNoServicePopUp() {
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

    public void showGPSPopUp() {
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

    public void showNoResponsePopUp() {
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

    public void setCurrentLocation(double latitude, double longitude) {
        currentUserLat = latitude;
        currentUserLong = longitude;
    }

    private void setLocationCamera(double latitude, double longitude) {
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom
                (new LatLng(latitude, longitude), 18);
        map_instance.animateCamera(point);
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
                        setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                        setCurrentLocationMarker(geopoint.getLatitude(), geopoint.getLongitude());
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

    public void stopLocationRequest(){
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
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

        startLocationRequest();
        retrieveUsers();
        getLastLocation();
        googleMap.setOnMarkerClickListener(this);
    }

    public void getDirections(String destinationLat, String destinationLong) {
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
                        try {
                            String status = response.getString("status");


                            if (status.equals("OK")) {
                                JSONArray routes = response.getJSONArray("routes");
                                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");

                                String distance_parsed = String.valueOf(distance.get("value"));

                                drawPolyline(routes);
                            }

                        } catch (JSONException e) {
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
                                String field = document.getString("field");
                                if(document.getDouble("latitude") != null
                                        && document.getDouble("longitude") != null
                                        && document.getBoolean("verified")){
                                    double latitude = document.getDouble("latitude");
                                    double longitude = document.getDouble("longitude");
                                    double radius_distance = vertex.calculateDistance(currentUserLat, latitude, currentUserLong, longitude);

                                    System.out.println(radius_distance);
                                    try {
                                        if (professional_filters.size() == 0 || professional_filters.contains(field)) {

                                            if (radius_filter == 0 || radius_filter >= radius_distance) {
                                                String id = document.getId();
                                                String meet_point = document.getString("meet_point");
                                                String fname = document.getString("first_name");
                                                String lname = document.getString("last_name");
                                                String specific_job = document.getString("specific_job");
                                                String name = fname + " " + lname;

                                                setMarkers(latitude, longitude, name, id, specific_job, field);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void setMarkers(double latitude, double longitude, double filter, String name, String id) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pro_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))).setTag(new MarkerTag(id, "users"));

    }

    public void setMarkers(double latitude, double longitude, String name, String id, String job, String field) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pro_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 50, 50, false);
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))).setTag(new MarkerTag(id, "users", name, job, field, new LatLng(latitude, longitude)));

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        MarkerTag tag = (MarkerTag) marker.getTag(); //Gets the object to retrieve id/infos

        if (tag != null) {
            String type = tag.getType();
            String name = marker.getTitle();
            String id = tag.getId();

            if (type.equals("users")) {

                View node_onclick_view = getLayoutInflater().inflate(R.layout.user_onclick_popup, null);
                AppCompatButton btn_location = node_onclick_view.findViewById(R.id.btn_location);
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

                TextView user_name = node_onclick_view.findViewById(R.id.user_name);
                TextView user_location = node_onclick_view.findViewById(R.id.user_location);
                TextView user_specfific_job = node_onclick_view.findViewById(R.id.user_specific_job);
                TextView user_field = node_onclick_view.findViewById(R.id.user_field);


                final String API_KEY = getString(R.string.google_maps_api_key);

                String location_string = tag.getLocation().latitude + ", " + tag.getLocation().longitude;

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
                                try {
                                    String status = response.getString("status");
                                    System.out.println(status);
                                    System.out.println(response);

                                    if (status.equals("OK")) {
                                        JSONArray results = response.getJSONArray("results");
                                        String formatted_address = results.getJSONObject(0).getString("formatted_address");
                                        user_location.setText(formatted_address);
                                    }

                                } catch (JSONException e) {
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

                user_name.setText(tag.getName());
                user_specfific_job.setText(tag.getJob());
                user_field.setText(tag.getField());
                final PopupWindow popupWindow = new PopupWindow(node_onclick_view, width, height, true);
                popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                btn_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        openRealtimeFragment(tag.getLocation().latitude, tag.getLocation().longitude);
                    }
                });
            }

        }

        return false;
    }

    private void openRealtimeFragment(double latitude, double longitude){
        Bundle result = new Bundle();
        result.putDouble("desLat", latitude);
        result.putDouble("desLong", longitude);

        parentFragmentManager.setFragmentResult("realtimeData", result);
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment, FragmentEvent_Realtime.class, null)
                .commit();
    }


    public void retrieveProfessionals(String start_point) {
        ArrayList<UserInfo> Users = new ArrayList<>();
        db.collection("professionals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<String> meet_point_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {
                                    if (document.getString("meet_point") != null) {
                                        double latitude = document.getDouble("latitude");
                                        double longitude = document.getDouble("longitude");
                                        String id = document.getId();
                                        String meet_point = document.getString("meet_point");
                                        String fname = document.getString("first_name");
                                        String lname = document.getString("last_name");

                                        String name = fname + " " + lname;
//                                        Users.add(new UserInfo(
//                                                id,
//                                                longitude,
//                                                latitude,
//                                                meet_point,
//                                                name
//                                        ));
                                        if (!meet_point_list.contains(meet_point)) {
                                            meet_point_list.add(meet_point);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (!meet_point_list.isEmpty()) {
                                searchProfessionals(start_point, meet_point_list, Users);
                            }

                        } else {
                            Toast.makeText(getActivity(), "There are no users", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void searchProfessionals(String start_point, ArrayList<String> meet_point_list, ArrayList<UserInfo> Users) {
        double finalLat;
        double finalLong;
        String meet_point;
        ArrayList<String> meet_point_list_filtered = new ArrayList<>();
        HashMap<String, VertexData> vertices = vertex.getVertex();

        HashMap<String, PathVertex> path = vertices.get(start_point).getPath();

        ArrayList<String> nearest_path = new ArrayList<>();

        for (int i = 0; i < meet_point_list.size(); i++) {
            if (path.get(meet_point_list.get(i)) != null || start_point.equals(meet_point_list.get(i))) {
                meet_point_list_filtered.add(meet_point_list.get(i));

                if (start_point.equals(meet_point_list.get(i))) {
                    System.out.println("True");
                    map_instance.clear();

                    finalLat = vertices.get(meet_point_list.get(i)).getLatitude();
                    finalLong = vertices.get(meet_point_list.get(i)).getLongitude();

                    setCurrentLocationMarker(currentUserLat, currentUserLong);
                    getDirections(String.valueOf(vertices.get(start_point).getLatitude()), String.valueOf(vertices.get(start_point).getLongitude()));
                    setDestinationMarker(finalLat, finalLong);

                    getUserMeetPoint(Users, start_point);

                    return;
                }
            } else {
                System.out.println("No path for " + meet_point_list.get(i));
            }

        }

        System.out.println("Meet points to check: " + meet_point_list_filtered.size());
        double nearest_distance = path.get(meet_point_list_filtered.get(0)).getDistance();
        nearest_path = path.get(meet_point_list_filtered.get(0)).getPath();
        for (int i = 0; i < meet_point_list_filtered.size() - 1; i++) {

            double check_distance = path.get(meet_point_list_filtered.get(i + 1)).getDistance();
            System.out.println(nearest_distance);
            System.out.println(check_distance);
            if (nearest_distance > check_distance) {
                System.out.println("new distance is: " + check_distance);
                nearest_path = path.get(meet_point_list_filtered.get(i + 1)).getPath();
            }

        }

        map_instance.clear();
        setCurrentLocationMarker(currentUserLat, currentUserLong);
        getDirections(String.valueOf(vertices.get(start_point).getLatitude()), String.valueOf(vertices.get(start_point).getLongitude()));
        for (int i = 0; i < nearest_path.size() - 1; i++) {
            String originLat = String.valueOf(vertices.get(nearest_path.get(i)).getLatitude());
            String originLong = String.valueOf(vertices.get(nearest_path.get(i)).getLongitude());
            String destinationLat = String.valueOf(vertices.get(nearest_path.get(i + 1)).getLatitude());
            String destinationLong = String.valueOf(vertices.get(nearest_path.get(i + 1)).getLongitude());

            getNodeDirection(destinationLat, destinationLong, originLat, originLong);
        }

        meet_point = nearest_path.get(nearest_path.size() - 1);
        finalLat = vertices.get(nearest_path.get(nearest_path.size() - 1)).getLatitude();
        finalLong = vertices.get(nearest_path.get(nearest_path.size() - 1)).getLongitude();
        getUserMeetPoint(Users, meet_point);
        setDestinationMarker(finalLat, finalLong);

    }

    public void getUserMeetPoint(ArrayList<UserInfo> Users, String meet_point) {

        for (int i = 0; i < Users.size(); i++) {

            if (Users.get(i).getMeet_point().equals(meet_point)) {
                setMarkers(Users.get(i).getLatitude(), Users.get(i).getLongitude(), 0, Users.get(i).getName(), Users.get(i).getId());
            }
        }

    }

    public void setDestinationMarker(double finalLat, double finalLong) {
        map_instance.addMarker(new MarkerOptions()
                .position(new LatLng(finalLat, finalLong))
                .title("Nearest Meet Point")
                .icon(BitmapFromVector(view.getContext(), R.drawable.icon_meetpoint)));
    }

    public void getNodeDirection(String destinationLat, String destinationLong, String originLat, String originLong) {
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
                        try {
                            String status = response.getString("status");


                            if (status.equals("OK")) {
                                JSONArray routes = response.getJSONArray("routes");
                                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                                JSONObject distance = legs.getJSONObject(0).getJSONObject("distance");

                                String distance_parsed = String.valueOf(distance.get("value"));

                                drawMultiplePolyLine(routes);
                            }

                        } catch (JSONException e) {
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

        for (int i = 0; i < routes.length(); i++) {
            points = new ArrayList<>();

            polylineOptions = new PolylineOptions();
            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

            for (int j = 0; j < legs.length(); j++) {
                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                for (int k = 0; k < steps.length(); k++) {
                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                    List<LatLng> list = PolyUtil.decode(polyline);

                    for (int l = 0; l < list.size(); l++) {
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
        stopLocationRequest();
        super.onStop();
        map_view.onStop();

    }

    @Override
    public void onPause() {
        stopLocationRequest();
        map_view.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        stopLocationRequest();
        map_view.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_view.onLowMemory();
    }
}