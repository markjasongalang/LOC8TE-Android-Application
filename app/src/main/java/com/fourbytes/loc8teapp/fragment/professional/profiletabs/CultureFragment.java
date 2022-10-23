package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;

public class CultureFragment extends Fragment {
    private View view;

    public CultureFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_culture, container, false);

        // TODO: Add editable input box

        return view;
    }
}