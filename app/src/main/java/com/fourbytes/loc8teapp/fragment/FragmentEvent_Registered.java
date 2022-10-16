package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_MyEvents_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Professional;


public class FragmentEvent_Registered extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnUnregister;

    private FragmentManager fragmentManager;
    public FragmentEvent_Registered() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_registered, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnUnregister = view.findViewById(R.id.btn_unregister);

        fragmentManager = getParentFragmentManager();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Unregister function

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}