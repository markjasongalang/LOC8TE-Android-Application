package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fourbytes.loc8teapp.R;
import com.google.android.gms.maps.MapView;

public class FragmentSetLocation_Professional extends Fragment {

    private MapView map_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_location_professional, container, false);
    }
}