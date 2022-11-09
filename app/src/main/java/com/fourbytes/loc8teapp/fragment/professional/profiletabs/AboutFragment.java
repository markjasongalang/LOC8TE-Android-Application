package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AboutFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private ShapeableImageView ivProfilePicture;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;
    private TextView tvAbout;
    private TextView tvAverageRating;

    private AppCompatButton btnEditProfile;
    private AppCompatButton btnConnect;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText edtAbout;

    private AppCompatButton btnSaveAboutPopup;
    private AppCompatButton btnCancelPopup;
    private AppCompatButton btnReport;

    private CardView cvReviews;

    private SharedViewModel viewModel;

    private Pair pair;

    private String accountType;
    private String username;
    private String current;

    private String viewedUsername;

    private HashMap<String, Object> temp;

    private int numberOfReports;

    private double currentSumRating;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_about, container, false);

        // Get views from layout
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        cvReviews = view.findViewById(R.id.cv_reviews);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnConnect = view.findViewById(R.id.btn_connect);
        tvAbout = view.findViewById(R.id.tv_about);
        tvAverageRating = view.findViewById(R.id.tv_average_rating);
        btnReport = view.findViewById(R.id.btn_report);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        temp = new HashMap<>();
        numberOfReports = 0;
        currentSumRating = 0;

        viewedUsername = DataPasser.getUsername1();

        // Determine the current user
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
            accountType = "client";
        }

        db.collection("professional_reviews").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

        if (accountType.equals("client")) {
            btnEditProfile.setVisibility(View.INVISIBLE);

            db.collection("client_homes").document(current).collection("pro_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        if (documentSnapshot.getId().equals(username) && (boolean) documentSnapshot.getData().get("is_connected")) {
                            btnConnect.setText("disconnect");
                            return;
                        }
                    }
                }
            });

            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> temp = new HashMap<>();
                    if (btnConnect.getText().toString().equals("connect")) {
                        temp.put("is_connected", true);
                        btnConnect.setText("disconnect");

                        HashMap<String, Object> temp2 = new HashMap<>();
                        temp2.put("exists", true);

                        db.collection("pro_homes").document(username).set(temp2);
                        db.collection("pro_homes").document(username).collection("client_list").document(current).set(temp2);
                    } else {
                        temp.put("is_connected", false);
                        btnConnect.setText("connect");
                        db.collection("pro_homes").document(username).collection("client_list").document(current).delete();
                    }

                    db.collection("client_homes").document(current).collection("pro_list").document(username).set(temp);
                }
            });

            db.collection("professional_reviews").document(viewedUsername).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberOfReports++;

                    db.collection("professional_reviews").document(viewedUsername).update("number_of_reports", numberOfReports);
                    if (numberOfReports % 3 == 0) {
                        db.collection("professional_reviews").document(viewedUsername).update("sum_rating", (currentSumRating < 5 ? 0 : currentSumRating - 5));
                    }
                }
            });

        } else {
            btnConnect.setVisibility(View.GONE);
            btnReport.setVisibility(View.GONE);
        }

        temp.put("exists", true);
        db.collection("pro_profiles").document(username).set(temp);

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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("hello_world", username);
                db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                        tvSpecificJob.setText(value.getData().get("specific_job").toString());
                    }
                });
            }
        }, 200);

        cvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment, Fragment_Reviews_About_Pro.class, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        db.collection("pro_profiles").document(username)
                .collection("about")
                .document("data")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            tvAbout.setText(value.getData().get("text").toString());
                        }
                    }
                });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View editAboutPopupView = getLayoutInflater().inflate(R.layout.edit_about_popup, null);

                edtAbout = editAboutPopupView.findViewById(R.id.edt_about);
                btnSaveAboutPopup = editAboutPopupView.findViewById(R.id.btn_save);
                btnCancelPopup = editAboutPopupView.findViewById(R.id.btn_cancel);

                edtAbout.setText(tvAbout.getText());

                dialogBuilder.setView(editAboutPopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnSaveAboutPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();
                        temp.put("text", edtAbout.getText().toString());
                        db.collection("pro_profiles").document(username)
                                .collection("about")
                                .document("data")
                                .set(temp);

                        dialog.dismiss();
                    }
                });

                btnCancelPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return view;
    }
}