package com.fourbytes.loc8teapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class Signup3Activity extends AppCompatActivity {
    private TextView tvAccountType;
    private TextView tvAttachReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        // Get views from the layout
        tvAccountType = findViewById(R.id.tv_account_type);
        tvAttachReminder = findViewById(R.id.tv_attach_reminder);

        // Change some text based on the previous activity
        setTextBasedOnPrevious();
    }

    private void setTextBasedOnPrevious() {
        String accountType = getIntent().getStringExtra("accountType");
        tvAccountType.setText(accountType);

        String message = tvAttachReminder.getText().toString();
        if (accountType.equals("client")) {
            message += "<b>this will ensure authenticity within the application.</b>";
        } else {
            message += "<b>this will be used to validate if your ID matches your picture.</b>";
        }
        tvAttachReminder.setText(Html.fromHtml(message));
    }

    public void openNextSignup(View view) {
        startActivity(new Intent(this, Signup4Activity.class));
    }
}