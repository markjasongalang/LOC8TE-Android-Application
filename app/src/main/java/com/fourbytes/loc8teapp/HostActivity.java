package com.fourbytes.loc8teapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    private BottomNavigationView bottomNavigationView;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.navigation);
        navController = Navigation.findNavController(this, R.id.fragment);

        // Retrieve account type to show the corresponding navigation (client or professional)
        String accountType = getIntent().getStringExtra("accountType");

        // Separate the navigation (client or professional)
        if (accountType.equals("client")) {
            bottomNavigationView.inflateMenu(R.menu.client_bottom_navigation_menu);
            navController.setGraph(R.navigation.client_navigation_graph);
        } else {
            bottomNavigationView.inflateMenu(R.menu.professional_bottom_navigation_menu);
            navController.setGraph(R.navigation.professional_navigation_graph);
        }

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}