package com.fourbytes.loc8teapp.fragment.professional;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourbytes.loc8teapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragmentEvent_CreatorView extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnEdit;

    private TextView event_title;
    private TextView event_location;
    private TextView event_description;
    private TextView event_date;
    private TextView event_time;
    private TextView host_name;
    private TextView host_profession;
    private TextView event_participant_count;
    private TextView event_parking_count;
    private ImageView host_img;

    private String doc_event_title;
    private String doc_event_location;
    private String doc_event_desc;
    private String doc_event_date;
    private String doc_event_time;
    private String doc_host;
    private String doc_host_id;
    private String doc_host_profession;
    private String doc_event_id;
    private String doc_event_participant_count;
    private String doc_event_parking_count;
    private String doc_event_parking_limit;

    private double event_latitude;
    private double event_longitude;

    private FirebaseFirestore db;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FragmentManager fragmentManager;
    public FragmentEvent_CreatorView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_creatorview, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnEdit = view.findViewById(R.id.btn_edit);
        event_title = view.findViewById(R.id.event_title);
        event_location = view.findViewById(R.id.event_location);
        event_description = view.findViewById(R.id.event_desc);
        event_date = view.findViewById(R.id.event_date);
        event_time = view.findViewById(R.id.event_time);
        host_name = view.findViewById(R.id.host_name);
        host_profession = view.findViewById(R.id.host_profession);
        event_participant_count = view.findViewById(R.id.participants_count);
        event_parking_count = view.findViewById(R.id.parking_count);
        host_img = view.findViewById(R.id.host_img);


        fragmentManager = getParentFragmentManager();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putString("title", doc_event_title);
                result.putString("location", doc_event_location);
                result.putString("description", doc_event_desc);
                result.putInt("parking_count", Integer.parseInt(doc_event_parking_count));
                result.putString("date", doc_event_date);
                result.putString("time", doc_event_time);
                result.putString("event_id", doc_event_id);
                result.putString("location", doc_event_location);
                result.putDouble("latitude", event_latitude);
                result.putDouble("longitude", event_longitude);

                fragmentManager.setFragmentResult("editData", result);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Edit.class, null)
                        .commit();
            }
        });

        fragmentManager.setFragmentResultListener("eventData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                doc_event_title = result.getString("title");
                doc_event_location = result.getString("location");
                doc_event_desc = result.getString("description");
                doc_event_date = result.getString("date");
                doc_event_time = result.getString("time");
                doc_event_parking_count = result.getString("host");
                doc_host_profession = result.getString("host_job");
                doc_event_id = result.getString("event_id");
                doc_event_participant_count = String.valueOf(result.getInt("participant"));
                doc_event_parking_count = String.valueOf(result.getInt("parking_limit") - result.getInt("parking_count"));
                doc_host_id = result.getString("host_id");
                event_latitude = result.getDouble("latitude");
                event_longitude = result.getDouble("longitude");

                StorageReference storageRef = storage.getReference();
                StorageReference pathReference = storageRef.child("profilePics/" + doc_host_id + "_profile.jpg");

                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        host_img.setImageBitmap(bmp);
                    }
                });
                event_title.setText(doc_event_title);
                event_location.setText(doc_event_location);
                event_description.setText(doc_event_desc);
                event_participant_count.setText(doc_event_participant_count);
                event_parking_count.setText(doc_event_parking_count);
                host_name.setText(doc_host);
                host_profession.setText(doc_host_profession);
                event_date.setText(doc_event_date);
                event_time.setText(doc_event_time);
            }
        });

        return view;
    }
}