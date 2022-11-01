package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.fourbytes.loc8teapp.fragment.professional.Fragment_Reviews_About_Pro;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AboutFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private ShapeableImageView ivProfilePicture;

    private SharedViewModel viewModel;

    private String username;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_about, container, false);

        // Get views from layout
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get username of current user
        username = "";
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            username = data;
        });

        // Get profile picture of current user
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("profilePics/" + username + "_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivProfilePicture.setImageBitmap(bmp);
                Log.d("image_stats", "Image retrieved.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("image_stats", "Image not retrieved.");
            }
        });

        return view;
    }
}