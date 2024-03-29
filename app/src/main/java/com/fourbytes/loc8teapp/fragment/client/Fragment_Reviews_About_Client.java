package com.fourbytes.loc8teapp.fragment.client;

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
import com.fourbytes.loc8teapp.adapter.ReviewAboutClientAdapter;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Reviews_About_Client extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private TextView tvClientName;
    private TextView tvAverageRating;

    private ShapeableImageView ivProfilePicture;

    private AppCompatButton btnBack;

    private RecyclerView rvReviewsAboutClient;

    private List<ReviewAboutClient> reviewAboutClientList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;
    private String viewedUsername;
    private String current;

    public Fragment_Reviews_About_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews_about_client, container, false);

        // Get views from layout
        btnBack = view.findViewById(R.id.btn_back);
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        tvClientName = view.findViewById(R.id.tv_client_name);
        rvReviewsAboutClient = view.findViewById(R.id.rv_reviews_about_client);
        tvAverageRating = view.findViewById(R.id.tv_average_rating);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get fragment manager of parent fragment
        parentFragmentManager = getParentFragmentManager();

        viewedUsername = DataPasser.getUsername2();

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

        db.collection("client_profiles").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.getData().get("sum_rating") != null) {
                        double sumRating = Double.valueOf(value.getData().get("sum_rating").toString());
                        double numberOfRatings = Double.valueOf(value.getData().get("number_of_ratings").toString());

                        tvAverageRating.setText((String.format("%.2f", (sumRating / numberOfRatings))));
                    } else {
                        tvAverageRating.setText("no rating yet");
                    }
                } else {
                    tvAverageRating.setText("no rating yet");
                }
            }
        });

        // Get full name of current user
        db.collection("clients").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String firstName = value.getData().get("first_name").toString();
                String lastName = value.getData().get("last_name").toString();
                tvClientName.setText(firstName + " " + lastName);
            }
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

        rvReviewsAboutClient.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reviewAboutClientList = new ArrayList<>();
        db.collection("client_profiles").document(username).collection("reviews_about_client").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                reviewAboutClientList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.getId().equals("sample_review")) {
                        continue;
                    }

                    reviewAboutClientList.add(new ReviewAboutClient(
                            documentSnapshot.getData().get("from").toString(),
                            (double) documentSnapshot.getData().get("rating"),
                            documentSnapshot.getData().get("review").toString(),
                            documentSnapshot.getData().get("timestamp").toString()
                    ));
                }
                rvReviewsAboutClient.setAdapter(new ReviewAboutClientAdapter(view.getContext(), reviewAboutClientList));

            }
        });

    }
}