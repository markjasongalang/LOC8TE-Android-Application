package com.fourbytes.loc8teapp;

import static android.content.ContentValues.TAG;
import static com.fourbytes.loc8teapp.Constants.ERROR_DIALOG_REQUEST;
import static com.fourbytes.loc8teapp.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.fourbytes.loc8teapp.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


import java.util.EnumSet;
//
//import io.radar.sdk.Radar;
//import io.radar.sdk.model.RadarRoutes;


public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private EditText edtUsername;
    private EditText edtPassword;

    private boolean LocationPermission = false;

    private AppCompatButton btnLogin;

    private TextView tvDontHaveAccount;
    private TextView tvAlert;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();

        // Get views from the layout
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        tvDontHaveAccount = findViewById(R.id.tv_dont_have_account);
        tvAlert = findViewById(R.id.tv_alert);

        // Permission Checker
        getLocationPermission();

//        db.collection("clients")
//                .document("sample_client69")
//                .collection("new")
//                .document("professional1")
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (task.getResult().exists()) {
//                                Log.d("subcol", task.getResult().getData().toString());
//                            } else {
//                                Log.d("subcol", "No document");
//                            }
//                        } else {
//                            Log.d("subcol", "Get failed");
//                        }
//                    }
//                });
//                });

//        Map<String, Object> mp = new HashMap<>();
//        mp.put("year", 1995);
//        db.collection("clients").document("sample_client69").collection("new").document("professional1").set(mp);
//        db.collection("clients").document("sample_client69").set(mp);
//        collection("new").document("professional1").set(mp)
//            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
//                }
//            });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = sha1(edtPassword.getText().toString());

                if (username.isEmpty() || edtPassword.getText().toString().isEmpty()) {
                    tvAlert.setText("Please fill out all fields.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                // todo: DON'T LET UNVERIFIED PROFESSIONALS LOG IN

                db.collection("clients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dbUsername = document.getData().get("username").toString();
                                String dbPassword = document.getData().get("password").toString();

                                if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                    String fname = document.getData().get("first_name").toString();
                                    String lname = document.getData().get("last_name").toString();
                                    Intent intent = new Intent(LoginActivity.this, HostActivity.class);
                                    intent.putExtra("accountType", "client");
                                    intent.putExtra("username", username);
                                    intent.putExtra("name", fname + " " + lname);

                                    mAuth.signInWithEmailAndPassword(document.getData().get("email").toString(), password)
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "signInWithEmail:success");
                                                    } else {
                                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                    }
                                                }
                                            });

                                    tvAlert.setVisibility(View.GONE);

                                    startActivity(intent);
                                    finish();

                                    return;
                                }
                            }

                            db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            String dbUsername = documentSnapshot.getData().get("username").toString();
                                            String dbPassword = documentSnapshot.getData().get("password").toString();

                                            if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                String fname = documentSnapshot.getData().get("first_name").toString();
                                                String lname = documentSnapshot.getData().get("last_name").toString();
                                                Intent intent = new Intent(LoginActivity.this, HostActivity.class);
                                                intent.putExtra("accountType", "professional");
                                                intent.putExtra("username", username);
                                                intent.putExtra("name", fname + " " + lname);
                                                intent.putExtra("field", documentSnapshot.getData().get("field").toString());
                                                intent.putExtra("specific_job", documentSnapshot.getData().get("specific_job").toString());

                                                mAuth.signInWithEmailAndPassword(documentSnapshot.getData().get("email").toString(), password)
                                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("pro_login", "signInWithEmail:success");
                                                                } else {
                                                                    Log.w("pro_login", "signInWithEmail:failure", task.getException());
                                                                }
                                                            }
                                                        });

                                                tvAlert.setVisibility(View.GONE);

                                                startActivity(intent);
                                                finish();

                                                return;
                                            }
                                        }

                                        tvAlert.setText("Incorrect username/password.");
                                        tvAlert.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.d("LOGIN_LOG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.d("LOGIN_LOG", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

        tvDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private String sha1(String input) {
        MessageDigest mDigest = null;

        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private boolean checkMapServices(){
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if (available == ConnectionResult.SUCCESS){
            // Everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }  else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // An error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationPermission = false;

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationPermission = true;
                }
            }
        }
    }

    public void openSignupActivity(View view) {
        startActivity(new Intent(this, Signup1Activity.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            Intent intent = new Intent(new Intent(LoginActivity.this, HostActivity.class));

            db.collection("clients").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot document : value) {
                        if (document.getData().get("email").toString().equals(email)) {
                            String fname = document.getData().get("first_name").toString();
                            String lname = document.getData().get("last_name").toString();
                            intent.putExtra("accountType", document.getData().get("account_type").toString());
                            intent.putExtra("name", fname + " " + lname);
                            intent.putExtra("username", document.getId());
                            startActivity(intent);
                            finish();
                        }
                    }

                    db.collection("professionals").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot document : value) {
                                if (document.getData().get("email").toString().equals(email)) {
                                    String fname = document.getData().get("first_name").toString();
                                    String lname = document.getData().get("last_name").toString();
                                    intent.putExtra("accountType", document.getData().get("account_type").toString());
                                    intent.putExtra("username", document.getId());
                                    intent.putExtra("name", fname + " " + lname);
                                    intent.putExtra("field", document.getData().get("field").toString());
                                    intent.putExtra("specific_job", document.getData().get("specific_job").toString());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
                }
            });

//            db.collection("clients").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        intent.putExtra("accountType", task.getResult().getData().get("account_type").toString());
//                        intent.putExtra("username", task.getResult().getData().get("username").toString());
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        db.collection("professionals").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    intent.putExtra("accountType", task.getResult().getData().get("account_type").toString());
//                                    intent.putExtra("username", task.getResult().getData().get("username").toString());
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        });
//                    }
//                }
//            });

        }
    }
}