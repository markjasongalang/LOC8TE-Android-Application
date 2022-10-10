package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ExperienceAdapter;
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceItem;

import java.util.ArrayList;
import java.util.List;

public class ExperienceFragment extends Fragment {
    private View view;

    private RecyclerView rvExperience;

    private List<ExperienceItem> experienceItemList;

    public ExperienceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_experience, container, false);

        // TODO: Add RecyclerView for Experiences

        rvExperience = view.findViewById(R.id.rv_experience);

        experienceItemList = new ArrayList<>();

        experienceItemList.add(new ExperienceItem(
           "Head Chef",
                "Netflix",
                "I was the no. 1 chef of the world at that time."
        ));

        experienceItemList.add(new ExperienceItem(
                "Former Class S Hero",
                "Jollibee",
                "I was the no. 1 hero of the world at that time."
        ));

        experienceItemList.add(new ExperienceItem(
                "Android Lord",
                "Apple",
                "I was the no. 1 android lord of the world at that time."
        ));

        rvExperience.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvExperience.setAdapter(new ExperienceAdapter(view.getContext(), experienceItemList));

        return view;
    }
}