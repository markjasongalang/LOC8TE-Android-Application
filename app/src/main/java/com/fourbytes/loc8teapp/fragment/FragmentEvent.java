package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;

public class FragmentEvent extends Fragment{
    private View view;

    private CardView cvGeneral;

    public FragmentManager fragmentManager;

    public FragmentEvent() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);

        fragmentManager = getParentFragmentManager();

        cvGeneral = view.findViewById(R.id.my_events_view);
        cvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_General.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}