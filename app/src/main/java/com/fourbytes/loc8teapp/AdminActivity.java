package com.fourbytes.loc8teapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fourbytes.loc8teapp.admin.AccountRequestItem;
import com.fourbytes.loc8teapp.admin.AdminAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<AccountRequestItem> items= new ArrayList<AccountRequestItem>();
        items.add(new AccountRequestItem("CHEF JOSHUA", R.drawable.juswa_hearts));
        items.add(new AccountRequestItem("CHEF JOSHUA2", R.drawable.juswa_hearts));
        items.add(new AccountRequestItem("CHEF JOSHUA3", R.drawable.juswa_hearts));
        items.add(new AccountRequestItem("CHEF JOSHUA4", R.drawable.juswa_hearts));
        items.add(new AccountRequestItem("CHEF JOSHUA5", R.drawable.juswa_hearts));
        items.add(new AccountRequestItem("CHEF JOSHUA6", R.drawable.juswa_hearts));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdminAdapter(getApplicationContext(),items));
    }
}