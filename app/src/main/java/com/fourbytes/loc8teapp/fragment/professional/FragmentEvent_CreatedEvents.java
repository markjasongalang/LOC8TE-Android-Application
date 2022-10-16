package com.fourbytes.loc8teapp.fragment.professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.CreatedEventsAdapter;
import com.fourbytes.loc8teapp.adapter.RegisteredEventsAdapter;
import com.fourbytes.loc8teapp.myeventsrecycler.MyEventsItems;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent_CreatedEvents extends Fragment {

    private View view;

    private RecyclerView recyclerView;

    private CardView btnCreate;

    private FragmentManager fragmentManager;

    List<MyEventsItems> items = new ArrayList<MyEventsItems>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_createdevents, container, false);

        btnCreate = view.findViewById(R.id.btn_create);
        recyclerView = view.findViewById(R.id.created_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        fragmentManager = getParentFragmentManager();

        items.add(new MyEventsItems(
                "Example Title",
                "Example Location",
                "Example Host",
                "Example Time",
                "Example Time",
                "Example Date",
                "Example Profession",
                R.drawable.anya
        ));

        items.add(new MyEventsItems(
                "Example Title",
                "Example Location",
                "Example Host",
                "Example Time",
                "Example Time",
                "Example Date",
                "Example Profession",
                R.drawable.anya
        ));

        updateEvents();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: function for Save changes
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_Edit.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;

    }

    public void updateEvents(){

        recyclerView.setAdapter(new CreatedEventsAdapter(view.getContext(), items, fragmentManager));

    }

}
