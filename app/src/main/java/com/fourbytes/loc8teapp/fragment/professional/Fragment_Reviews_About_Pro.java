package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.profiletabs.AboutFragment;

public class Fragment_Reviews_About_Pro extends Fragment {
    private View view;

    private FragmentManager parentFragmentManager;

    private AppCompatButton btnBack;

    private RecyclerView rvReviewsAboutPro;

    public Fragment_Reviews_About_Pro() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews_about_pro, container, false);

        parentFragmentManager = getParentFragmentManager();

        // Get views from layout
        btnBack = view.findViewById(R.id.btn_back);
        rvReviewsAboutPro = view.findViewById(R.id.rv_reviews_about_pro);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        return view;
    }
}