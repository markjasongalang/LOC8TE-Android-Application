package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.fourbytes.loc8teapp.fragment.professional.Fragment_Reviews_About_Pro;

public class AboutFragment extends Fragment {
    private View view;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_about, container, false);

        return view;
    }
}