package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ExperienceAdapter;
import com.fourbytes.loc8teapp.adapter.ReviewAboutClientAdapter;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClient;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Reviews_About_Client extends Fragment {
    private View view;

    private FragmentManager parentFragmentManager;

    private AppCompatButton btnBack;

    private RecyclerView rvReviewsAboutClient;

    private List<ReviewAboutClient> reviewAboutClientList;

    public Fragment_Reviews_About_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews_about_client, container, false);

        // Get views from layout
        btnBack = view.findViewById(R.id.btn_back);
        rvReviewsAboutClient = view.findViewById(R.id.rv_reviews_about_client);

        parentFragmentManager = getParentFragmentManager();

        reviewAboutClientList = new ArrayList<>();

        reviewAboutClientList.add(new ReviewAboutClient(
                "Julia",
                "",
                "Santos",
                "IT Specialist",
                "Respectful client. Very pleasant to work with!",
                "01/21/2022",
                5.0
        ));

        reviewAboutClientList.add(new ReviewAboutClient(
                "Pablo",
                "",
                "Delos Reyes",
                "Software Engineer",
                "Good person.",
                "08/09/2022",
                5.0
        ));

        rvReviewsAboutClient.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvReviewsAboutClient.setAdapter(new ReviewAboutClientAdapter(view.getContext(), reviewAboutClientList));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        return view;
    }
}