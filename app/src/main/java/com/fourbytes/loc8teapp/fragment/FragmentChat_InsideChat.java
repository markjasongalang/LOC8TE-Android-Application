package com.fourbytes.loc8teapp.fragment;

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

import java.sql.Timestamp;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.chatsrecycler.InsideChatItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.InsideChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentChat_InsideChat extends Fragment {
    private View view;

    private FirebaseFirestore db;

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

    private EditText edtChatMesage;

    public FragmentChat_InsideChat() {
    }

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

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getFirst();
        accountType = pair.getSecond();

        parentFragmentManager.setFragmentResultListener("data_from_chat_adapter", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                otherUsername = result.getString("chat_username");

                if (accountType.equals("client")) {
                    // For client
                    db.collection("professionals").document(otherUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                tvChatName.setText(task.getResult().getData().get("first_name") + " " + task.getResult().getData().get("last_name"));
                            }
                        }
                    });
                } else {
                    // For professional
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

//                db.collection("professionals").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (QueryDocumentSnapshot documentSnapshot : value) {
//                            // Place code to add in the List<> for Recyclerview
//                            Log.d("sample_data", documentSnapshot.getData().toString());
//                        }
//                    }
//                });

                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Map<String, Object> temp = new HashMap<>();
                temp.put("sender", username);
                temp.put("message", chatMessage);
                temp.put("timestamp", ts.toString());
                db.collection("client_chats").document(username).collection("pro_list_chats")
                        .document(otherUsername).collection("messages").add(temp);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



        rvInsideChat.setLayoutManager(new LinearLayoutManager(view.getContext()));

        insideChatItemsList = new ArrayList<>();

        db.collection("client_chats").document(username).collection("pro_list_chats").document(otherUsername)
                .collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        insideChatItemsList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
//                            insideChatItemsList.add()
                        }
                        rvInsideChat.setAdapter(new InsideChatAdapter(getContext(), insideChatItemsList));

                    }
                });

//        insideChatItemsList.add(new InsideChatItems(
//                "Hello!",
//                R.drawable.random2,
//                InsideChatItems.layout_right
//        ));
//
//        insideChatItemsList.add(new InsideChatItems(
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
//                R.drawable.random2,
//                InsideChatItems.layout_right
//        ));
//
//        insideChatItemsList.add(new InsideChatItems(
//                "Thanks for contacting me!",
//                R.drawable.random1,
//                InsideChatItems.layout_left
//        ));

    }
}