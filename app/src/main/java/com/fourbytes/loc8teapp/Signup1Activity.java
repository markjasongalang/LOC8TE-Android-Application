package com.fourbytes.loc8teapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Signup1Activity extends AppCompatActivity {
    private FirebaseFirestore db;

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtMiddleName;
    private EditText edtLastName;

    private AppCompatButton btnNext;
    private AppCompatButton btnDate;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        // Initialize Firebase database
        db = FirebaseFirestore.getInstance();

        // Get views from layout
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtMiddleName = findViewById(R.id.edt_middle_name);
        edtLastName = findViewById(R.id.edt_last_name);
        btnNext = findViewById(R.id.btn_next);
        btnDate = findViewById(R.id.btn_date);

        // Initialize the date picker dialog
        initDatePicker();
        btnDate.setText(getDateToday());

        // Date button
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        // Next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Check username if it already exists in the database

                String username = edtUsername.getText().toString();
                String password = sha1(edtPassword.getText().toString());
                String firstName = edtFirstName.getText().toString();
                String middleName = edtMiddleName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String birthdate = btnDate.getText().toString();

//                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
//                    middleName.isEmpty() || lastName.isEmpty() || birthdate.isEmpty()) {
//
//                    Toast.makeText(Signup1Activity.this, "Please answer all fields.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("firstName", firstName);
                intent.putExtra("middleName", middleName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("birthdate", birthdate);
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

    private String getDateToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
         dateSetListener = (datePicker, year, month, day) -> {
             month = month + 1;
             String date = makeDateString(day, month, year);
             btnDate.setText(date);
         };

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return month + "/" + day + "/" + year;
    }
}