package com.fourbytes.loc8teapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fourbytes.loc8teapp.admin.Admin_Holder_Data;
import com.fourbytes.loc8teapp.admin.Admin_Recyclerview_Adapter;
import com.fourbytes.loc8teapp.admin.RecyclerViewInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements RecyclerViewInterface {

    private FirebaseFirestore db;
    public int client_counter = 0;
    public int prof_counter = 0;
    public int ToProcess_counter = 0;


    ArrayList<Admin_Holder_Data> AdminHolderData =  new ArrayList<>();

    int[] professionalImages = {R.drawable.anya, R.drawable.loid,R.drawable.loid, R.drawable.juswa_hearts};
    TextView Number_of_clients_result;
    TextView Number_of_professionals_result;
    TextView Number_of_To_Process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        RecyclerView recyclerView = findViewById(R.id.admin_recyclerview);
        Number_of_clients_result =  findViewById(R.id.no_clients_result);
        Number_of_professionals_result =  findViewById(R.id.no_prof_result1);
        Number_of_To_Process = findViewById(R.id.no_prof_result2);
        setUpAdmin_Holder_Data();
        Admin_Recyclerview_Adapter adapter =  new Admin_Recyclerview_Adapter(this, AdminHolderData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseFirestore.getInstance();

        FirebaseFirestore.getInstance()
                .collection("clients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document:task.getResult()){
                               client_counter = client_counter + 1;
                                Number_of_clients_result.setText(client_counter + " " + document.getString("account_type") + "s");


                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("this",e.getMessage());
                    }
                });



        FirebaseFirestore.getInstance()
                .collection("professionals")
                .whereEqualTo("verified", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document:task.getResult()){
                                prof_counter = prof_counter + 1;
                                Number_of_professionals_result.setText(prof_counter + " " + document.getString("account_type") + "s");



                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("this",e.getMessage());
                    }
                });


        FirebaseFirestore.getInstance()
                .collection("professionals")
                .whereEqualTo("verified", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document:task.getResult()){
                                ToProcess_counter = ToProcess_counter + 1;
                                Number_of_To_Process.setText(ToProcess_counter + " " + document.getString("account_type") + "s");


                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("this",e.getMessage());
                    }
                });




    }




    public void setUpAdmin_Holder_Data(){
        String[] professionalNames = getResources().getStringArray(R.array.sample_names_array);

        for(int i = 0; i <professionalNames.length; i++){
            AdminHolderData.add(new Admin_Holder_Data(professionalNames[i],professionalImages[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminActivity.this, AdminActivity2.class);





        startActivity(intent);

    }
}