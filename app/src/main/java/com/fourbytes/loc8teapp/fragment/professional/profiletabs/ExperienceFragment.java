package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fourbytes.loc8teapp.adapter.ExperienceAdapter;
import com.fourbytes.loc8teapp.adapter.ServiceAdapter;
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceItem;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExperienceFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;

    private AppCompatButton btnAddExperience;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText edtPosition;
    private EditText edtCompany;
    private EditText edtDescription;

    private AppCompatButton btnAddExperiencePopup;
    private AppCompatButton btnCancelPopup;

    private RecyclerView rvExperience;

    private List<ExperienceItem> experienceItemList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    private String viewedUsername;

    private HashMap<String, Object> temp;

    public ExperienceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_experience, container, false);

        // Get views from layout
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        btnAddExperience = view.findViewById(R.id.btn_add_experience);
        rvExperience = view.findViewById(R.id.rv_experience);

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
            btnAddExperience.setVisibility(View.GONE);
        }

        db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                tvSpecificJob.setText(value.getData().get("specific_job").toString());
            }
        });

        temp.put("exists", true);
        db.collection("pro_profiles").document(username).set(temp);

        btnAddExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View addExperiencePopupView = getLayoutInflater().inflate(R.layout.add_experience_popup, null);

                edtPosition = addExperiencePopupView.findViewById(R.id.edt_position);
                edtCompany = addExperiencePopupView.findViewById(R.id.edt_company);
                edtDescription = addExperiencePopupView.findViewById(R.id.edt_description);
                btnAddExperiencePopup = addExperiencePopupView.findViewById(R.id.btn_add);
                btnCancelPopup = addExperiencePopupView.findViewById(R.id.btn_cancel);

                dialogBuilder.setView(addExperiencePopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnAddExperiencePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();

                        temp.put("position", edtPosition.getText().toString());
                        temp.put("company", edtCompany.getText().toString());
                        temp.put("description", edtDescription.getText().toString());

                        db.collection("pro_profiles").document(username).collection("experience").document().set(temp);

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

    @Override
    public void onStart() {
        super.onStart();

        rvExperience.setLayoutManager(new LinearLayoutManager(view.getContext()));

        experienceItemList = new ArrayList<>();
        db.collection("pro_profiles").document(username).collection("experience").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experienceItemList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    experienceItemList.add(new ExperienceItem(
                            documentSnapshot.getId(),
                            documentSnapshot.getData().get("position").toString(),
                            documentSnapshot.getData().get("company").toString(),
                            documentSnapshot.getData().get("description").toString()
                    ));
                }

                rvExperience.setAdapter(new ExperienceAdapter(view.getContext(), experienceItemList, getLayoutInflater(), username));
            }
        });

//        experienceItemList.add(new ExperienceItem(
//                "Head Chef",
//                "Netflix",
//                "I was the no. 1 chef of the world at that time."
//        ));
//
//        experienceItemList.add(new ExperienceItem(
//                "Former Class S Hero",
//                "Jollibee",
//                "I was the no. 1 hero of the world at that time."
//        ));
//
//        experienceItemList.add(new ExperienceItem(
//                "Android Lord",
//                "Apple",
//                "I was the no. 1 android lord of the world at that time."
//        ));
    }
}