package com.fourbytes.loc8teapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class Signup4Activity extends AppCompatActivity {
    private FirebaseFirestore db;

    private AppCompatButton btnGps;
    private AppCompatButton btnSignup;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Double currentUserLat;
    private Double currentUserLong;

    private TextView tvConfirmationNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        // Get views from layout
        btnGps = findViewById(R.id.btn_gps);
        btnSignup = findViewById(R.id.btn_signup);
        tvConfirmationNote = findViewById(R.id.tv_confirmation_note);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentUserLat = currentUserLong = null;

        if (getIntent().getStringExtra("accountType").equals("professional")) {
            tvConfirmationNote.setText("After signing up, we will send the confirmation/verification of your account in the email that you have provided." + "\n" +
                    "Note: the account cannot be used if it's not yet verified");
        }

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = btnGps.getText().toString();
                if (status.equals("turn on GPS")) {
                    getLastLocation();
                    btnGps.setText("turn off GPS");
                } else {
                    currentUserLat = currentUserLong = null;
                    btnGps.setText("turn on GPS");
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                data.put("verified", false);

                // TODO: Save pictures in database

                if (accountType.equals("professional")) {
                    data.put("field", field);
                    data.put("specific_job", specificJob);
                }

                data.put("latitude", currentUserLat);
                data.put("longitude", currentUserLong);

                String type = (accountType.equals("professional") ? "professionals" : "clients");
                db.collection(type).document(username)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Signup4Activity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();
                                Log.d("FSUCCESS", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup4Activity.this, "An error has occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                Log.w("FERROR", "Error writing document", e);
                            }
                        });
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
}