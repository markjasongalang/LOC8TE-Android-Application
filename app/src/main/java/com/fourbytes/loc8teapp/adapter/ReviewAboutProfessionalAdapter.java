package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.reviewaboutproreycler.ReviewAboutProfessional;
import com.fourbytes.loc8teapp.reviewaboutproreycler.ReviewAboutProfessionalViewHolder;

import java.util.List;

public class ReviewAboutProfessionalAdapter extends RecyclerView.Adapter<ReviewAboutProfessionalViewHolder> {
    private Context context;
    private List<ReviewAboutProfessional> reviewsAboutProfessional;

    public ReviewAboutProfessionalAdapter(Context context, List<ReviewAboutProfessional> reviewsAboutProfessional) {
        this.context = context;
        this.reviewsAboutProfessional = reviewsAboutProfessional;
    }

    @NonNull
    @Override
    public ReviewAboutProfessionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAboutProfessionalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review_about_professional, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAboutProfessionalViewHolder holder, int position) {
        String clientName = reviewsAboutProfessional.get(position).getClientName();
        double rating = reviewsAboutProfessional.get(position).getRating();
        String review = reviewsAboutProfessional.get(position).getReview();
        String timestamp = reviewsAboutProfessional.get(position).getTimestamp();

        holder.tvFullName.setText(clientName);
        holder.tvRating.setText(rating + " out of 5");
        holder.tvReview.setText(review);
        holder.tvTimestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return reviewsAboutProfessional.size();
    }
}
