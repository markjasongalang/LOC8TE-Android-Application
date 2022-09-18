package com.fourbytes.loc8teapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Signup2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spAccount;
    private LinearLayout llProOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        spAccount = findViewById(R.id.sp_account);
        llProOption = findViewById(R.id.ll_pro_option);

        initSpinnerAccount();
        spAccount.setOnItemSelectedListener(this);
    }

    private void initSpinnerAccount() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types_array, R.layout.spinner_dropdown_layout);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spAccount.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (pos == 0) {
            llProOption.setVisibility(View.GONE);
        } else {
            llProOption.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}