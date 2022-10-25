package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.Fragment_Reviews_About_Pro;

public class AboutFragment extends Fragment {
    private View view;

    private FragmentManager parentFragmentManager;

    private CardView cvReviews;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_about, container, false);

        parentFragmentManager = getParentFragmentManager();

        // Get views from layout
        cvReviews = view.findViewById(R.id.cv_reviews);

        // todo: OPEN REVIEWS ABOUT PROFESSIONAL (fix)

        cvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}