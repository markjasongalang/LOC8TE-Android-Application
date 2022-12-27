package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private AppCompatButton btnBack;

    private ShapeableImageView ivProfilePicture;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;
    private TextView tvAverageRating;

    private RecyclerView rvReviewsAboutPro;

    private List<ReviewAboutProfessional> reviewAboutProfessionalList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;
    private String viewedUsername;
    private String current;

    public ReviewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);

        // Get views from layout
        btnBack = view.findViewById(R.id.btn_back);
        rvReviewsAboutPro = view.findViewById(R.id.rv_reviews_about_pro);
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        tvAverageRating = view.findViewById(R.id.tv_average_rating);

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

        db.collection("professional_reviews").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.getData().get("sum_rating") != null && value.getData().get("number_of_ratings") != null) {
                        double sumRating = Double.valueOf(value.getData().get("sum_rating").toString());
                        double numberOfRatings = Double.valueOf(value.getData().get("number_of_ratings").toString());

                        tvAverageRating.setText((String.format("%.2f", (sumRating / numberOfRatings))));
                    } else {
                        tvAverageRating.setText("new");
                    }
                } else {
                    tvAverageRating.setText("new");
                }
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

        db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                    tvSpecificJob.setText(value.getData().get("specific_job").toString());
                }
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
        db.collection("professional_reviews").document(username).collection("reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                reviewAboutProfessionalList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.getId().equals("sample_review")) {
                        continue;
                    }

                    reviewAboutProfessionalList.add(new ReviewAboutProfessional(
                            documentSnapshot.getData().get("from").toString(),
                            (double) documentSnapshot.getData().get("rating"),
                            documentSnapshot.getData().get("review").toString(),
                            documentSnapshot.getData().get("timestamp").toString()
                    ));
                }
                rvReviewsAboutPro.setAdapter(new ReviewAboutProfessionalAdapter(view.getContext(), reviewAboutProfessionalList));

            }
        });

    }
}