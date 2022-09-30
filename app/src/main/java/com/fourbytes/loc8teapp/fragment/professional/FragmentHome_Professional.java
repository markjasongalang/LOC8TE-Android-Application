package com.fourbytes.loc8teapp.fragment.professional;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.client.FragmentHome_ConnectedList;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentHome_Professional extends Fragment {

    private View view;

    FrameLayout connected_list_container;

    ExtendedFloatingActionButton home_settings_FAB, location_settings_FAB;
    FloatingActionButton search_cli_FAB;
    Button logoutButton;
    Boolean isAllFABVisible, isAllFABVisible2, isAllFABVisible3;

    Fragment connected_list_fragment;

    public FragmentHome_Professional() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_professional, container, false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        connected_list_container = view.findViewById(R.id.connected_list_container);
        connected_list_fragment = new FragmentHome_ConnectedList();

        logoutButton = view.findViewById(R.id.logout);

        home_settings_FAB = view.findViewById(R.id.home_settings_prof);
        location_settings_FAB = view.findViewById(R.id.location_settings_prof);
        search_cli_FAB = view.findViewById(R.id.search_cli_button);

        View l = view.findViewById(R.id.home_settings_toolbar_prof);
        View l2 = view.findViewById(R.id.location_settings_toolbar_prof);
        View l3 = view.findViewById(R.id.search_cli_field);

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);
        l3.setVisibility(view.GONE);
        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        ft.replace(R.id.connected_list_container, connected_list_fragment);
        ft.commit();

        home_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isAllFABVisible){
                    home_settings_FAB.extend();
                    l.setVisibility(view.VISIBLE);
                    isAllFABVisible = true;
                }else{
                    home_settings_FAB.shrink();
                    l.setVisibility(view.GONE);
                    isAllFABVisible = false;
                }

            }
        });

        location_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isAllFABVisible2){
                    location_settings_FAB.extend();
                    l2.setVisibility(view.VISIBLE);
                    isAllFABVisible2 = true;
                }else{
                    location_settings_FAB.shrink();
                    l2.setVisibility(view.GONE);
                    isAllFABVisible2 = false;
                }

            }
        });

        search_cli_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isAllFABVisible3){
                    l3.setVisibility(view.VISIBLE);
                    isAllFABVisible3 = true;
                }else{
                    l3.setVisibility(view.GONE);
                    isAllFABVisible3 = false;
                }

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }
}