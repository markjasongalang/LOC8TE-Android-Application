package com.fourbytes.loc8teapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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

                // TODO: Apply a Hash Function on the password

                Map<String, Object> client = new HashMap<>();
                client.put("password", edtPassword.getText().toString());
                client.put("first_name", edtFirstName.getText().toString());
                client.put("middle_name", edtMiddleName.getText().toString());
                client.put("last_name", edtLastName.getText().toString());
                client.put("birth_date", btnDate.getText().toString());

                db.collection("clients").document(edtUsername.getText().toString())
                        .set(client)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Signup1Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup1Activity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });


                //startActivity(new Intent(getApplicationContext(), Signup2Activity.class));
            }
        });
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

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return month + "/" + day + "/" + year;
    }
}