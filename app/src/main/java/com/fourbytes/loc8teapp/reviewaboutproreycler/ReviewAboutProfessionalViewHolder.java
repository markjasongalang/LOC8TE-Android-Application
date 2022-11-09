package com.fourbytes.loc8teapp.reviewaboutproreycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

public class ReviewAboutProfessionalViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFullName;
    public TextView tvReview;
    public TextView tvTimestamp;
    public TextView tvRating;

    public ReviewAboutProfessionalViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFullName = itemView.findViewById(R.id.tv_full_name);
        tvReview = itemView.findViewById(R.id.tv_review);
        tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        tvRating = itemView.findViewById(R.id.tv_rating);
    }
}
