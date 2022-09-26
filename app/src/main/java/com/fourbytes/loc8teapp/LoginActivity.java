package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase database
        db = FirebaseFirestore.getInstance();

        // Find views from the layout
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HostActivity.class);
                intent.putExtra("accountType", "client");
                startActivity(intent);
            }
        });
    }

    public void openSignupActivity(View view) {
        startActivity(new Intent(this, Signup1Activity.class));
    }
}