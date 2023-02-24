package com.fourbytes.loc8teapp;

import static com.fourbytes.loc8teapp.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Signup4Activity extends AppCompatActivity {
    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FirebaseAuth mAuth;

    private StorageReference storageRef;

    private AppCompatButton btnGps;
    private AppCompatButton btnSignup;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Double currentUserLat;
    private Double currentUserLong;

    private HashMap<String, Object> temp;

    private LocationCallback mLocationCallback;

    private boolean isLocationSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        // Get views from layout
        btnGps = findViewById(R.id.btn_gps);
        btnSignup = findViewById(R.id.btn_signup);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentUserLat = currentUserLong = null;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        temp = new HashMap<>();

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = btnGps.getText().toString();
                if (status.equals("turn on GPS")) {
                    startLocationRequest();
                    getLastLocation();
                    btnGps.setText("turn off GPS");
                } else {
                    currentUserLat = currentUserLong = null;
                    btnGps.setText("turn on GPS");
                }
            }
        });

        temp.put("exists", true);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get all data
                String username = getIntent().getStringExtra("username");
                String password = getIntent().getStringExtra("password");
                String firstName = getIntent().getStringExtra("firstName");
                String middleName = getIntent().getStringExtra("middleName");
                String lastName = getIntent().getStringExtra("lastName");
                String birthdate = getIntent().getStringExtra("birthdate");
                String email = getIntent().getStringExtra("email");
                String contactNumber = getIntent().getStringExtra("contactNumber");
                String accountType = getIntent().getStringExtra("accountType");

                String field = "";
                String specificJob = "";
                byte[] idPicByteArray = null;
                Bundle extras = getIntent().getExtras();
                if (accountType.equals("professional")) {
                    field = getIntent().getStringExtra("field");
                    specificJob = getIntent().getStringExtra("specificJob");
                    idPicByteArray = extras.getByteArray("idPicture");
                }

                byte[] profilePicByteArray = extras.getByteArray("profilePicture");

                // Put data in HashMap
                Map<String, Object> data = new HashMap<>();
                data.put("username", username);
                data.put("password", password);
                data.put("first_name", firstName);
                data.put("middle_name", middleName);
                data.put("last_name", lastName);
                data.put("birthdate", birthdate);
                data.put("email", email);
                data.put("contact_number", contactNumber);
                data.put("account_type", accountType);

                if (accountType.equals("professional")) {
                    data.put("field", field);
                    data.put("specific_job", specificJob);
                    data.put("verified", false);
                    data.put("meet_point", null);
                }

                data.put("latitude", currentUserLat);
                data.put("longitude", currentUserLong);

                String type = (accountType.equals("professional") ? "professionals" : "clients");

                /* Cloud Firestore */
                db.collection(type).document(username)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Signup4Activity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();
                                Log.d("SIGNUP_SUCCESS", "DocumentSnapshot successfully written!");

                                db.collection(type)
                                        .document(username)
                                        .collection("registered_events")
                                        .document("sample_event")
                                        .set(temp);

                                if (accountType.equals("professional")) {
                                    db.collection(type)
                                            .document(username)
                                            .collection("created_events")
                                            .document("sample_event")
                                            .set(temp);
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup4Activity.this, "An error has occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                Log.w("SIGNUP_ERROR", "Error writing document", e);
                            }
                        });

                /* Firebase Authentication */
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup4Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("AUTH_SUCCESS", "Authentication Success");
                                    FirebaseAuth.getInstance().signOut();
                                } else {
                                    Log.d("AUTH_ERROR", "Authentication Error");
                                }
                            }
                        });

                /* Firebase Storage */

                // Upload id picture
                if (accountType.equals("professional")) {
                    Uri idPicUri = Uri.parse(getIntent().getStringExtra("idPicUri"));
                    StorageReference idPicRef = storageRef.child("idPics/" + username + "_id." + getFileExtension(idPicUri));
                    UploadTask uploadTask = idPicRef.putBytes(idPicByteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("ID_PIC_SUCCESS", "Successfully uploaded the id picture.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ID_PIC_ERROR", "Error in uploading the id picture.");
                            }
                        });
                }

                // Upload profile picture
                Uri profilePicUri = Uri.parse(getIntent().getStringExtra("profilePicUri"));
                StorageReference profileRef = storageRef.child("profilePics/" + username + "_profile." + getFileExtension(profilePicUri));
                UploadTask uploadTask = profileRef.putBytes(profilePicByteArray);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("PROFILE_PIC_SUCCESS", "Successfully uploaded the profile picture.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PROFILE_PIC_ERROR", "Error in uploading the profile picture.");
                    }
                });

                Intent intent = null;
                if (accountType.equals("client")) {
                    intent = new Intent(Signup4Activity.this, LoginActivity.class);
                } else {
                    intent = new Intent(Signup4Activity.this, ExtraActivity.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                }
            }
        });
        return;
    }

    public void setCurrentLocation(double latitude, double longitude) {
        currentUserLat = latitude;
        currentUserLong = longitude;
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
                            setCurrentLocation(geopoint.getLatitude(), geopoint.getLongitude());
                            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                            isLocationSet = false;
                        }
                        isLocationSet = true;
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

}