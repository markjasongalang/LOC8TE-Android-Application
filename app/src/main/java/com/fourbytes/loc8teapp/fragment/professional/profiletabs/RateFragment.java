package com.fourbytes.loc8teapp.fragment.professional.profiletabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ServiceAdapter;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceItem;

import java.util.ArrayList;
import java.util.List;

public class RateFragment extends Fragment {
    private View view;

    private RecyclerView rvService;

    private List<ServiceItem> serviceItemList;

    public RateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_pro_rate, container, false);

        rvService = view.findViewById(R.id.rv_service);

        serviceItemList = new ArrayList<>();

        serviceItemList.add(new ServiceItem(
                "Live Cooking Show",
                101.69,
                "per hour",
                "I love to cook in front of the panelists <3"
        ));

        serviceItemList.add(new ServiceItem(
                "Hero Duties",
                99999.99,
                "depends on the calamity",
                "Class S Hero to the rescue!"
        ));

        serviceItemList.add(new ServiceItem(
                "Recycler View Lover",
                43.75,
                "per hour",
                "Protect Rylie at all cost..."
        ));

        rvService.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvService.setAdapter(new ServiceAdapter(view.getContext(), serviceItemList));

        return view;
    }
}