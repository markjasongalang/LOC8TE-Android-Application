package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DataPrivacy extends AppCompatActivity {
    private TextView tvPrivacyPolicy;

    private AppCompatButton btnContinue;
    private AppCompatButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_privacy);

        // Get views from the layout
        tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);
        btnContinue = findViewById(R.id.btn_continue);
        btnGoBack = findViewById(R.id.btn_go_back);

        tvPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freeprivacypolicy.com/live/29545bdd-509d-4f48-938c-769d3a4f40cf")));
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataPrivacy.this, Signup1Activity.class));
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}