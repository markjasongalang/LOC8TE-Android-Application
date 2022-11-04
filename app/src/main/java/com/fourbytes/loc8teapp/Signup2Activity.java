package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class Signup2Activity extends AppCompatActivity {
    private FirebaseFirestore db;

    private Spinner spAccount;
    private Spinner spFields;

    private LinearLayout llProOption;

    private TextView tvValidation;
    private TextView tvImageStatus;
    private TextView tvAlert;
    private TextView tvImageAlert;

    private String accountType;

    private AppCompatButton btnNext;
    private AppCompatButton btnAttach;

    private EditText edtEmail;
    private EditText edtContactNumber;
    private EditText edtSpecificJob;

    private byte[] byteArray;

    private Uri idPicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        // Initialize Firebase database
        db = FirebaseFirestore.getInstance();

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
        tvAlert = findViewById(R.id.tv_alert);
        tvImageAlert = findViewById(R.id.tv_image_alert);

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
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        initSpinnerFields();
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

                String email = edtEmail.getText().toString();
                String contactNumber = edtContactNumber.getText().toString();
                String field = spFields.getSelectedItem().toString();
                String specificJob = edtSpecificJob.getText().toString();

                if (email.isEmpty() || contactNumber.isEmpty()) {
                    tvAlert.setText("Please answer all fields.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                if (accountType.equals("professional") && byteArray == null) {
                    tvImageAlert.setText("Please attach an image.");
                    tvImageAlert.setVisibility(View.VISIBLE);
                    return;
                }

                if ((accountType.equals("professional")) && (field.isEmpty() || specificJob.isEmpty())) {
                    tvAlert.setText("Please answer all fields.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

                if (!isEmailCorrect(email)) {
                    tvAlert.setText("Email is not in correct format.");
                    tvAlert.setVisibility(View.VISIBLE);
                    return;
                }

//                db.collection("clients").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                tvAlert.setText("Email already exists.");
//                                tvAlert.setVisibility(View.VISIBLE);
//                                return;
//                            }
//
//                            db.collection("professionals")
//                                .document(getIntent().getStringExtra("username"))
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            DocumentSnapshot document = task.getResult();
//                                            if (document.get) {
//                                                tvAlert.setText("Email already exists.");
//                                                tvAlert.setVisibility(View.VISIBLE);
//                                                return;
//                                            }
//
//                                            tvAlert.setVisibility(View.GONE);
//
//                                            // Previous data to be passed
//                                            intent.putExtra("username", getIntent().getStringExtra("username"));
//                                            intent.putExtra("password", getIntent().getStringExtra("password"));
//                                            intent.putExtra("firstName", getIntent().getStringExtra("firstName"));
//                                            intent.putExtra("middleName", getIntent().getStringExtra("middleName"));
//                                            intent.putExtra("lastName", getIntent().getStringExtra("lastName"));
//                                            intent.putExtra("birthdate", getIntent().getStringExtra("birthdate"));
//
//                                            // New data to be passed
//                                            intent.putExtra("email", email);
//                                            intent.putExtra("contactNumber", contactNumber);
//                                            intent.putExtra("accountType", accountType);
//                                            if (accountType.equals("professional")) {
//                                                tvImageAlert.setVisibility(View.GONE);
//
//                                                intent.putExtra("field", field);
//                                                intent.putExtra("specificJob", specificJob);
//                                                intent.putExtra("idPicture", byteArray);
//                                                intent.putExtra("idPicUri", idPicUri.toString());
//                                            }
//
//                                            startActivity(intent);
//                                        } else {
//                                            Log.d("CHECK_EMAIL", "Failed to read the data...");
//                                        }
//                                    }
//                                });
//                        } else {
//                            Log.d("CHECK_EMAIL", "Failed to read the data...");
//                        }
//                    }
//                });

                db.collection("clients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean emailExists = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("email").equals(email)) {
                                    emailExists = true;
                                    break;
                                }
                            }

                            if (emailExists) {
                                tvAlert.setText("Email already exists.");
                                tvAlert.setVisibility(View.VISIBLE);
                                return;
                            }

                            db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean emailExists = false;

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.getData().get("email").equals(email)) {
                                                emailExists = true;
                                                break;
                                            }
                                        }

                                        if (emailExists) {
                                            tvAlert.setText("Email already exists.");
                                            tvAlert.setVisibility(View.VISIBLE);
                                            return;
                                        }

                                        tvAlert.setVisibility(View.GONE);

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
                                        if (accountType.equals("professional")) {
                                            tvImageAlert.setVisibility(View.GONE);

                                            intent.putExtra("field", field);
                                            intent.putExtra("specificJob", specificJob);
                                            intent.putExtra("idPicture", byteArray);
                                            intent.putExtra("idPicUri", idPicUri.toString());
                                        }

                                        startActivity(intent);
                                    } else {
                                        Log.d("CHECK_EMAIL", "Failed to read the data...");
                                    }
                                }
                            });
                        } else {
                            Log.d("CHECK_EMAIL", "Failed to read the data...");
                        }
                    }
                });

            }
        });
    }

    private boolean isEmailCorrect(String email) {
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        if (email == null) {
            return false;
        }

        return pattern.matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            idPicUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), idPicUri);
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