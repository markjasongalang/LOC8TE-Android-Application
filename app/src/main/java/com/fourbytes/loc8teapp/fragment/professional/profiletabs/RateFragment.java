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
import com.fourbytes.loc8teapp.adapter.ServiceAdapter;
import com.fourbytes.loc8teapp.fragment.client.FragmentProfile_Client;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class RateFragment extends Fragment {
    private View view;

    private FirebaseFirestore db;

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

        viewedUsername = DataPasser.getUsername1();

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
            btnAddService.setVisibility(View.GONE);
        }

        db.collection("professionals").document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvProfessionalName.setText(value.getData().get("first_name") + " " + value.getData().get("last_name").toString());
                tvSpecificJob.setText(value.getData().get("specific_job").toString());
            }
        });

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View addServicePopupView = getLayoutInflater().inflate(R.layout.add_service_popup, null);

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
                        Toast.makeText(getContext(), "Successfully added!", Toast.LENGTH_SHORT).show();
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

        serviceItemList.add(new ServiceItem(
                "Live Cooking Show",
                101.69,
                "per hour",
                "I love to cook in front of the panelists <3"
        ));

        serviceItemList.add(new ServiceItem(
                "Hero Duties",
                99999.99,
                "depends on the calamity",
                "Class S Hero to the rescue!"
        ));

        serviceItemList.add(new ServiceItem(
                "Recycler View Lover",
                43.75,
                "per hour",
                "Protect Rylie at all cost..."
        ));

        rvService.setAdapter(new ServiceAdapter(view.getContext(), serviceItemList));

    }
}