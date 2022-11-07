package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Registered;
import com.fourbytes.loc8teapp.fragment.professional.FragmentEvent_CreatorView;
import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsItems;
import com.fourbytes.loc8teapp.generaleventsrecycler.GeneralEventsViewHolder;

import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.fragment.FragmentEvent_Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GeneralEventsAdapter extends RecyclerView.Adapter<GeneralEventsViewHolder> {

    Context context;
    private List<GeneralEventsItems> general_items;
    private FragmentManager fragmentManager;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Pair pair;

    private final int NOT_REGISTERED = 0;
    private final int REGISTERED = 1;
    private final int CREATED = 2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GeneralEventsAdapter(Context context, List<GeneralEventsItems> general_items, FragmentManager fragmentManager, Pair pair) {
        this.context = context;
        this.general_items = general_items;
        this.fragmentManager = fragmentManager;
        this.pair = pair;
    }

    @NonNull
    @Override
    public GeneralEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralEventsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_event_general_professional_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralEventsViewHolder holder, int position) {

        holder.event_title.setText(general_items.get(position).getEvent_title());
        holder.event_location.setText(general_items.get(position).getEvent_location());
        holder.event_time.setText(general_items.get(position).getTime());
        holder.event_date.setText(general_items.get(position).getDate());
        holder.host_name.setText(general_items.get(position).getHosted_by());
        holder.host_job.setText(general_items.get(position).getJob_title());

        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("profilePics/" + general_items.get(position).getHost_id() + "_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.host_image.setImageBitmap(bmp);
            }
        });

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pair.getAccountType().equals("client")){
                    checkClientRegistered(holder);
                }else{
                    checkProRegistered(holder);
                }

            }
        });
    }

    private void checkClientRegistered(GeneralEventsViewHolder holder){

        String TAG = "Check";

        db.collection("clients").document(pair.getUsername()).collection("registered_events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean registeredCheck = false;
                            String event_id_check = general_items.get(holder.getAdapterPosition()).getEvent_id();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals("sample_event")){
                                    if (document.getString("event_id").equals(event_id_check)){
                                        registeredCheck = true;
                                        break;
                                    }
                                }
                            }

                            if(registeredCheck){
                                openFragment(holder, REGISTERED);
                            }else{
                                openFragment(holder, NOT_REGISTERED);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void checkProRegistered(GeneralEventsViewHolder holder){

        db.collection("professionals").document(pair.getUsername()).collection("registered_events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean registeredCheck = false;
                            String event_id_check = general_items.get(holder.getAdapterPosition()).getEvent_id();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals("sample_event")){
                                    if (document.getString("event_id").equals(event_id_check)){
                                        registeredCheck = true;
                                        break;
                                    }
                                }
                            }

                            if(registeredCheck){
                                openFragment(holder, REGISTERED);
                            }else{
                                checkCreated(holder);
                            }
                        }
                    }
                });
    }

    private void checkCreated(GeneralEventsViewHolder holder){

        db.collection("professionals").document(pair.getUsername()).collection("created_events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean createdCheck = false;
                            String event_id_check = general_items.get(holder.getAdapterPosition()).getEvent_id();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals("sample_event")){
                                    if (document.getString("event_id").equals(event_id_check)){
                                        createdCheck = true;
                                        break;
                                    }
                                }
                            }

                            if(createdCheck){
                                openFragment(holder, CREATED);
                            }else{
                                openFragment(holder, NOT_REGISTERED);
                            }
                        }
                    }
                });
    }

    private void openFragment(GeneralEventsViewHolder holder, int status){
        //TODO: Pass data into fragments
        Bundle result = new Bundle();
        result.putString("title", general_items.get(holder.getAdapterPosition()).getEvent_title());
        result.putString("location", general_items.get(holder.getAdapterPosition()).getEvent_location());
        result.putString("description", general_items.get(holder.getAdapterPosition()).getEvent_description());
        result.putInt("participant", general_items.get(holder.getAdapterPosition()).getParticipant_count());
        result.putInt("parking_limit", general_items.get(holder.getAdapterPosition()).getParking_limit());
        result.putInt("parking_count", general_items.get(holder.getAdapterPosition()).getParking_count());
        result.putString("host", general_items.get(holder.getAdapterPosition()).getHosted_by());
        result.putString("host_id", general_items.get(holder.getAdapterPosition()).getHost_id());
        result.putString("host_job", general_items.get(holder.getAdapterPosition()).getJob_title());
        result.putString("date", general_items.get(holder.getAdapterPosition()).getDate());
        result.putString("time", general_items.get(holder.getAdapterPosition()).getTime());
        result.putString("event_id", general_items.get(holder.getAdapterPosition()).getEvent_id());
        result.putDouble("latitude", general_items.get(holder.getAdapterPosition()).getLatitude());
        result.putDouble("longitude", general_items.get(holder.getAdapterPosition()).getLongitude());

        fragmentManager.setFragmentResult("eventData", result);
        Fragment fragment;
        if(status == NOT_REGISTERED){
            fragment = new FragmentEvent_Register();
        }else if(status == REGISTERED){
            fragment = new FragmentEvent_Registered();
        }else{
            fragment = new FragmentEvent_CreatorView();
        }


        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, null)
                .commit();
    }


    @Override
    public int getItemCount() {
        return general_items.size();
    }
}
