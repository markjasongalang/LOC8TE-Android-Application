package com.fourbytes.loc8teapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.ItemViewModel;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.ReviewForProfessional;
import com.fourbytes.loc8teapp.ReviewForProfessionalAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;

    private ItemViewModel viewModel;

    private RecyclerView rvReviewForProfessional;

    private AppCompatButton btnAddReview;
    private AppCompatButton btnRate;
    private AppCompatButton btnCancel;

    private EditText edtReview;

    private Spinner spProfessional;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public FragmentProfile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        view = getViewBasedOnAccountType(inflater, container);

        // Get views from layout
        rvReviewForProfessional = view.findViewById(R.id.rv_review_for_professional);
        btnAddReview = view.findViewById(R.id.btn_add_review);

        rvReviewForProfessional.setLayoutManager(new LinearLayoutManager(view.getContext()));
        List<ReviewForProfessional> reviewForProfessionals = new ArrayList<>();
        reviewForProfessionals.add(new ReviewForProfessional(
                "Julia",
                "Cruz",
                "Santos",
                "IT Specialist",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vitae tellus turpis. ",
                        "03/31/2022",
                        3.45
                        ));

        reviewForProfessionals.add(new ReviewForProfessional(
                "Elon",
                "Robot",
                "Musk",
                "Rocket Engineer",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vitae tellus turpis. ",
                "05/21/2022",
                4.3
        ));

        reviewForProfessionals.add(new ReviewForProfessional(
                "Joshua Matthew",
                "Secret",
                "Padilla",
                "100K Android Developer",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vitae tellus turpis. ",
                "12/25/2021",
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
                spProfessional.setOnItemSelectedListener(FragmentProfile.this);

                dialogBuilder.setView(rateProfessionalPopupView);
                dialog = dialogBuilder.create();
                dialog.show();
                dialog.getWindow().setLayout(1050, 1350);

                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Nice rating tho", Toast.LENGTH_SHORT).show();
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

        return view;
    }

    private View getViewBasedOnAccountType(LayoutInflater inflater, ViewGroup container) {
        String accountType = viewModel.getSelectedItem().getValue();
        if (accountType.equals("client")) {
            view = inflater.inflate(R.layout.client_fragment_profile, container, false);
        } else {
            view = inflater.inflate(R.layout.professional_fragment_profile, container, false);
        }
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

        if (pos == 2) {
            Toast.makeText(view.getContext(), "Pogi!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}