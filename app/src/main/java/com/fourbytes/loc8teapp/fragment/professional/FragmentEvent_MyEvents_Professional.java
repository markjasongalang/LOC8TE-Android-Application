package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_RegisteredEvents;

public class FragmentEvent_MyEvents_Professional extends Fragment {
    private View view;

    private FragmentManager fragmentManager;
    private FragmentManager fragmentManager2;

    private AppCompatButton btnBack;
    private AppCompatButton btnRegistered;
    private AppCompatButton btnCreated;

    public FragmentEvent_MyEvents_Professional() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_myevents_professional, container, false);

        fragmentManager = getParentFragmentManager();
        fragmentManager2 = getChildFragmentManager();
        btnBack = view.findViewById(R.id.btn_back);
        btnRegistered = view.findViewById(R.id.btn_registered);
        btnCreated = view.findViewById(R.id.btn_created);

        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FragmentEvent_RegisteredEvents());
            }
        });

        btnCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FragmentEvent_CreatedEvents());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }
}