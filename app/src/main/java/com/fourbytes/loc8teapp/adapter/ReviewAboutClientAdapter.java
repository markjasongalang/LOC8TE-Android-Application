package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClient;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClientViewHolder;

import java.util.List;

public class ReviewAboutClientAdapter extends RecyclerView.Adapter<ReviewAboutClientViewHolder> {
    private Context context;

    private List<ReviewAboutClient> reviewsAboutClient;

    public ReviewAboutClientAdapter(Context context, List<ReviewAboutClient> reviewsAboutClient) {
        this.context = context;
        this.reviewsAboutClient = reviewsAboutClient;
    }

    @NonNull
    @Override
    public ReviewAboutClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAboutClientViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review_about_client, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAboutClientViewHolder holder, int position) {
        String fullName = reviewsAboutClient.get(position).getProfessionalName();
        double rating = reviewsAboutClient.get(position).getRating();
        String review = reviewsAboutClient.get(position).getReview();
        String timestamp = reviewsAboutClient.get(position).getTimestamp();

        holder.tvFullName.setText(fullName);
        holder.tvRating.setText(rating + " out of 5");
        holder.tvReview.setText(review);
        holder.tvTimestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return reviewsAboutClient.size();
    }
}
