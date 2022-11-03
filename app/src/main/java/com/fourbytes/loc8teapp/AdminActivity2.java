package com.fourbytes.loc8teapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class AdminActivity2 extends AppCompatActivity {

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db;
    public Spinner sp_id_type;
    public ImageView image_ids;


    TextView Professional_name;
    TextView Professional_work;
    TextView Professional_first_name;
    TextView Professional_mid_name;
    TextView Professional_last_name;
    TextView Professional_bday;
    ImageView Professional_profile_pic;
    ImageView Professional_id_pic;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

        sp_id_type=findViewById(R.id.spinner_id_type);
        image_ids=findViewById(R.id.image_ids);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference getImage = databaseReference.child("profilePics");


        Professional_name =  findViewById(R.id.prof_name2);
        Professional_work =  findViewById(R.id.prof_work2);
        Professional_first_name = findViewById(R.id.prof_fist_name2);
        Professional_mid_name = findViewById(R.id.prof_middle_name2);
        Professional_last_name = findViewById(R.id.prof_last_name2);
        Professional_bday = findViewById(R.id.prof_bday2);
        Professional_profile_pic = findViewById(R.id.prof_profile_pic);
        Professional_id_pic = findViewById(R.id.prof_id_pic);

        String [] id_type = {"Select one", "Passport", "Drivers license", "School ID", " Voters ID"};

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this, R.layout.spinner_dropdown_layout,id_type);

        sp_id_type.setAdapter(adapter);

        sp_id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 1:
                        image_ids.setImageResource(R.drawable.loid);
                        break;
                    case 2:
                        image_ids.setImageResource(R.drawable.driverslicense_template);
                        break;
                    case 3:
                        image_ids.setImageResource(R.drawable.juswa_hearts);
                        break;
                    case 4:
                        image_ids.setImageResource(R.drawable.anya);
                        break;
                    default:
                        image_ids.setImageResource(R.drawable.white_round_corners_10);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                               Professional_name.setText(document.getString("first_name") + " " + document.getString("middle_name") + " " + document.getString("last_name"));
                               Professional_work.setText(document.getString("specific_job"));
                               Professional_first_name.setText(document.getString("first_name"));
                               Professional_mid_name.setText(document.getString("middle_name"));
                               Professional_last_name.setText(document.getString("last_name"));
                               Professional_bday.setText(document.getString("birthdate"));

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
}