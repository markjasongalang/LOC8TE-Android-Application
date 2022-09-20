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

public class Signup2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spAccount;
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
        llProOption = findViewById(R.id.ll_pro_option);
        tvValidation = findViewById(R.id.tv_validation);

        // Fill spinner with data
        initSpinnerAccount();
        spAccount.setOnItemSelectedListener(this);

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

    /* The methods below are needed to be overridden for the spinner action listener. */
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

    public void openNextSignUp(View view) {
        Intent intent = new Intent(this, Signup3Activity.class);
        intent.putExtra("accountType", accountType);
        startActivity(intent);
    }
}