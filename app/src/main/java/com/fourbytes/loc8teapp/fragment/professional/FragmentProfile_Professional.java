package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;

public class FragmentProfile_Professional extends Fragment {
    private View view;

    public FragmentProfile_Professional() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_professional, container, false);


        return view;
    }
}