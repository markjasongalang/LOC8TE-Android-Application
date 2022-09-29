package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Signup2Activity extends AppCompatActivity {
    private Spinner spAccount;
    private Spinner spFields;

    private LinearLayout llProOption;

    private TextView tvValidation;

    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        // TODO: Test if an image can be retrieved from gallery

        // Get views from the layout
        spAccount = findViewById(R.id.sp_account);
        spFields = findViewById(R.id.sp_fields);
        llProOption = findViewById(R.id.ll_pro_option);
        tvValidation = findViewById(R.id.tv_validation);

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
        spFields.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Put message for validation
        initTextValidation();
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

    public void openNextSignUp(View view) {
        Intent intent = new Intent(this, Signup3Activity.class);
        intent.putExtra("accountType", accountType);
        startActivity(intent);
    }
}