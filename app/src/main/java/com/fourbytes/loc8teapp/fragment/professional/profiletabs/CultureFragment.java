package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CultureFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;

    private AppCompatButton btnEditCulture;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText edtReligion;
    private EditText edtLifeMotto;
    private EditText edtLanguages;
    private EditText edtPersonalNote;

    private AppCompatButton btnSaveCulturePopup;
    private AppCompatButton btnCancelPopup;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    private String viewedUsername;

    public CultureFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_culture, container, false);

        // Get views from layout
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        btnEditCulture = view.findViewById(R.id.btn_edit_culture);

        // Initialize values
        db = FirebaseFirestore.getInstance();

        viewedUsername = DataPasser.getUsername();

        // Get username and account type of current user
        if (viewedUsername == null) {
            pair = null;
            viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
                pair = data;
            });

            username = pair.getFirst();
            accountType = pair.getSecond();
        } else {
            username = viewedUsername;
            accountType = "client";
        }

        if (accountType.equals("client")) {
            btnEditCulture.setVisibility(View.GONE);
        }

        db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                tvSpecificJob.setText(value.getData().get("specific_job").toString());
            }
        });

        btnEditCulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View editCulturePopupView = getLayoutInflater().inflate(R.layout.edit_culture_popup, null);

                edtReligion = editCulturePopupView.findViewById(R.id.edt_religion);
                edtLifeMotto = editCulturePopupView.findViewById(R.id.edt_life_motto);
                edtLanguages = editCulturePopupView.findViewById(R.id.edt_languages);
                edtPersonalNote = editCulturePopupView.findViewById(R.id.edt_personal_note);
                btnSaveCulturePopup = editCulturePopupView.findViewById(R.id.btn_save);
                btnCancelPopup = editCulturePopupView.findViewById(R.id.btn_cancel);

                dialogBuilder.setView(editCulturePopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnSaveCulturePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
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