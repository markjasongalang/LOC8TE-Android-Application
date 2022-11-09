package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.app.AlertDialog;
import android.os.Bundle;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ServiceAdapter;
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

public class RateFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FragmentManager parentFragmentManager;

    private TextView tvProfessionalName;
    private TextView tvSpecificJob;

    private AppCompatButton btnAddService;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText edtServiceName;
    private EditText edtPrice;
    private EditText edtRate;
    private EditText edtDescription;

    private AppCompatButton btnAddServicePopup;
    private AppCompatButton btnCancelPopup;

    private RecyclerView rvService;

    private List<ServiceItem> serviceItemList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    private String viewedUsername;

    private HashMap<String, Object> temp;

    private int current;

    private LayoutInflater layoutInflater;

    public RateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_rate, container, false);

        // Get views from layout
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);
        tvSpecificJob = view.findViewById(R.id.tv_specific_job);
        btnAddService = view.findViewById(R.id.btn_add_service);
        rvService = view.findViewById(R.id.rv_service);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        temp = new HashMap<>();
        parentFragmentManager = getParentFragmentManager();
        layoutInflater = getLayoutInflater();

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
            btnAddService.setVisibility(View.GONE);
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

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View addServicePopupView = layoutInflater.inflate(R.layout.add_service_popup, null);

                edtServiceName = addServicePopupView.findViewById(R.id.edt_service_name);
                edtPrice = addServicePopupView.findViewById(R.id.edt_price);
                edtRate = addServicePopupView.findViewById(R.id.edt_rate);
                edtDescription = addServicePopupView.findViewById(R.id.edt_description);
                btnAddServicePopup = addServicePopupView.findViewById(R.id.btn_add);
                btnCancelPopup = addServicePopupView.findViewById(R.id.btn_cancel);

                dialogBuilder.setView(addServicePopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                btnAddServicePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();

                        temp.put("service_name", edtServiceName.getText().toString());
                        temp.put("price", edtPrice.getText().toString());
                        temp.put("rate_type", edtRate.getText().toString());
                        temp.put("description", edtDescription.getText().toString());

                        db.collection("pro_profiles")
                                .document(username)
                                .collection("rate")
                                .document()
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

    @Override
    public void onStart() {
        super.onStart();

        rvService.setLayoutManager(new LinearLayoutManager(view.getContext()));

        serviceItemList = new ArrayList<>();
        db.collection("pro_profiles").document(username).collection("rate").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                serviceItemList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    serviceItemList.add(new ServiceItem(
                            documentSnapshot.getId(),
                            documentSnapshot.getData().get("service_name").toString(),
                            Double.valueOf(documentSnapshot.getData().get("price").toString()),
                            documentSnapshot.getData().get("rate_type").toString(),
                            documentSnapshot.getData().get("description").toString()
                    ));
                }

                rvService.setAdapter(new ServiceAdapter(view.getContext(), serviceItemList, layoutInflater, username));
            }
        });
    }
}