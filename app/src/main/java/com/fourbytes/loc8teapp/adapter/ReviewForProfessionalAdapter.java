package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.reviewforprorecycler.ReviewForProfessional;
import com.fourbytes.loc8teapp.reviewforprorecycler.ReviewForProfessionalViewHolder;

import java.util.List;

public class ReviewForProfessionalAdapter extends RecyclerView.Adapter<ReviewForProfessionalViewHolder> {
    private Context context;
    private List<ReviewForProfessional> reviewsForProfessional;

    public ReviewForProfessionalAdapter(Context context, List<ReviewForProfessional> reviewsForProfessional) {
        this.context = context;
        this.reviewsForProfessional = reviewsForProfessional;
    }

    @NonNull
    @Override
    public ReviewForProfessionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewForProfessionalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review_for_professional, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewForProfessionalViewHolder holder, int position) {
        String firstName = reviewsForProfessional.get(position).getFirstName();
        String middleName = reviewsForProfessional.get(position).getMiddleName();
        String lastName = reviewsForProfessional.get(position).getLastName();
        String profession = reviewsForProfessional.get(position).getProfession();
        String review = reviewsForProfessional.get(position).getReview();
        String timestamp = reviewsForProfessional.get(position).getTimestamp();
        double rating = reviewsForProfessional.get(position).getRating();

        holder.tvFullName.setText(firstName + " " + middleName + " " + lastName);
        holder.tvProfession.setText(profession);
        holder.tvReview.setText(review);
        holder.tvTimestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return reviewsForProfessional.size();
    }
}
