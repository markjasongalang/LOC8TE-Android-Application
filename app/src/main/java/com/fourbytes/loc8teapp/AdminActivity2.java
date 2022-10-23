package com.fourbytes.loc8teapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ImageView;

public class AdminActivity2 extends AppCompatActivity {

    Spinner sp_IDtype;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

//
//        sp_IDtype = findViewById(R.id.sp_IDtype);
//        imageView = findViewById(R.id.imageview);
//        final String str[]={"Drivers Licence", "National ID", "Voters ID", "Passport"};
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(AdminActivity2.this, android.R.layout.simple_dropdown_item_1line,str);
//        sp_IDtype.setAdapter(arrayAdapter);
//        sp_IDtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(str[0].equals(sp_IDtype.getItemAtPosition(i).toString())){
//                    imageView.setImageResource(R.drawable.driverslicense_template);
//                }else if(str[1].equals(sp_IDtype.getItemAtPosition(i).toString())){
//                    imageView.setImageResource(R.drawable.juswa_hearts);
//                }else if(str[2].equals(sp_IDtype.getItemAtPosition(i).toString())){
//                    imageView.setImageResource(R.drawable.loid);
//                }else if(str[3].equals(sp_IDtype.getItemAtPosition(i).toString())){
//                    imageView.setImageResource(R.drawable.yor);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
}