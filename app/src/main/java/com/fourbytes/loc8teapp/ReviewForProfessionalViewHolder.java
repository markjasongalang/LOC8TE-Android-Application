package com.fourbytes.loc8teapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewForProfessionalViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFullName;
    public TextView tvProfession;
    public TextView tvReview;
    public TextView tvTimestamp;

    public ReviewForProfessionalViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFullName = itemView.findViewById(R.id.tv_full_name);
        tvProfession = itemView.findViewById(R.id.tv_profession);
        tvReview = itemView.findViewById(R.id.tv_review);
        tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
    }
}
