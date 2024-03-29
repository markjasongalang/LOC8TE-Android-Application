package com.fourbytes.loc8teapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsItems;
import com.fourbytes.loc8teapp.chatsrecycler.ChatsViewHolder;
import com.fourbytes.loc8teapp.fragment.FragmentChat_InsideChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatsViewHolder> {
    private Context chats_context;

    private List<ChatsItems> chat_items;

    private FragmentManager parentFragmentManager;

    private FirebaseFirestore db;

    private String username;
    private String accountType;
    private String field;
    private String fieldUsername;

    public ChatAdapter(Context chats_context, List<ChatsItems> chat_items, FragmentManager parentFragmentManager, FirebaseFirestore db, String username, String accountType) {
        this.chats_context = chats_context;
        this.chat_items = chat_items;
        this.parentFragmentManager = parentFragmentManager;
        this.db = db;
        this.username = username;
        this.accountType = accountType;
    }

    public ChatAdapter(Context chats_context, List<ChatsItems> chat_items, FragmentManager parentFragmentManager, FirebaseFirestore db, String username, String accountType, String field, String fieldUsername) {
        this.chats_context = chats_context;
        this.chat_items = chat_items;
        this.parentFragmentManager = parentFragmentManager;
        this.db = db;
        this.username = username;
        this.accountType = accountType;
        this.field = field;
        this.fieldUsername = fieldUsername;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatsViewHolder(LayoutInflater.from(chats_context).inflate(R.layout.fragment_chat_items,parent,false));
    }

    static class MessagePreview {
        String name;
        String message;
        String timestamp;

        public MessagePreview(String name, String message, String timestamp) {
            this.name = name;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {

        int pos = position;
        if (fieldUsername == null) {
            Log.d("hello_test", chat_items.get(pos).getChatUsername());
            db.collection((accountType.equals("client") ? "professionals" : "clients"))
                    .document(chat_items.get(pos).getChatUsername())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().get("first_name") != null && task.getResult().get("last_name") != null) {
                                holder.chats_name.setText(task.getResult().getData().get("first_name").toString() + " " + task.getResult().getData().get("last_name").toString());

                                if (accountType.equals("client") && task.getResult().get("specific_job") != null) {
                                    holder.chats_occupation.setText(task.getResult().getData().get("specific_job").toString());
                                }
                            }
                        }
                    });
        } else {
            holder.chats_name.setText(field);
        }

        StorageReference pathReference = chat_items.get(pos).getPathReference();
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.chats_image.setImageBitmap(bmp);
            }
        });

        String sender = "";
        String receiver = "";
        if (accountType.equals("client")) {
            sender = username;
            receiver = chat_items.get(pos).getChatUsername();
        } else {
            sender = chat_items.get(pos).getChatUsername();
            receiver = username;
            holder.chats_occupation.setVisibility(View.GONE);
        }

        CollectionReference colRef;
        if (fieldUsername == null) {
            colRef = db.collection("client_chats")
                    .document(sender)
                    .collection("pro_list_chats")
                    .document(receiver)
                    .collection("messages");
        } else {
            colRef = db.collection("industry_chat").document(fieldUsername).collection("messages");
        }

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<MessagePreview> messages = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.getId().equals("sample_message")) {
                        continue;
                    }

                    messages.add(new MessagePreview(
                            documentSnapshot.getData().get("sender").toString(),
                            documentSnapshot.getData().get("message").toString(),
                            documentSnapshot.getData().get("timestamp").toString()
                    ));
                }

                Collections.sort(messages, new Comparator<MessagePreview>() {
                    @Override
                    public int compare(MessagePreview message, MessagePreview other) {
                        Timestamp ts1 = Timestamp.valueOf(message.timestamp);
                        Timestamp ts2 = Timestamp.valueOf(other.timestamp);
                        return ts1.compareTo(ts2);
                    }
                });

                if (messages.size() > 0) {
                    db.collection((accountType.equals("client") || fieldUsername != null ? "professionals" : "clients"))
                            .document((fieldUsername != null ? messages.get(messages.size() - 1).name.toString() : chat_items.get(pos).getChatUsername()))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String name = "";
                                        String message = "";
                                        if (messages.size() > 0) {
                                            name = messages.get(messages.size() - 1).name.toString();
                                            message = messages.get(messages.size() - 1).message.toString();

                                            if (name.equals(username)) {
                                                name = "You";
                                            } else {
                                                if (task.getResult().exists()) {
                                                    name = task.getResult().getData().get("first_name").toString();
                                                } else {
                                                    holder.chats_msgpreview.setText("Click here to chat");
                                                }
                                            }
                                        }

                                        holder.chats_msgpreview.setText(name + ": " + message);
                                    }
                                }
                            });
                } else {
                    holder.chats_msgpreview.setText("Click here to chat");
                }

            }
        });

        String username = chat_items.get(pos).getChatUsername();
        holder.chats_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPasser.setChatUsername(username);

                if (fieldUsername != null) {
                    DataPasser.setIndustryChosen(true);
                }

                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentChat_InsideChat.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_items.size();
    }
}
