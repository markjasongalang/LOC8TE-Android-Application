package com.fourbytes.loc8teapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    private BottomNavigationView bottomNavigationView;

    private NavController navController;

    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        // Initialize values
        fragmentManager = getSupportFragmentManager();

        // Get views from layout
        bottomNavigationView = findViewById(R.id.navigation);
        navController = Navigation.findNavController(this, R.id.fragment);

        // Retrieve account type to show the corresponding navigation (client or professional)
        String accountType = getIntent().getStringExtra("accountType");
        String username = getIntent().getStringExtra("username");
        String name = getIntent().getStringExtra("name");

        // Pass username and account type to all fragments
        if (accountType.equals("client")) {
            viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
            viewModel.setData(new Pair(username, accountType, name));
        } else {
            String field = getIntent().getStringExtra("field");
            String specific_job = getIntent().getStringExtra("specific_job");
            viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
            viewModel.setData(new Pair(username, accountType, name, field, specific_job));
        }

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