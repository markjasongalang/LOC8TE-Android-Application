package com.fourbytes.loc8teapp.fragment.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.SectionPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import javax.annotation.Nullable;

public class FragmentHome_ListView extends Fragment {
    private View view;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private AppBarLayout appBarLayout;

    private FragmentManager parentFragmentManager;

    private AppCompatButton btnMapView;

    public FragmentHome_ListView() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_client, container, false);

        // Get views from layout
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        btnMapView = view.findViewById(R.id.btn_map_view);

        // Get parent fragment manager (from host activity)
        parentFragmentManager = getParentFragmentManager();

        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, new FragmentHome_MapView(null), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
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

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FragmentHome_NewList(), "New");
        adapter.addFragment(new FragmentHome_ConnectedList(), "Connected");

        viewPager.setAdapter(adapter);
    }
}