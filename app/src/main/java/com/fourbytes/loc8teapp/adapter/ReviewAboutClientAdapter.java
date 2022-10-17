package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClient;
import com.fourbytes.loc8teapp.reviewaboutclientrecycler.ReviewAboutClientViewHolder;
import com.fourbytes.loc8teapp.reviewforprorecycler.ReviewForProfessional;

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
        String firstName = reviewsAboutClient.get(position).getFirstName();
        String middleName = reviewsAboutClient.get(position).getMiddleName();
        String lastName = reviewsAboutClient.get(position).getLastName();
        String profession = reviewsAboutClient.get(position).getProfession();
        String review = reviewsAboutClient.get(position).getReview();
        String timestamp = reviewsAboutClient.get(position).getTimestamp();
        double rating = reviewsAboutClient.get(position).getRating();

        holder.tvFullName.setText(firstName + " " + middleName + " " + lastName);
        holder.tvProfession.setText(profession);
        holder.tvReview.setText(review);
        holder.tvTimestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return reviewsAboutClient.size();
    }
}
