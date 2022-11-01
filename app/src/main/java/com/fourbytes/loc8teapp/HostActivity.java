package com.fourbytes.loc8teapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        viewModel.setData(username);

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