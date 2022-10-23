package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Signup2Activity extends AppCompatActivity {
    private Spinner spAccount;
    private Spinner spFields;

    private LinearLayout llProOption;

    private TextView tvValidation;
    private TextView tvImageStatus;

    private String accountType;

    private AppCompatButton btnNext;
    private AppCompatButton btnAttach;

    private EditText edtEmail;
    private EditText edtContactNumber;
    private EditText edtSpecificJob;

    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        // Get views from the layout
        spAccount = findViewById(R.id.sp_account);
        spFields = findViewById(R.id.sp_fields);
        llProOption = findViewById(R.id.ll_pro_option);
        tvValidation = findViewById(R.id.tv_validation);
        btnNext = findViewById(R.id.btn_next);
        edtEmail = findViewById(R.id.edt_email);
        edtContactNumber = findViewById(R.id.edt_contact_number);
        edtSpecificJob = findViewById(R.id.edt_specific_job);
        btnAttach = findViewById(R.id.btn_attach);
        tvImageStatus = findViewById(R.id.tv_image_status);

        initSpinnerAccount();
        spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos == 0) {
                    llProOption.setVisibility(View.GONE);
                    accountType = "client";
                } else {
                    llProOption.setVisibility(View.VISIBLE);
                    accountType = "professional";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        initSpinnerFields();

        // Put message for validation
        initTextValidation();

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
                Intent intent = new Intent(getApplicationContext(), Signup3Activity.class);

                // TODO: Separate conditions for client and professional (for passing data)

                String email = edtEmail.getText().toString();
                String contactNumber = edtContactNumber.getText().toString();
                String field = spFields.getSelectedItem().toString();
                String specificJob = edtSpecificJob.getText().toString();

//                if (email.isEmpty() || contactNumber.isEmpty() || field.isEmpty() || specificJob.isEmpty()) {
//                    Toast.makeText(Signup2Activity.this, "Please answer all fields.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                
                if (accountType.equals("professional") && byteArray == null) {
                    Toast.makeText(Signup2Activity.this, "Please select an image first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Previous data to be passed
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("password", getIntent().getStringExtra("password"));
                intent.putExtra("firstName", getIntent().getStringExtra("firstName"));
                intent.putExtra("middleName", getIntent().getStringExtra("middleName"));
                intent.putExtra("lastName", getIntent().getStringExtra("lastName"));
                intent.putExtra("birthdate", getIntent().getStringExtra("birthdate"));

                // New data to be passed
                intent.putExtra("email", email);
                intent.putExtra("contactNumber", contactNumber);
                intent.putExtra("accountType", accountType);
                intent.putExtra("field", field);
                intent.putExtra("specificJob", specificJob);
                intent.putExtra("idPicture", byteArray);
                startActivity(intent);
            }
        });
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
            byteArray = stream.toByteArray();

            tvImageStatus.setText("image attached");
        } else {
            byteArray = null;
            tvImageStatus.setText("no image attached");
        }
    }

    private void initTextValidation() {
        String message = "** validation process can take <b>3 - 5 business days</b>." +
                "you will receive an email once your account is verified. " +
                "<b>you may not use the account while it is in the process of verification</b>.";

        tvValidation.setText(Html.fromHtml(message));
    }

    private void initSpinnerAccount() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.account_types_array,
                R.layout.spinner_dropdown_layout
        );

        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spAccount.setAdapter(adapter);
    }

    private void initSpinnerFields() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.fields_array,
                R.layout.spinner_dropdown_layout
        );

        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spFields.setAdapter(adapter);
    }
}