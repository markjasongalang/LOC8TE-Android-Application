package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_General;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_MyEvents_Professional;

public class FragmentEvent_Client extends Fragment{
    private View view;

    private CardView card_general, card_myEvents;

    public FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_client, container, false);

        fragmentManager = getParentFragmentManager();

        card_general = view.findViewById(R.id.general_events_view);
        card_myEvents = view.findViewById(R.id.my_events_view);

        card_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_General.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });


        card_myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Client.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}