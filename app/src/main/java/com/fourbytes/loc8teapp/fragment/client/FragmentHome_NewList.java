package com.fourbytes.loc8teapp.fragment.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;
import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentHome_NewList extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvNewList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    private List<NewListItems> newList;

    private Map<String, Object> temp;

    public FragmentHome_NewList() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_new, container, false);

        // Get views from layout
        rvNewList = view.findViewById(R.id.new_recyclerview);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();

        /// Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getFirst();
        accountType = pair.getSecond();

        // Workaround to enable the visibility of the document
        temp = new HashMap<>();
        temp.put("exists", true);
        db.collection("client_homes").document(username).set(temp);

        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        HashSet<String> connected = new HashSet<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            if ((boolean) documentSnapshot.getData().get("is_connected")) {
                                connected.add(documentSnapshot.getId());
                            }
                        }

                        temp = new HashMap<>();
                        temp.put("is_connected", false);
                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!connected.contains(document.getId())) {
                                            db.collection("client_homes")
                                                    .document(username)
                                                    .collection("pro_list")
                                                    .document(document.getId())
                                                    .set(temp);
                                        }
                                    }
                                }
                            }
                        });

                    }
                });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvNewList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        newList = new ArrayList<>();
        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        HashSet<String> connected = new HashSet<>();
                        for (QueryDocumentSnapshot document : value) {
                            Log.d("try_lang", document.getId() + " " + document.getData());
                            if ((boolean) document.getData().get("is_connected")) {
                                connected.add(document.getId());
                            }
                        }

                        newList = new ArrayList<>();
                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    // todo: FIX CLEARING OF LIST TO AVOID DUPLICATES
                                    // todo: MAKE RETRIEVAL OF DATA POSSIBLE

                                    newList = new ArrayList<>();
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if (!connected.contains(documentSnapshot.getId())) {
                                            String fullName = documentSnapshot.getData().get("first_name") + " " + documentSnapshot.getData().get("last_name");
                                            String specific_job = documentSnapshot.getData().get("specific_job").toString();
                                            String field = documentSnapshot.getData().get("field").toString();

                                            // Get profile picture of current user
                                            StorageReference storageRef = storage.getReference();
                                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId().toString() + "_profile.jpg");
                                            final long ONE_MEGABYTE = 1024 * 1024;
                                            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    Log.d("image_stats", "Image retrieved.");
                                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                    newList.add(new NewListItems(
                                                            documentSnapshot.getId(),
                                                            fullName,
                                                            specific_job,
                                                            field,
                                                            bmp
                                                    ));
                                                    Log.d("new_list_id", documentSnapshot.getId());
                                                    rvNewList.setAdapter(new NewListAdapter(getContext(), newList, parentFragmentManager));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Log.d("image_stats", "Image not retrieved.");
                                                }
                                            });

                                        }
                                    }

                                }
                            }
                        });

                    }
                });
    }
}