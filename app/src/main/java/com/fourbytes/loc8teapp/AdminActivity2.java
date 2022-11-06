package com.fourbytes.loc8teapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
    private View view;
    private FirebaseStorage storage;
    public Spinner sp_id_type;
    public ImageView image_ids;
    private String username;

    TextView Professional_name;
    TextView Professional_work;
    TextView Professional_first_name;
    TextView Professional_mid_name;
    TextView Professional_last_name;
    TextView Professional_bday;
    ImageView Professional_profile_pic;
    ImageView Professional_id_pic;
    AppCompatButton Accept_button;
    AppCompatButton Reject_button;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("key");
        Log.d("hello", username);

        setContentView(R.layout.activity_admin2);

        sp_id_type=findViewById(R.id.spinner_id_type);
        image_ids=findViewById(R.id.image_ids);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference getImage = databaseReference.child("profilePics");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Professional_name =  findViewById(R.id.prof_name2);
        Professional_work =  findViewById(R.id.prof_work2);
        Professional_first_name = findViewById(R.id.prof_fist_name2);
        Professional_mid_name = findViewById(R.id.prof_middle_name2);
        Professional_last_name = findViewById(R.id.prof_last_name2);
        Professional_bday = findViewById(R.id.prof_bday2);
        Professional_profile_pic = findViewById(R.id.prof_profile_pic);
        Professional_id_pic = findViewById(R.id.prof_id_pic);
        Accept_button = findViewById(R.id.accept_admin);
        Reject_button = findViewById(R.id.reject_admin);



        String [] id_type = {"Select one", "Passport", "Drivers license", "School ID", " Unified Multi-Purpose ID"};

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this, R.layout.spinner_dropdown_layout,id_type);

        sp_id_type.setAdapter(adapter);

        sp_id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 1:
                        image_ids.setImageResource(R.drawable.passport);
                        break;
                    case 2:
                        image_ids.setImageResource(R.drawable.driverslicense_template);
                        break;
                    case 3:
                        image_ids.setImageResource(R.drawable.student_id);
                        break;
                    case 4:
                        image_ids.setImageResource(R.drawable.umid_id);
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


        Reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("professionals").document(username).delete();
                finish();
            }
        });

        Accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("professionals").document(username).update("verified", true);
                finish();
            }
        });


        db.collection("professionals")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Professional_name.setText(task.getResult().getData().get("first_name") + " " + task.getResult().getData().get("middle_name") + " " + task.getResult().getData().get("last_name"));
                            Professional_work.setText(task.getResult().getData().get("specific_job").toString());
                            Professional_first_name.setText(task.getResult().get("first_name").toString());
                            Professional_mid_name.setText(task.getResult().get("middle_name").toString());
                            Professional_last_name.setText(task.getResult().get("last_name").toString());
                            Professional_bday.setText(task.getResult().get("birthdate").toString());

                            StorageReference storageRef = storage.getReference();
                                StorageReference pathReference = storageRef.child("profilePics/" + task.getResult().getData().get("username") + "_profile.jpg");
                                StorageReference pathReference2 = storageRef.child("idPics/" +  task.getResult().getData().get("username") + "_id.jpg");
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
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                                    Professional_id_pic.setImageBitmap(bmp);
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
                });










//        FirebaseFirestore.getInstance()
//                .collection("professionals")
//                .whereEqualTo("verified", false)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(DocumentSnapshot document:task.getResult()){
//
//                               Professional_name.setText(document.getString("first_name") + " " + document.getString("middle_name") + " " + document.getString("last_name"));
//                               Professional_work.setText(document.getString("specific_job"));
//                               Professional_first_name.setText(document.getString("first_name"));
//                               Professional_mid_name.setText(document.getString("middle_name"));
//                               Professional_last_name.setText(document.getString("last_name"));
//                               Professional_bday.setText(document.getString("birthdate"));
//
//                                StorageReference storageRef = storage.getReference();
//                                StorageReference pathReference = storageRef.child("profilePics/" + document.getString("username") + "_profile.jpg");
//                                StorageReference pathReference2 = storageRef.child("idPics/" + document.getString("username") + "_id.jpg");
//                                final long ONE_MEGABYTE = 1024 * 1024;
//                                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                    @Override
//                                    public void onSuccess(byte[] bytes) {
//                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
//                                        Professional_profile_pic.setImageBitmap(bmp);
//                                        Log.d("image_stats", "Image retrieved.");
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d("image_stats","image not retrieved.");
//                                    }
//                                });
//
//                                pathReference2.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                    @Override
//                                    public void onSuccess(byte[] bytes) {
//                                        Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
//                                        Professional_id_pic.setImageBitmap(bmp2);
//                                        Log.d("image_stats", "Image retrieved.");
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d("image_stats","image not retrieved.");
//                                    }
//                                });
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("this",e.getMessage());
//                    }
//                });


    }
}