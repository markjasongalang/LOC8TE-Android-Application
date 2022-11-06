package com.fourbytes.loc8teapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.fourbytes.loc8teapp.HostActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.client.FragmentEvent_Client;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Industry_Professional;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_Professional;

public class FragmentEvent_Register_Success extends Fragment {
    private View view;

    private TextView register_name;
    private TextView event_location;
    private TextView event_title;
    private AppCompatButton done_button;

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_register_success, container, false);

        event_title = view.findViewById(R.id.event_title);
        event_location = view.findViewById(R.id.event_location);
        register_name = view.findViewById(R.id.register_name);
        done_button = view.findViewById(R.id.done_button);

        fragmentManager = getParentFragmentManager();

        //TODO: Pass data to change the name and event title

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();

            }
        });

        fragmentManager.setFragmentResultListener("successData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String successMessage = result.getString("name") + ", you are now registered to this event.";
                event_title.setText(result.getString("event_title"));
                event_location.setText(result.getString("event_location"));
                register_name.setText(successMessage);

            }
        });

        return view;
    }
}