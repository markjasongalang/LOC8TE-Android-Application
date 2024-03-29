package com.fourbytes.loc8teapp.fragment.client;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
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
import com.fourbytes.loc8teapp.adapter.ReviewForProfessionalAdapter;
import com.fourbytes.loc8teapp.reviewforprorecycler.ReviewForProfessional;
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
import java.util.HashMap;
import java.util.List;

public class FragmentProfile_Client extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvReviewForProfessional;

    private AppCompatButton btnAddReview;
    private AppCompatButton btnRate;
    private AppCompatButton btnCancel;
    private AppCompatButton btnReport;

    private EditText edtReview;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private CardView cvReviews;

    private Pair pair;

    private String username;
    private String accountType;
    private String viewedUsername;
    private String current;

    private SharedViewModel viewModel;

    private TextView tvClientName;
    private TextView tvClientNamePopup;
    private TextView tvAverageRating;

    private ShapeableImageView ivProfilePicture;

    private HashMap<String, Object> temp;

    private List<ReviewForProfessional> reviewForProfessionals;

    private int numberOfReports;

    private double currentSumRating;

    public FragmentProfile_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_client, container, false);

        // Get views from layout
        rvReviewForProfessional = view.findViewById(R.id.rv_review_for_professional);
        btnAddReview = view.findViewById(R.id.btn_add_review);
        cvReviews = view.findViewById(R.id.cv_reviews);
        tvClientName = view.findViewById(R.id.tv_client_name);
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        btnReport = view.findViewById(R.id.btn_report);
        tvAverageRating = view.findViewById(R.id.tv_average_rating);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        temp = new HashMap<>();
        currentSumRating = 0;
        numberOfReports = 0;

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

            temp.clear();
            temp.put("exists", true);
            db.collection("client_profiles").document(username).update(temp);

            db.collection("client_profiles")
                    .document(username)
                    .collection("reviews_for_professionals")
                    .document("sample_review")
                    .set(temp);

            db.collection("client_profiles")
                    .document(username)
                    .collection("reviews_about_client")
                    .document("sample_review")
                    .set(temp);
        } else {
            current = pair.getUsername();
            username = viewedUsername;
            accountType = "professional";

            db.collection("pro_homes")
                    .document(current)
                    .collection("client_list")
                    .document(username)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()) {
                                if (value.getData().get("is_reported") != null) {
                                    if ((boolean) value.getData().get("is_reported")) {
                                        btnReport.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }
                    });

            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewedUsername != null) {
                        numberOfReports++;

                        db.collection("client_profiles")
                                .document(viewedUsername)
                                .update("number_of_reports", numberOfReports);

                        if (numberOfReports % 3 == 0) {
                            db.collection("client_profiles")
                                    .document(viewedUsername)
                                    .update("sum_rating", (currentSumRating < 5 ? 0 : currentSumRating - 5));
                        }

                        db.collection("pro_homes")
                                .document(current)
                                .collection("client_list")
                                .document(username)
                                .update("is_reported", true);
                    }
                }
            });
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
                        tvAverageRating.setText("none");
                    }
                } else {
                    tvAverageRating.setText("none");
                }
            }
        });

        if (viewedUsername != null) {
            db.collection("client_profiles").document(viewedUsername).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.getData().get("sum_rating") != null) {
                        currentSumRating = Double.valueOf(value.getData().get("sum_rating").toString());
                    }

                    if (value.getData().get("number_of_reports") != null) {
                        numberOfReports = Integer.valueOf(value.getData().get("number_of_reports").toString());
                    }
                }
            });
        }

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

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View rateProfessionalPopupView = getLayoutInflater().inflate(R.layout.rate_professional_popup, null);

                btnRate = rateProfessionalPopupView.findViewById(R.id.btn_rate);
                btnCancel = rateProfessionalPopupView.findViewById(R.id.btn_cancel);
                edtReview = rateProfessionalPopupView.findViewById(R.id.edt_review);
                tvClientNamePopup = rateProfessionalPopupView.findViewById(R.id.tv_client_name_popup);

                dialogBuilder.setView(rateProfessionalPopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Successfully rated!", Toast.LENGTH_SHORT).show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        cvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, Fragment_Reviews_About_Client.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (accountType.equals("professional")) {
            btnReport.setVisibility(View.VISIBLE);
            btnAddReview.setVisibility(View.GONE);
        }

        rvReviewForProfessional.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reviewForProfessionals = new ArrayList<>();
        db.collection("client_profiles").document(username).collection("reviews_for_professionals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                reviewForProfessionals = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.getId().equals("sample_review")) {
                        continue;
                    }

                    reviewForProfessionals.add(new ReviewForProfessional(
                            documentSnapshot.getData().get("to").toString(),
                            (double) documentSnapshot.getData().get("rating"),
                            documentSnapshot.getData().get("review").toString(),
                            documentSnapshot.getData().get("timestamp").toString()
                    ));
                }
                rvReviewForProfessional.setAdapter(new ReviewForProfessionalAdapter(view.getContext(), reviewForProfessionals));

            }
        });
    }
}