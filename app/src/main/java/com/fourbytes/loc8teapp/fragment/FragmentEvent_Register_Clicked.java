package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;

public class FragmentEvent_Register_Clicked extends Fragment {
    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnRegister;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register_clicked, container, false);

        fragmentManager = getParentFragmentManager();
        btnBack = view.findViewById(R.id.btn_back);
        btnRegister = view.findViewById(R.id.register_button);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Register_Success.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}