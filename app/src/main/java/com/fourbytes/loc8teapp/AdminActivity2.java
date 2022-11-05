package com.fourbytes.loc8teapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminActivity2 extends AppCompatActivity {

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db;
    private View view;
    private FirebaseStorage storage;
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

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//        DatabaseReference databaseReference = firebaseDatabase.getReference();
//
//        DatabaseReference getImage = databaseReference.child("profilePics");

        storage = FirebaseStorage.getInstance();

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
                        image_ids.setImageResource(R.drawable.drivers_license_template);
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

                                StorageReference storageRef = storage.getReference();
                                StorageReference pathReference = storageRef.child("profilePics/" + document.getString("username") + "_profile.jpg");
                                StorageReference pathReference2 = storageRef.child("idPics/" + document.getString("username") + "_id.jpg");
                                final long ONE_MEGABYTE = 1024 * 1024;
                                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                                        Professional_profile_pic.setImageBitmap(bmp);
                                        Log.d("image_stats", "Image retrieved.");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("image_stats","image not retrieved.");
                                    }
                                });

                                pathReference2.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                                        Professional_id_pic.setImageBitmap(bmp2);
                                        Log.d("image_stats", "Image retrieved.");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("image_stats","image not retrieved.");
                                    }
                                });
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