package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;

public class FragmentEvent_Industry extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private AppCompatButton btnBack;

    public FragmentEvent_Industry() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_industry, container, false);

        fragmentManager = getParentFragmentManager();
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


}