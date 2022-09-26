package com.fourbytes.loc8teapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {
    private ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        // Note: If there's an error here, go to build.gradle(:app) then click Sync Now
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Set account type to show the corresponding layout (client or professional)
        setAccountType();
    }

    private void setAccountType() {
        String accountType = getIntent().getStringExtra("accountType");
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.setItem(accountType);
    }

    public void openGeneral(View view) {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.fragmentEvent_general);
    }

    public void openIndustry(View view) {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.fragmentEvent_industry);
    }

    public void openMyEvents(View view) {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.fragmentEvent_myevents);
    }

    public void backButton(View view) {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.fragmentEvent);
    }
}