package com.fourbytes.loc8teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.newadmin.Item;
import com.fourbytes.loc8teapp.newadmin.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseFirestore collect;

    private int client_counter = 0;
    private int prof_counter = 0;
    private int ToProcess_counter = 0;

    private RecyclerView recyclerView;

    private List<Item> ProfessionalList;

    private TextView Number_of_clients_result;
    private TextView Number_of_professionals_result;
    private TextView Number_of_To_Process;

    private AppCompatButton btnLogoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Find views of components
        recyclerView = findViewById(R.id.admin_recyclerview);
        Number_of_clients_result = findViewById(R.id.no_clients_result);
        Number_of_professionals_result = findViewById(R.id.no_prof_result1);
        Number_of_To_Process = findViewById(R.id.no_prof_result2);
        btnLogoutAdmin = findViewById(R.id.btn_logout_admin);

        // Get Firebase Instances
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collect = FirebaseFirestore.getInstance();

        db.collection("clients").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                client_counter = 0;
                for (QueryDocumentSnapshot document : value) {
                    client_counter = client_counter + 1;
                }
                Number_of_clients_result.setText(String.valueOf(client_counter));
            }
        });

        db.collection("professionals")
                .whereEqualTo("verified", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        prof_counter = 0;
                        for (QueryDocumentSnapshot document : value) {
                            prof_counter = prof_counter + 1;
                        }
                        Number_of_professionals_result.setText(String.valueOf(prof_counter));
                    }
                });

        db.collection("professionals")
                .whereEqualTo("verified", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ToProcess_counter = 0;
                        for (QueryDocumentSnapshot document : value) {
                            ToProcess_counter = ToProcess_counter + 1;
                        }
                        Number_of_To_Process.setText(String.valueOf(ToProcess_counter));
                    }
                });

        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProfessionalList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db.collection("professionals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ProfessionalList.clear();
                for (QueryDocumentSnapshot document : value) {
                    if ((boolean) document.getData().get("verified") == false) {
                        // Get profile picture of current user
                        StorageReference storageRef = storage.getReference();
                        StorageReference pathReference = storageRef.child("profilePics/" + document.getId().toString() + "_profile.jpg");
                        ProfessionalList.add(new Item(document.getId(), document.getData().get("first_name").toString() + " " + document.getData().get("last_name").toString(), pathReference));

                    }
                }
                recyclerView.setAdapter(new MyAdapter(AdminActivity.this, ProfessionalList));
            }
        });
    }
}

