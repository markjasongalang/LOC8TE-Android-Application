package com.fourbytes.loc8teapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ProfileTabAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArr = new ArrayList<>();
    private final ArrayList<String> fragmentTitles = new ArrayList<>();

    public ProfileTabAdapter(@NonNull FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArr.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArr.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentArr.add(fragment);
        fragmentTitles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
