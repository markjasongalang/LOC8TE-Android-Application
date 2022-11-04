package com.fourbytes.loc8teapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.InsideChatAdapter;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;
import com.fourbytes.loc8teapp.chatsrecycler.InsideChatItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentChat_InsideChat extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvInsideChat;

    private TextView tvChatName;

    private AppCompatButton btnBack;
    private AppCompatButton btnSend;

    private List<InsideChatItems> insideChatItemsList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;
    private String otherUsername;
    private String sender;
    private String receiver;

    private EditText edtChatMesage;

    public FragmentChat_InsideChat() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_inside_chat, container, false);

        // Get views from layout
        rvInsideChat = view.findViewById(R.id.insidechat_recyclerview);
        btnBack = view.findViewById(R.id.btn_back);
        tvChatName = view.findViewById(R.id.tv_chat_name);
        edtChatMesage = view.findViewById(R.id.edt_chat_message);
        btnSend = view.findViewById(R.id.btn_send);

        // Initialize values
        parentFragmentManager = getParentFragmentManager();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        insideChatItemsList = new ArrayList<>();
        sender = "";
        receiver = "";

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getUsername();
        accountType = pair.getAccountType();

        otherUsername = DataPasser.getChatUsername();

        if (accountType.equals("client")) {
            sender = username;
            receiver = otherUsername;
        } else {
            sender = otherUsername;
            receiver = username;
        }

        db.collection((accountType.equals("client") ? "professionals" : "clients")).document(otherUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tvChatName.setText(task.getResult().getData().get("first_name") + " " + task.getResult().getData().get("last_name"));
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatMessage = edtChatMesage.getText().toString();
                if (chatMessage.isEmpty()) {
                    return;
                }

                Timestamp ts = new Timestamp(System.currentTimeMillis());

                Map<String, Object> temp = new HashMap<>();
                temp.put("sender", username);
                temp.put("message", chatMessage);
                temp.put("timestamp", ts.toString());

                db.collection("client_chats").document(sender).collection("pro_list_chats")
                        .document(receiver).collection("messages").add(temp);

                edtChatMesage.setText("");

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvInsideChat.setLayoutManager(new LinearLayoutManager(view.getContext()));

        insideChatItemsList = new ArrayList<>();
        db.collection("client_chats").document(sender).collection("pro_list_chats").document(receiver)
                .collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        insideChatItemsList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {

                            // Get profile picture of current user
                            StorageReference storageRef = storage.getReference();
                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getData().get("sender").toString() + "_profile.jpg");
                            insideChatItemsList.add(new InsideChatItems(
                                    documentSnapshot.getData().get("message").toString(),
                                    documentSnapshot.getData().get("timestamp").toString(),
                                    pathReference,
                                    (documentSnapshot.getData().get("sender").equals(username) ? InsideChatItems.layout_right : InsideChatItems.layout_left)
                            ));

                        }
                        Collections.sort(insideChatItemsList, new Comparator<InsideChatItems>() {
                            @Override
                            public int compare(InsideChatItems message, InsideChatItems other) {
                                Timestamp ts1 = Timestamp.valueOf(message.getInside_chat_timestamp());
                                Timestamp ts2 = Timestamp.valueOf(other.getInside_chat_timestamp());
                                return ts1.compareTo(ts2);
                            }
                        });
                        rvInsideChat.setAdapter(new InsideChatAdapter(getContext(), insideChatItemsList));
                        if (insideChatItemsList.size() > 0) {
                            rvInsideChat.scrollToPosition(insideChatItemsList.size()-1);
                        }

                    }
                });

    }
}