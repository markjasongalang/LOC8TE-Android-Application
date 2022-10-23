package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;

public class AboutFragment extends Fragment {
    private View view;

    private FragmentManager parentFragmentManager;

    private CardView cvReviews;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_about, container, false);

        cvReviews = view.findViewById(R.id.cv_reviews);

        parentFragmentManager = getParentFragmentManager();

        cvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}