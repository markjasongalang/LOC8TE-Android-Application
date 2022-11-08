package com.fourbytes.loc8teapp.fragment.professional;

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
import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;

public class FragmentChat_Professional extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private RecyclerView chats_recyclerView1;
    private RecyclerView chats_recyclerView2;

    private List<ChatsItems> chats_items_industry;
    private List<ChatsItems> chats_items_clients;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;
    private String field;
    private String fieldUsername;

    private FragmentManager parentFragmentManager;

    private HashMap<String, Object> temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_professional, container, false);

        // Get views from layout
        chats_recyclerView1 = view.findViewById(R.id.chats_recyclerview1);
        chats_recyclerView2 = view.findViewById(R.id.chats_recyclerview2);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        temp = new HashMap<>();

        /// Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getUsername();
        accountType = pair.getAccountType();
        field = pair.getField();

        fieldUsername = "";
        for (char ch : field.toCharArray()) {
            if (ch == ' ') {
                fieldUsername += '_';
            } else {
                fieldUsername += ch;
            }
        }

        temp.put("exists", true);
        db.collection("industry_chat").document(fieldUsername).set(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("industry_chat").document(fieldUsername).collection("messages").document("sample_message").set(temp);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // For industry chat
        chats_recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chats_items_industry = new ArrayList<>();

        // Get industry picture
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("industryChatPics/" + fieldUsername + ".jpg");

        chats_items_industry.add(new ChatsItems(
                fieldUsername,
                pathReference
        ));

        chats_recyclerView1.setAdapter(new ChatAdapter(getContext(), chats_items_industry, parentFragmentManager, db, username, accountType, field, fieldUsername));

        // For client chat
        chats_recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chats_items_clients = new ArrayList<>();
        db.collection("pro_homes").document(username).collection("client_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                chats_items_clients = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    // Get profile picture of current user
                    StorageReference storageRef = storage.getReference();
                    StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId() + "_profile.jpg");
                    chats_items_clients.add(new ChatsItems(
                            documentSnapshot.getId(),
                            pathReference
                    ));
                }
                chats_recyclerView2.setAdapter(new ChatAdapter(getContext(), chats_items_clients, parentFragmentManager, db, username, accountType));

            }
        });

    }
}