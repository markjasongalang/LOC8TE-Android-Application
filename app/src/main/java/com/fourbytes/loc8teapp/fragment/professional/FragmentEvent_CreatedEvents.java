package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fourbytes.loc8teapp.R;

public class FragmentEvent_CreatedEvents extends Fragment {

    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentevent_createdevents, container, false);

        return view;

    }

}
