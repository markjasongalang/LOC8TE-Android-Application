package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class CultureFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;
    private TextView tvReligion;
    private TextView tvLifeMotto;
    private TextView tvLanguages;
    private TextView tvPersonalNote;

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

    private HashMap<String, Object> temp;

    public CultureFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_culture, container, false);

        // Get views from layout
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        btnEditCulture = view.findViewById(R.id.btn_edit_culture);
        tvReligion = view.findViewById(R.id.tv_religion);
        tvLifeMotto = view.findViewById(R.id.tv_life_motto);
        tvLanguages = view.findViewById(R.id.tv_languages);
        tvPersonalNote = view.findViewById(R.id.tv_personal_note);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        temp = new HashMap<>();

        viewedUsername = DataPasser.getUsername1();

        // Get username and account type of current user
        if (viewedUsername == null) {
            pair = null;
            viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
                pair = data;
            });

            username = pair.getUsername();
            accountType = pair.getAccountType();
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
                if (value.exists()) {
                    tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                    tvSpecificJob.setText(value.getData().get("specific_job").toString());
                }
            }
        });

        temp.put("exists", true);
        db.collection("pro_profiles").document(username).set(temp);

        db.collection("pro_profiles")
                .document(username)
                .collection("culture")
                .document("data").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            tvReligion.setText(value.getData().get("religion").toString());
                            tvLifeMotto.setText(value.getData().get("life_motto").toString());
                            tvLanguages.setText(value.getData().get("languages").toString());
                            tvPersonalNote.setText(value.getData().get("personal_note").toString());
                        }
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

                edtReligion.setText(tvReligion.getText());
                edtLifeMotto.setText(tvLifeMotto.getText());
                edtLanguages.setText(tvLanguages.getText());
                edtPersonalNote.setText(tvPersonalNote.getText());

                dialogBuilder.setView(editCulturePopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnSaveCulturePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();

                        temp.put("religion", edtReligion.getText().toString());
                        temp.put("life_motto", edtLifeMotto.getText().toString());
                        temp.put("languages", edtLanguages.getText().toString());
                        temp.put("personal_note", edtPersonalNote.getText().toString());

                        db.collection("pro_profiles").document(username).collection("culture").document("data").set(temp);

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