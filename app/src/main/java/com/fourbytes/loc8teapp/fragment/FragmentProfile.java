package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fourbytes.loc8teapp.ItemViewModel;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.ReviewForProfessional;
import com.fourbytes.loc8teapp.ReviewForProfessionalAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile extends Fragment {
    private View view;

    private ItemViewModel viewModel;

    private RecyclerView rvReviewForProfessional;

    private AppCompatButton btnAddReview;

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
                Toast.makeText(view.getContext(), "Add a review!", Toast.LENGTH_SHORT).show();
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
}