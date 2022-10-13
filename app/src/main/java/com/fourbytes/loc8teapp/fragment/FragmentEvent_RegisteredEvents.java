package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fourbytes.loc8teapp.R;

public class FragmentEvent_RegisteredEvents extends Fragment {

    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentevent_registeredevents, container, false);

        return view;

    }
}
