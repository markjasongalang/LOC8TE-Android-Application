package com.fourbytes.loc8teapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getFirst();
        accountType = pair.getSecond();

//        parentFragmentManager.setFragmentResultListener("data_from_chat_adapter", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                otherUsername = result.getString("chat_username");
//            }
//        });

        otherUsername = DataPasser.getUsername();

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

                db.collection("client_chats").document(username).collection("pro_list_chats")
                        .document(otherUsername).collection("messages").add(temp);

                edtChatMesage.setText("");

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        rvInsideChat.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("client_chats").document(username).collection("pro_list_chats").document(otherUsername)
                .collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        insideChatItemsList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {

                            // Get profile picture of current user
                            StorageReference storageRef = storage.getReference();
                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getData().get("sender").toString() + "_profile.jpg");
                            final long ONE_MEGABYTE = 1024 * 1024;
                            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Log.d("image_stats", "Image retrieved.");
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    insideChatItemsList.add(new InsideChatItems(
                                            documentSnapshot.getData().get("message").toString(),
                                            documentSnapshot.getData().get("timestamp").toString(),
                                            bmp,
                                            (documentSnapshot.getData().get("sender").equals(username) ? InsideChatItems.layout_right : InsideChatItems.layout_left)
                                    ));
//                                    rvNewList.setAdapter(new NewListAdapter(getContext(), newList, parentFragmentManager));

                                    Collections.sort(insideChatItemsList, new Comparator<InsideChatItems>() {
                                        @Override
                                        public int compare(InsideChatItems message, InsideChatItems other) {
                                            Timestamp ts1 = Timestamp.valueOf(message.getInside_chat_timestamp());
                                            Timestamp ts2 = Timestamp.valueOf(other.getInside_chat_timestamp());
                                            return ts1.compareTo(ts2);
                                        }
                                    });
                                    rvInsideChat.setAdapter(new InsideChatAdapter(getContext(), insideChatItemsList));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.d("image_stats", "Image not retrieved.");
                                }
                            });

//                            insideChatItemsList.add(new InsideChatItems(
//                                    documentSnapshot.getData().get("message").toString(),
//                                    documentSnapshot.getData().get("timestamp").toString(),
//                                    R.drawable.icon_profile,
//                                    (documentSnapshot.getData().get("sender").equals(username) ? InsideChatItems.layout_right : InsideChatItems.layout_left)
//                            ));

                        }

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