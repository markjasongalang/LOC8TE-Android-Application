package com.fourbytes.loc8teapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Signup4Activity extends AppCompatActivity {
    private AppCompatButton btnGps;
    private AppCompatButton btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        // Get views from layout
        btnGps = findViewById(R.id.btn_gps);
        btnSignup = findViewById(R.id.btn_signup);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("profilePicture");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}