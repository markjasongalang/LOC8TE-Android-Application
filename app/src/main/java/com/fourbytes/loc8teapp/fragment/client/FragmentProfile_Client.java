package com.fourbytes.loc8teapp.fragment.client;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile_Client extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvReviewForProfessional;

    private AppCompatButton btnAddReview;
    private AppCompatButton btnRate;
    private AppCompatButton btnCancel;

    private EditText edtReview;

    private Spinner spProfessional;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private CardView cvReviews;

    private Pair pair;

    private String username;
    private String accountType;

    private SharedViewModel viewModel;

    private TextView tvClientName;

    private ShapeableImageView ivProfilePicture;

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

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Parent fragment manager
        parentFragmentManager = getParentFragmentManager();

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getUsername();
        accountType = pair.getAccountType();

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

        rvReviewForProfessional.setLayoutManager(new LinearLayoutManager(view.getContext()));
        List<ReviewForProfessional> reviewForProfessionals = new ArrayList<>();
        reviewForProfessionals.add(new ReviewForProfessional(
                "Julia",
                "Cruz",
                "Santos",
                "IT Specialist",
                "She was very hardworking.",
                "03/31/2022",
                3.45
        ));

        reviewForProfessionals.add(new ReviewForProfessional(
                "Elon",
                "Robot",
                "Musk",
                "Rocket Engineer",
                "This guy is so brilliant!",
                "05/21/2022",
                4.3
        ));

        reviewForProfessionals.add(new ReviewForProfessional(
                "Joshua Matthew",
                "Secret",
                "Padilla",
                "100K Android Developer",
                "He is a genius in his line of work.",
                "12/25/2021",
                3.78
        ));

        reviewForProfessionals.add(new ReviewForProfessional(
                "Mary Angeline",
                "",
                "Corral",
                "Software Tester",
                "She did a good job.",
                "03/31/2022",
                3.78
        ));

        rvReviewForProfessional.setAdapter(new ReviewForProfessionalAdapter(view.getContext(), reviewForProfessionals));

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View rateProfessionalPopupView = getLayoutInflater().inflate(R.layout.rate_professional_popup, null);

                spProfessional = rateProfessionalPopupView.findViewById(R.id.sp_professional);
                btnRate = rateProfessionalPopupView.findViewById(R.id.btn_rate);
                btnCancel = rateProfessionalPopupView.findViewById(R.id.btn_cancel);
                edtReview = rateProfessionalPopupView.findViewById(R.id.edt_review);

                initSpinnerProfessional();
                spProfessional.setOnItemSelectedListener(FragmentProfile_Client.this);

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

    private void initSpinnerProfessional() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.sample_names_array,
                R.layout.spinner_dropdown_layout
        );

        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spProfessional.setAdapter(adapter);
    }

    /* The methods below are needed to be overridden for the spinner action listener. */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        edtReview.setText("");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}