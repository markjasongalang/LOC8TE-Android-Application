package com.fourbytes.loc8teapp.fragment.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class FragmentChat_Client extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvChats;

    private List<ChatsItems> chatsItemsList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    public FragmentChat_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_client, container, false);

        // Get views from layout
        rvChats = view.findViewById(R.id.chats_recyclerview);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getFirst();
        accountType = pair.getSecond();

        // Workaround to enable the visibility of the document
        Map<String, Object> temp = new HashMap<>();
        temp.put("exists", true);
        db.collection("client_chats").document(username).set(temp);

        db.collection("client_homes").document(username).collection("pro_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if ((boolean) documentSnapshot.getData().get("is_connected")) {
                        db.collection("client_chats").document(username).collection("pro_list_chats").document(documentSnapshot.getId()).set(temp);
                    } else {
                        db.collection("client_chats").document(username).collection("pro_list_chats").document(documentSnapshot.getId()).delete();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvChats.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("client_chats")
                .document(username)
                .collection("pro_list_chats")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("chat_err", "Error retrieving the chats");
                            return;
                        }

                        chatsItemsList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            db.collection("professionals").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        // Get profile picture of current user
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId().toString() + "_profile.jpg");
                                        chatsItemsList.add(new ChatsItems(
                                                documentSnapshot.getId(),
                                                task.getResult().getData().get("first_name") + " " + task.getResult().getData().get("last_name"),
                                                task.getResult().getData().get("specific_job") + " ",
                                                task.getResult().getData().get("first_name").toString() + ": Hello",
                                                pathReference
                                        ));
                                        rvChats.setAdapter(new ChatAdapter(getContext(), chatsItemsList, parentFragmentManager, db, username, accountType));
                                    }
                                }
                            });
                        }

                    }
                });

    }
}