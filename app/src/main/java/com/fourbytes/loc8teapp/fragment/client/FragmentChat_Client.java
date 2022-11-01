package com.fourbytes.loc8teapp.fragment.client;

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

import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.ConnectedListAdapter;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.ChatAdapter;
import com.fourbytes.loc8teapp.connectedlistrecycler.ConnectedListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentChat_Client extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvChats;

    private List<ChatsItems> chatsItemsList;

    private SharedViewModel viewModel;

    private String username;

    public FragmentChat_Client() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_client, container, false);

        // Get views from layout
        rvChats = view.findViewById(R.id.chats_recyclerview);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        parentFragmentManager = getParentFragmentManager();

        // Get username of current user
        username = "";
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            username = data;
        });

        // Workaround to enable the visibility of the document
        Map<String, Object> temp = new HashMap<>();
        temp.put("exists", true);
        db.collection("client_chats").document(username).set(temp);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvChats.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chatsItemsList = new ArrayList<>();

        chatsItemsList.add(new ChatsItems(
                "Anya Forger",
                "Batang Pasaway",
                "Anya: Waku waku!",
                R.drawable.icon_profile
        ));

        chatsItemsList.add(new ChatsItems(
                "Yor Forger",
                "Assassin",
                "Yor: Good day!",
                R.drawable.icon_profile
        ));

        chatsItemsList.add(new ChatsItems(
                "Loid Forger",
                "Spy Mans",
                "Loid: Hola",
                R.drawable.icon_profile
        ));

        rvChats.setAdapter(new ChatAdapter(view.getContext(), chatsItemsList, parentFragmentManager));

//        db.collection("client_homes")
//                .document(username)
//                .collection("pro_list")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        HashSet<String> connected = new HashSet<>();
//                        for (QueryDocumentSnapshot document : value) {
//                            if ((boolean) document.getData().get("is_connected")) {
//                                connected.add(document.getId());
//                            }
//                        }
//
//                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    connectedList = new ArrayList<>();
//                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                        if (connected.contains(documentSnapshot.getId())) {
//                                            String fullName = documentSnapshot.getData().get("first_name") + " " + documentSnapshot.getData().get("last_name");
//                                            String specific_job = documentSnapshot.getData().get("specific_job").toString();
//                                            String field = documentSnapshot.getData().get("field").toString();
//                                            connectedList.add(new ConnectedListItems(
//                                                    fullName,
//                                                    specific_job,
//                                                    field,
//                                                    R.drawable.icon_profile
//                                            ));
//                                        }
//                                    }
//                                    rvChats.setAdapter(new ChatAdapter(view.getContext(), chatsItemsList, parentFragmentManager));
//                                }
//                            }
//                        });
//
//                    }
//                });

    }
}