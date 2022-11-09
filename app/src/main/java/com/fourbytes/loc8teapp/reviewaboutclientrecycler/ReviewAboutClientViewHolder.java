package com.fourbytes.loc8teapp.reviewaboutclientrecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;

import org.w3c.dom.Text;

public class ReviewAboutClientViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFullName;
    public TextView tvRating;
    public TextView tvReview;
    public TextView tvTimestamp;

    public ReviewAboutClientViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFullName = itemView.findViewById(R.id.tv_full_name);
        tvRating = itemView.findViewById(R.id.tv_rating);
        tvReview = itemView.findViewById(R.id.tv_review);
        tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
    }
}
