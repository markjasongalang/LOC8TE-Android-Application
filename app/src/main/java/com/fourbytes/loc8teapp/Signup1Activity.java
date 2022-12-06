package com.fourbytes.loc8teapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class Signup1Activity extends AppCompatActivity {
    private FirebaseFirestore db;

    private TextView tvAlert;

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtFirstName;
    private EditText edtMiddleName;
    private EditText edtLastName;

    private AppCompatButton btnNext;
    private AppCompatButton btnDate;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Calendar calendar;

    private boolean doneChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        // Initialize Firebase database
        db = FirebaseFirestore.getInstance();

        // Get views from layout
        tvAlert = findViewById(R.id.tv_alert);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtMiddleName = findViewById(R.id.edt_middle_name);
        edtLastName = findViewById(R.id.edt_last_name);
        btnNext = findViewById(R.id.btn_next);
        btnDate = findViewById(R.id.btn_date);

        // Initialize the date picker dialog
        initDatePicker();
        btnDate.setText(getDateToday());

        // TODO: Place Data Privacy

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
                String username = edtUsername.getText().toString();
                String password = sha1(edtPassword.getText().toString());
                String confirmPassword = sha1(edtConfirmPassword.getText().toString());
                String firstName = edtFirstName.getText().toString();
                String middleName = edtMiddleName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String birthdate = btnDate.getText().toString();

                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
                    middleName.isEmpty() || lastName.isEmpty() || birthdate.isEmpty()) {

                    tvAlert.setText("Please answer all fields.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    tvAlert.setText("Passwords do not match.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                tvAlert.setVisibility(View.GONE);

                if (isPasswordWeak(edtPassword.getText().toString())) {
                    tvAlert.setText(
                        "** Password is too weak. **\n" +
                        "A password is said to be strong if:\n" +
                        "- It contains at least one lowercase English character.\n" +
                        "- It contains at least one uppercase English character.\n" +
                        "- It contains at least one special character. The special characters are: !@#$%^&*()-+" +
                        "- Its length is at least 8.\n" +
                        "- It contains at least one digit."
                    );
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                db.collection("clients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean usernameExists = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(username)) {
                                    usernameExists = true;
                                    break;
                                }
                            }

                            if (usernameExists) {
                                tvAlert.setText("Username already exists.");
                                tvAlert.setVisibility(View.VISIBLE);
                                return;
                            }

                            db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean usernameExists = false;

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.getId().equals(username)) {
                                                usernameExists = true;
                                                break;
                                            }
                                        }

                                        if (usernameExists) {
                                            tvAlert.setText("Username already exists.");
                                            tvAlert.setVisibility(View.VISIBLE);
                                            return;
                                        }

                                        tvAlert.setVisibility(View.GONE);
                                        passIntent(username, password, firstName, middleName, lastName, birthdate);
                                    } else {
                                        Log.d("READ_CLIENT_ERR", "Failed to retrieve the clients collection...");
                                    }
                                }
                            });
                        } else {
                            Log.d("READ_CLIENT_ERR", "Failed to retrieve the clients collection...");
                        }
                    }
                });

            }
        });
    }

    private boolean isPasswordWeak(String password) {
        int n = password.length();

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        Set<Character> set = new HashSet<Character>( Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLower = true;
            }

            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            }

            if (Character.isDigit(ch)) {
                hasDigit = true;
            }

            if (set.contains(ch)) {
                hasSpecialChar = true;
            }
        }

        return !(hasDigit && hasLower && hasUpper && hasSpecialChar && (n >= 8));
    }

    private void passIntent(String username, String password, String firstName, String middleName, String lastName, String birthdate) {
        Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("firstName", firstName);
        intent.putExtra("middleName", middleName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("birthdate", birthdate);
        startActivity(intent);
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