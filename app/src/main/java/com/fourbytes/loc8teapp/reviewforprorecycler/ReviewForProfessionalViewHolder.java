package com.fourbytes.loc8teapp.reviewforprorecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ReviewForProfessionalViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFullName;
    public TextView tvRating;
    public TextView tvReview;
    public TextView tvTimestamp;

    public ReviewForProfessionalViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFullName = itemView.findViewById(R.id.tv_full_name);
        tvRating = itemView.findViewById(R.id.tv_rating);
        tvReview = itemView.findViewById(R.id.tv_review);
        tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
    }
}
