package com.fourbytes.loc8teapp.fragment.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.SectionPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import javax.annotation.Nullable;

public class FragmentHome_Client extends Fragment {
    private View view;

    ViewPager viewPager;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    FrameLayout map_view_container;
    CheckBox mapview, listview;

    ExtendedFloatingActionButton home_settings_FAB, location_settings_FAB;
    FloatingActionButton search_prof_FAB;
    Button logoutButton;
    Boolean isAllFABVisible, isAllFABVisible2, isAllFABVisible3;

    Fragment map_view_fragment;

    public FragmentHome_Client() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_client, container, false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        mapview = view.findViewById(R.id.map_view_checkbox);
        map_view_container = view.findViewById(R.id.map_view_container);
        map_view_fragment = new FragmentHome_MapView();

        listview = view.findViewById(R.id.list_view_checkbox);

        home_settings_FAB = view.findViewById(R.id.home_settings);
        location_settings_FAB = view.findViewById(R.id.location_settings);
        search_prof_FAB = view.findViewById(R.id.search_prof_button);
        logoutButton = view.findViewById(R.id.logout);
        View l = view.findViewById(R.id.home_settings_toolbar);
        View l2 = view.findViewById(R.id.location_settings_toolbar);
        View l3 = view.findViewById(R.id.search_prof_field);

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);
        l3.setVisibility(view.GONE);
        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        ft.replace(R.id.map_view_container, map_view_fragment);
        ft.commit();
        //map_view_container.bringToFront();
        listview.setChecked(true);
        listview.setEnabled(false);

        mapview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    //view = inflater.inflate(R.layout.fragment_home_map, container, false);
                    appBarLayout.setVisibility(view.GONE);
                    buttonView.setEnabled(false);
                    map_view_container.setClickable(true);
                    map_view_container.setVisibility(view.VISIBLE);
                    viewPager.removeAllViews();

                    listview.setChecked(false);
                    listview.setEnabled(true);
                }
            }
        });

        listview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    appBarLayout.setVisibility(view.VISIBLE);
                    buttonView.setEnabled(false);

                    map_view_container.setVisibility(view.GONE);
                    setUpViewPager(viewPager);

                    mapview.setChecked(false);
                    mapview.setEnabled(true);
                }
            }
        });

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

        search_prof_FAB.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager(ViewPager viewPager){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FragmentHome_NewList(), "New");
        adapter.addFragment(new FragmentHome_ConnectedList(), "Connected");

        viewPager.setAdapter(adapter);
    }

}