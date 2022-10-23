package com.fourbytes.loc8teapp.experienceprorecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ExperienceViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPosition;
    public TextView tvCompany;
    public TextView tvExpDescription;

    public AppCompatButton btnExpEdit;

    public ExperienceViewHolder(@NonNull View itemView) {
        super(itemView);

        tvPosition = itemView.findViewById(R.id.tv_position);
        tvCompany = itemView.findViewById(R.id.tv_company);
        tvExpDescription = itemView.findViewById(R.id.tv_exp_description);

        btnExpEdit = itemView.findViewById(R.id.btn_exp_edit);
    }
}
