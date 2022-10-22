package com.fourbytes.loc8teapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Signup3Activity extends AppCompatActivity {
    private TextView tvAccountType;
    private TextView tvAttachReminder;
    private TextView tvImageStatus;

    private AppCompatButton btnAttach;
    private AppCompatButton btnNext;

    private byte[] curByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        // Get views from the layout
        tvAccountType = findViewById(R.id.tv_account_type);
        tvAttachReminder = findViewById(R.id.tv_attach_reminder);
        tvImageStatus = findViewById(R.id.tv_image_status);
        btnAttach = findViewById(R.id.btn_attach);
        btnNext = findViewById(R.id.btn_next);

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup4Activity.class);

                if (curByteArray == null) {
                    Toast.makeText(Signup3Activity.this, "Please select an image first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Previous data to be passed
                String accountType = getIntent().getStringExtra("accountType");

                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("password", getIntent().getStringExtra("password"));
                intent.putExtra("firstName", getIntent().getStringExtra("firstName"));
                intent.putExtra("middleName", getIntent().getStringExtra("middleName"));
                intent.putExtra("lastName", getIntent().getStringExtra("lastName"));
                intent.putExtra("birthdate", getIntent().getStringExtra("birthdate"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("contactNumber", getIntent().getStringExtra("contactNumber"));
                intent.putExtra("accountType", accountType);

                if (accountType.equals("professional")) {
                    intent.putExtra("field", getIntent().getStringExtra("field"));
                    intent.putExtra("specificJob", getIntent().getStringExtra("specificJob"));

                    Bundle extras = getIntent().getExtras();
                    byte[] prevByteArray = extras.getByteArray("idPicture");
                    intent.putExtra("idPicture", prevByteArray);
                }

                // New data to be passed
                intent.putExtra("profilePicture", curByteArray);

                // Problem: Not proceeding to SignUpActivity4 :(
                startActivity(intent);
            }
        });

        // Change some text based on the previous activity
        setTextBasedOnPrevious();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            curByteArray = stream.toByteArray();

            tvImageStatus.setText("image attached");
        } else {
            curByteArray = null;
            tvImageStatus.setText("no image attached");
        }
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
}