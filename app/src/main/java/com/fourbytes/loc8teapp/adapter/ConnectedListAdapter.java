package com.fourbytes.loc8teapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListViewHolder;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.professional.FragmentProfile_Professional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class ConnectedListAdapter extends RecyclerView.Adapter<ConnectedListViewHolder> {
    private Context connectedlist_context;
    
    private List<ConnectedListItems> connectedlist_items;
    
    private FragmentManager parentFragmentManager;
    
    private LayoutInflater layoutInflater;

    private String username;
    private String clientName;

    private FirebaseFirestore db;

    private HashMap<String, Object> temp;

    public ConnectedListAdapter(Context connectedlist_context, List<ConnectedListItems> connectedlist_items, FragmentManager parentFragmentManager, LayoutInflater layoutInflater, String username, String clientName) {
        this.connectedlist_context = connectedlist_context;
        this.connectedlist_items = connectedlist_items;
        this.parentFragmentManager = parentFragmentManager;
        this.layoutInflater = layoutInflater;
        this.username = username;
        this.clientName = clientName;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ConnectedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConnectedListViewHolder(LayoutInflater.from(connectedlist_context).inflate(R.layout.fragment_home_connected_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedListViewHolder holder, int position) {
        int pos = position;

        String professionalName = connectedlist_items.get(position).getConnectedlist_name();
        String profession = connectedlist_items.get(position).getConnectedlist_occupation();
        String field = connectedlist_items.get(position).getConnectedlist_field();

        holder.connected_list_name.setText(professionalName);
        holder.connected_list_occupation.setText(profession);
        holder.connected_list_field.setText(field);

        StorageReference pathReference = connectedlist_items.get(position).getPathReference();
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.connected_list_img.setImageBitmap(bmp);
            }
        });

        temp = new HashMap<>();
        temp.put("exists", true);
        db.collection("professional_reviews").document(connectedlist_items.get(pos).getConnectedlist_username()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    db.collection("professional_reviews").document(connectedlist_items.get(pos).getConnectedlist_username()).set(temp);
                }
            }
        });

        db.collection("client_profiles").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    db.collection("client_profiles").document(username).set(temp);
                }
            }
        });

        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .document(connectedlist_items.get(pos).getConnectedlist_username())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            if (value.getData().get("is_rated") != null) {
                                if ((boolean) value.getData().get("is_rated")) {
                                    holder.connected_list_rate.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                });

        holder.connected_list_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View rateClientPopupView = layoutInflater.inflate(R.layout.rate_professional_popup, null);

                TextView tvProfessionalName = rateClientPopupView.findViewById(R.id.tv_professional_name);
                RatingBar rbProfessionalRating = rateClientPopupView.findViewById(R.id.rb_professional_rating);
                EditText edtReview = rateClientPopupView.findViewById(R.id.edt_review);
                AppCompatButton btnRate = rateClientPopupView.findViewById(R.id.btn_rate);
                AppCompatButton btnCancel = rateClientPopupView.findViewById(R.id.btn_cancel);

                tvProfessionalName.setText(connectedlist_items.get(pos).getConnectedlist_name());

                dialogBuilder.setView(rateClientPopupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                
                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("client_homes")
                                .document(username)
                                .collection("pro_list")
                                .document(connectedlist_items.get(pos).getConnectedlist_username())
                                .update("is_rated", true);

                        Timestamp ts = new Timestamp(System.currentTimeMillis());

                        temp.clear();
                        temp.put("from", clientName);
                        temp.put("rating", Double.valueOf(rbProfessionalRating.getRating()));
                        temp.put("review", edtReview.getText().toString());
                        temp.put("timestamp", ts.toString());

                        db.collection("professional_reviews")
                                .document(connectedlist_items.get(pos).getConnectedlist_username())
                                .collection("reviews")
                                .document()
                                .set(temp);

                        temp.clear();
                        temp.put("to", connectedlist_items.get(pos).getConnectedlist_name());
                        temp.put("rating", Double.valueOf(rbProfessionalRating.getRating()));
                        temp.put("review", edtReview.getText().toString());
                        temp.put("timestamp", ts.toString());
                        db.collection("client_profiles")
                                .document(username)
                                .collection("reviews_for_professionals")
                                .document()
                                .set(temp);

                        db.collection("professional_reviews")
                                .document(connectedlist_items.get(pos).getConnectedlist_username())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().getData().get("sum_rating") != null) {
                                                double newSumRating = (double) task.getResult().getData().get("sum_rating") + rbProfessionalRating.getRating();
                                                int newNumberOfRatings = Integer.valueOf(task.getResult().getData().get("number_of_ratings").toString()) + 1;
                                                db.collection("professional_reviews")
                                                        .document(connectedlist_items.get(pos).getConnectedlist_username())
                                                        .update("sum_rating", newSumRating);

                                                db.collection("professional_reviews")
                                                        .document(connectedlist_items.get(pos).getConnectedlist_username())
                                                        .update("number_of_ratings", newNumberOfRatings);
                                            } else {
                                                db.collection("professional_reviews")
                                                        .document(connectedlist_items.get(pos).getConnectedlist_username())
                                                        .update("sum_rating", (double) rbProfessionalRating.getRating());

                                                db.collection("professional_reviews")
                                                        .document(connectedlist_items.get(pos).getConnectedlist_username())
                                                        .update("number_of_ratings", 1);
                                            }
                                        }
                                    }
                                });



                        Toast.makeText(connectedlist_context, "Successfully rated!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                
            }
        });

        // Get username of chosen professional
        String proUsername = connectedlist_items.get(position).getConnectedlist_username();

        holder.connected_list_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataPasser.setUsername1(proUsername);

                parentFragmentManager.beginTransaction()
                        .replace(R.id.layout_client_home, FragmentProfile_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return connectedlist_items.size();
    }
}
