package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Professional;

public class FragmentEvent_Register_Success extends Fragment {
    private View view;

    private TextView register_name;

    private AppCompatButton done_button;

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register_success, container, false);

        done_button = view.findViewById(R.id.done_button);

        fragmentManager = getParentFragmentManager();

        //TODO: Pass data to change the name and event title

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}