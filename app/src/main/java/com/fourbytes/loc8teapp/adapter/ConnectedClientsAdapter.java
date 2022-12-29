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
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientItem;
import com.fourbytes.loc8teapp.connectedclientsrecycler.ClientViewHolder;
import com.fourbytes.loc8teapp.fragment.client.FragmentProfile_Client;
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

public class ConnectedClientsAdapter extends RecyclerView.Adapter<ClientViewHolder> {
    private Context context;

    private List<ClientItem> clientItems;

    private FragmentManager parentFragmentManager;

    private LayoutInflater layoutInflater;

    private String professionalName;

    private FirebaseFirestore db;

    private HashMap<String, Object> temp;

    private String username;

    public ConnectedClientsAdapter(Context context,
                                   List<ClientItem> clientItems,
                                   FragmentManager parentFragmentManager,
                                   LayoutInflater layoutInflater,
                                   String professionalName,
                                   String username) {
        this.context = context;
        this.clientItems = clientItems;
        this.parentFragmentManager = parentFragmentManager;
        this.layoutInflater = layoutInflater;
        this.professionalName = professionalName;
        this.username = username;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(context).inflate(R.layout.connected_client_item_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        int pos = position;

        StorageReference pathReference = clientItems.get(position).getPathReference();
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivClientImage.setImageBitmap(bmp);
            }
        });

        temp = new HashMap<>();
        temp.put("exists", true);
        db.collection("client_profiles").document(clientItems.get(pos).getClientUsername()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    db.collection("client_profiles").document(clientItems.get(pos).getClientUsername()).set(temp);
                }
            }
        });

        db.collection("clients").document(clientItems.get(pos).getClientUsername()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String firstName = task.getResult().getData().get("first_name").toString();
                    String middleName = task.getResult().getData().get("middle_name").toString();
                    String lastName = task.getResult().getData().get("last_name").toString();

                    holder.tvClientName.setText(firstName + " " + middleName + " " + lastName);

                    db.collection("pro_homes")
                            .document(username)
                            .collection("client_list")
                            .document(clientItems.get(pos).getClientUsername())
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value.exists()) {
                                        if (value.getData().get("is_rated") != null) {
                                            if ((boolean) value.getData().get("is_rated")) {
                                                holder.btnClientRate.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                            });

                    holder.btnClientRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                            final View rateClientPopupView = layoutInflater.inflate(R.layout.rate_client_popup, null);

                            TextView tvClientNamePopup = rateClientPopupView.findViewById(R.id.tv_client_name_popup);
                            RatingBar rbClientRating = rateClientPopupView.findViewById(R.id.rb_client_rating);
                            EditText edtReview = rateClientPopupView.findViewById(R.id.edt_review);
                            AppCompatButton btnRate = rateClientPopupView.findViewById(R.id.btn_rate);
                            AppCompatButton btnCancel = rateClientPopupView.findViewById(R.id.btn_cancel);

                            tvClientNamePopup.setText(firstName + " " + lastName);

                            dialogBuilder.setView(rateClientPopupView);
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.show();

                            btnRate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    db.collection("pro_homes")
                                            .document(username)
                                            .collection("client_list")
                                            .document(clientItems.get(pos).getClientUsername())
                                            .update("is_rated", true);

                                    Timestamp ts = new Timestamp(System.currentTimeMillis());

                                    temp.clear();
                                    temp.put("from", professionalName);
                                    temp.put("rating", Double.valueOf(rbClientRating.getRating()));
                                    temp.put("review", edtReview.getText().toString());
                                    temp.put("timestamp", ts.toString());

                                    db.collection("client_profiles")
                                            .document(clientItems.get(pos).getClientUsername())
                                            .collection("reviews_about_client")
                                            .document()
                                            .set(temp);

                                    db.collection("client_profiles")
                                            .document(clientItems.get(pos).getClientUsername())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().getData().get("sum_rating") != null) {
                                                            double newSumRating = (double) task.getResult().getData().get("sum_rating") + rbClientRating.getRating();
                                                            int newNumberOfRatings = Integer.valueOf(task.getResult().getData().get("number_of_ratings").toString()) + 1;
                                                            db.collection("client_profiles")
                                                                    .document(clientItems.get(pos).getClientUsername())
                                                                    .update("sum_rating", newSumRating);

                                                            db.collection("client_profiles")
                                                                    .document(clientItems.get(pos).getClientUsername())
                                                                    .update("number_of_ratings", newNumberOfRatings);
                                                        } else {
                                                            db.collection("client_profiles")
                                                                    .document(clientItems.get(pos).getClientUsername())
                                                                    .update("sum_rating", (double) rbClientRating.getRating());

                                                            db.collection("client_profiles")
                                                                    .document(clientItems.get(pos).getClientUsername())
                                                                    .update("number_of_ratings", 1);
                                                        }
                                                    }
                                                }
                                            });

                                    Toast.makeText(context, "Successfully rated!", Toast.LENGTH_SHORT).show();
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
                }
            }
        });

        holder.btnClientProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataPasser.setUsername2(clientItems.get(pos).getClientUsername().toString());

                parentFragmentManager.beginTransaction()
                        .replace(R.id.layout_home_professional, FragmentProfile_Client.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return clientItems.size();
    }
}
