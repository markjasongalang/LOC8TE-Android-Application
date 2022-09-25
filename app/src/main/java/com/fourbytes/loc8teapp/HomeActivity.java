package com.fourbytes.loc8teapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        // Note: If there's an error here, go to build.gradle(:app) then click Sync Now
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}