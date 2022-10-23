package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Register;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Registered;
import com.fourbytes.loc8teapp.myeventsrecycler.MyEventsItems;
import com.fourbytes.loc8teapp.myeventsrecycler.MyEventsViewHolder;

import java.util.List;

public class RegisteredEventsAdapter extends RecyclerView.Adapter<MyEventsViewHolder> {

    Context context;
    private List<MyEventsItems> myevents_items;
    private FragmentManager fragmentManager;
    public RegisteredEventsAdapter(Context context, List<MyEventsItems> myevents_items, FragmentManager fragmentManager){
        this.context = context;
        this.myevents_items = myevents_items;
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public MyEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEventsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_event_myevents_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsViewHolder holder, int position) {

        holder.event_title.setText(myevents_items.get(position).getEvent_title());
        holder.event_location.setText(myevents_items.get(position).getEvent_location());
        holder.event_time.setText(myevents_items.get(position).getTime());
        holder.event_date.setText(myevents_items.get(position).getDate());
        holder.host_name.setText(myevents_items.get(position).getHosted_by());
        holder.host_job.setText(myevents_items.get(position).getJob_title());
        holder.host_image.setImageResource(myevents_items.get(position).getImage());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Pass data into fragments
                FragmentEvent_Registered fragment = new FragmentEvent_Registered();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(view.getContext(), myevents_items.get(holder.getAdapterPosition()).getEvent_title(), Toast.LENGTH_SHORT).show();
                Log.d("EVENTS BUTTON", myevents_items.get(holder.getAdapterPosition()).getEvent_title());

            }
        });
    }

    @Override
    public int getItemCount() {
        return myevents_items.size();
    }
}
