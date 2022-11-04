package com.fourbytes.loc8teapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.chatsrecycler.InsideChatItems;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.adapter.InsideChatAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat_InsideChat extends Fragment {
    private View view;

    private FirebaseFirestore db;

    private FragmentManager parentFragmentManager;

    private RecyclerView rvInsideChat;

    private TextView tvProfessionalName;

    private AppCompatButton btnBack;

    private List<InsideChatItems> insideChatItemsList;

    private SharedViewModel viewModel;

    private Pair pair;

    private String username;
    private String accountType;

    public FragmentChat_InsideChat() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_inside_chat, container, false);

        // Get views from layout
        rvInsideChat = view.findViewById(R.id.insidechat_recyclerview);
        btnBack = view.findViewById(R.id.btn_back);
        tvProfessionalName = view.findViewById(R.id.tv_professional_name);

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

        parentFragmentManager.setFragmentResultListener("data_from_prev", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String professionalUsername = result.getString("pro_username");
                tvProfessionalName.setText(professionalUsername);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFragmentManager.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        insideChatItemsList = new ArrayList<>();
        rvInsideChat.setLayoutManager(new LinearLayoutManager(view.getContext()));

        insideChatItemsList.add(new InsideChatItems(
                "Hello!",
                R.drawable.random2,
                InsideChatItems.layout_right
        ));

        insideChatItemsList.add(new InsideChatItems(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                R.drawable.random2,
                InsideChatItems.layout_right
        ));

        insideChatItemsList.add(new InsideChatItems(
                "Thanks for contacting me!",
                R.drawable.random1,
                InsideChatItems.layout_left
        ));

        rvInsideChat.setAdapter(new InsideChatAdapter(view.getContext(), insideChatItemsList));
    }
}