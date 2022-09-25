package com.fourbytes.loc8teapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.fourbytes.loc8teapp.fragment.FragmentEvent;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_General;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Industry;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_MyEvents;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    public void openGeneral(View view) {
        FragmentEvent_General fragment = new FragmentEvent_General();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
    public void openIndustry(View view) {
        FragmentEvent_Industry fragment = new FragmentEvent_Industry();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
    public void openMyEvents(View view) {
        FragmentEvent_MyEvents fragment = new FragmentEvent_MyEvents();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
    public void backButton(View view) {
        FragmentEvent fragment = new FragmentEvent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
}