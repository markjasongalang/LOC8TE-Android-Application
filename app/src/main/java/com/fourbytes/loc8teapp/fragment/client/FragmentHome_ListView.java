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
import androidx.fragment.app.FragmentManager;
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

public class FragmentHome_ListView extends Fragment {
    private View view;
    private View l;
    private View l2;
    private View l3;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private AppBarLayout appBarLayout;

    private CheckBox mapViewCheckBox;
    private CheckBox listViewCheckBox;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private FloatingActionButton search_prof_FAB;

    private Button logoutButton;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;
    private Boolean isAllFABVisible3;

    private FragmentManager parentFragmentManager;

    public FragmentHome_ListView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_client, container, false);

        // Get views from layout
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        mapViewCheckBox = view.findViewById(R.id.map_view_checkbox);
        listViewCheckBox = view.findViewById(R.id.list_view_checkbox);
        home_settings_FAB = view.findViewById(R.id.home_settings);
        location_settings_FAB = view.findViewById(R.id.location_settings);
//        search_prof_FAB = view.findViewById(R.id.search_prof_button);
        l = view.findViewById(R.id.home_settings_toolbar);
        l2 = view.findViewById(R.id.location_settings_toolbar);
//        l3 = view.findViewById(R.id.search_prof_field);
        logoutButton = view.findViewById(R.id.logout);

        // Get parent fragment manager (from host activity)
        parentFragmentManager = getParentFragmentManager();

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);
//        l3.setVisibility(view.GONE);
        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        listViewCheckBox.setChecked(true);
        listViewCheckBox.setEnabled(false);

        mapViewCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    listViewCheckBox.setChecked(false);
                    parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment, new FragmentHome_MapView(null), null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        home_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible) {
                    home_settings_FAB.extend();
                    l.setVisibility(view.VISIBLE);
                    isAllFABVisible = true;
                } else {
                    home_settings_FAB.shrink();
                    l.setVisibility(view.GONE);
                    isAllFABVisible = false;
                }
            }
        });

        location_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible2) {
                    location_settings_FAB.extend();
                    l2.setVisibility(view.VISIBLE);
                    isAllFABVisible2 = true;
                } else {
                    location_settings_FAB.shrink();
                    l2.setVisibility(view.GONE);
                    isAllFABVisible2 = false;
                }
            }
        });

//        search_prof_FAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isAllFABVisible3) {
//                    l3.setVisibility(view.VISIBLE);
//                    isAllFABVisible3 = true;
//                } else {
//                    l3.setVisibility(view.GONE);
//                    isAllFABVisible3 = false;
//                }
//            }
//        });

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
            public void onTabSelected(TabLayout.Tab tab) {}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FragmentHome_NewList(), "New");
        adapter.addFragment(new FragmentHome_ConnectedList(), "Connected");

        viewPager.setAdapter(adapter);
    }
}