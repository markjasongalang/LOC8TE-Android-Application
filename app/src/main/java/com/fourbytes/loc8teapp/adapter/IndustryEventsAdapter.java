package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.HostActivity;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Register;
import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsViewHolder;
import com.fourbytes.loc8teapp.industryeventsrecycler.IndustryEventsItems;
import com.fourbytes.loc8teapp.industryeventsrecycler.IndustryEventsViewHolder;

import java.util.List;

public class IndustryEventsAdapter extends RecyclerView.Adapter<IndustryEventsViewHolder> {

    Context context;
    private List<IndustryEventsItems> industry_items;

    public IndustryEventsAdapter(Context context, List<IndustryEventsItems> industry_items){
        this.context = context;
        this.industry_items = industry_items;
    }
    @NonNull
    @Override
    public IndustryEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IndustryEventsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_event_industry_professional_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IndustryEventsViewHolder holder, int position) {

        holder.event_title.setText(industry_items.get(position).getEvent_title());
        holder.event_location.setText(industry_items.get(position).getEvent_location());
        holder.event_time.setText(industry_items.get(position).getTime());
        holder.event_date.setText(industry_items.get(position).getDate());
        holder.host_name.setText(industry_items.get(position).getHosted_by());
        holder.host_job.setText(industry_items.get(position).getJob_title());
        holder.host_image.setImageResource(industry_items.get(position).getImage());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Pass data into fragments
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentEvent_Register fragment = new FragmentEvent_Register();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(view.getContext(), industry_items.get(holder.getAdapterPosition()).getEvent_title(), Toast.LENGTH_SHORT).show();
                Log.d("EVENTS BUTTON", industry_items.get(holder.getAdapterPosition()).getEvent_title());

            }
        });
    }

    @Override
    public int getItemCount() {
        return industry_items.size();
    }
}
