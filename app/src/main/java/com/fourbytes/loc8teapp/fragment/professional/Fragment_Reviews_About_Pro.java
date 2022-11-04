package com.fourbytes.loc8teapp.fragment.professional;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ReviewAboutProfessionalAdapter;
import com.fourbytes.loc8teapp.reviewaboutproreycler.ReviewAboutProfessional;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Reviews_About_Pro extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private AppCompatButton btnBack;

    private ShapeableImageView ivProfilePicture;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;

    private RecyclerView rvReviewsAboutPro;

    private List<ReviewAboutProfessional> reviewAboutProfessionalList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;
    private String viewedUsername;
    private String current;

    public Fragment_Reviews_About_Pro() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews_about_pro, container, false);

        // Get views from layout
        btnBack = view.findViewById(R.id.btn_back);
        rvReviewsAboutPro = view.findViewById(R.id.rv_reviews_about_pro);
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();

        viewedUsername = DataPasser.getUsername1();

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });


        if (viewedUsername == null) {
            username = pair.getUsername();
            accountType = pair.getAccountType();
        } else {
            current = pair.getUsername();
            username = viewedUsername;
            accountType = "professional";
        }

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

        db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                tvSpecificJob.setText(value.getData().get("specific_job").toString());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvReviewsAboutPro.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reviewAboutProfessionalList = new ArrayList<>();

        reviewAboutProfessionalList.add(new ReviewAboutProfessional(
                "Jason",
                "",
                "Galang",
                "Respectful client. Very pleasant to work with!",
                "01/21/2022",
                5.0
        ));

        reviewAboutProfessionalList.add(new ReviewAboutProfessional(
                "Mark",
                "",
                "Banuelos",
                "Good person.",
                "08/09/2022",
                5.0
        ));

        rvReviewsAboutPro.setAdapter(new ReviewAboutProfessionalAdapter(view.getContext(), reviewAboutProfessionalList));
    }
}