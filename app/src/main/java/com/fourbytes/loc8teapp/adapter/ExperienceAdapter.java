package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceItem;
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceViewHolder;

import java.util.List;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceViewHolder> {
    private Context context;
    private List<ExperienceItem> experienceItems;

    public ExperienceAdapter(Context context, List<ExperienceItem> experienceItems) {
        this.context = context;
        this.experienceItems = experienceItems;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExperienceViewHolder(LayoutInflater.from(context).inflate(R.layout.experience_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        holder.tvPosition.setText(experienceItems.get(position).getPosition());
        holder.tvCompany.setText(experienceItems.get(position).getCompany());
        holder.tvExpDescription.setText(experienceItems.get(position).getDescription());
        
        holder.btnExpEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You've clicked edit!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return experienceItems.size();
    }
}
