package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;


public class FragmentEvent_Edit extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnSave;
    private AppCompatButton btnSetLocation;
    private TextView btnCancel;

    private FragmentManager fragmentManager;
    public FragmentEvent_Edit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_edit, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSetLocation = view.findViewById(R.id.btn_set_location);
        fragmentManager = getParentFragmentManager();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: function for Save changes
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_SetLocation.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}