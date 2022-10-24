package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ProfileTabAdapter;
import com.fourbytes.loc8teapp.fragment.professional.profiletabs.AboutFragment;
import com.fourbytes.loc8teapp.fragment.professional.profiletabs.CultureFragment;
import com.fourbytes.loc8teapp.fragment.professional.profiletabs.ExperienceFragment;
import com.fourbytes.loc8teapp.fragment.professional.profiletabs.RateFragment;
import com.google.android.material.tabs.TabLayout;

public class FragmentProfile_Professional extends Fragment {
    private View view;

    private FragmentManager fragmentManager;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ProfileTabAdapter profileTabAdapter;

    public FragmentProfile_Professional() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_professional, container, false);

        fragmentManager = getParentFragmentManager();

        // Get views from layout
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        profileTabAdapter = new ProfileTabAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        profileTabAdapter.addFragment(new AboutFragment(), "About");
        profileTabAdapter.addFragment(new RateFragment(), "Rate");
        profileTabAdapter.addFragment(new ExperienceFragment(), "Exp");
        profileTabAdapter.addFragment(new CultureFragment(), "Culture");

        viewPager.setAdapter(profileTabAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
