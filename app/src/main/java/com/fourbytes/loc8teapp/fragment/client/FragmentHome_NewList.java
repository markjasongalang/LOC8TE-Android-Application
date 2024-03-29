package com.fourbytes.loc8teapp.fragment.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.LoginActivity;
import com.fourbytes.loc8teapp.Pair;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.SharedViewModel;
import com.fourbytes.loc8teapp.adapter.NewListAdapter;
import com.fourbytes.loc8teapp.newlistrecycler.NewListItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentHome_NewList extends Fragment {
    private View view;
    private View l;
    private View l2;

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
    private Map<String, Object> temp2;

    private ExtendedFloatingActionButton home_settings_FAB;
    private ExtendedFloatingActionButton location_settings_FAB;

    private Button logoutButton;

    private Boolean isAllFABVisible;
    private Boolean isAllFABVisible2;
    private Boolean isAllFABVisible3;

    private AppCompatCheckBox cbMed;
    private AppCompatCheckBox cbTech;
    private AppCompatCheckBox cbSkilled;
    private AppCompatCheckBox cbBusiness;
    private AppCompatCheckBox cbEduc;
    private AppCompatCheckBox cbLaw;
    private AppCompatCheckBox cbFood;
    private AppCompatCheckBox cbArts;

    private boolean[] bitArray;
    private String[] words;
    private boolean enableFilter;

    public FragmentHome_NewList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_new, container, false);

        // Get views from layout
        rvNewList = view.findViewById(R.id.new_recyclerview);
        home_settings_FAB = view.findViewById(R.id.home_settings);
        location_settings_FAB = view.findViewById(R.id.location_settings);
        l = view.findViewById(R.id.home_settings_toolbar);
        l2 = view.findViewById(R.id.location_settings_toolbar);
        logoutButton = view.findViewById(R.id.logout);
        cbMed = view.findViewById(R.id.cb_medical);
        cbTech = view.findViewById(R.id.cb_tech);
        cbSkilled = view.findViewById(R.id.cb_skilled);
        cbBusiness = view.findViewById(R.id.cb_business);
        cbEduc = view.findViewById(R.id.cb_educ);
        cbLaw = view.findViewById(R.id.cb_law);
        cbFood = view.findViewById(R.id.cb_food);
        cbArts = view.findViewById(R.id.cb_arts);

        // Initialize values
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        parentFragmentManager = getParentFragmentManager();
        bitArray = new boolean[100];
        enableFilter = false;
        words = new String[]{
                "medical",
                "technology",
                "skilled",
                "business",
                "education",
                "law",
                "food",
                "arts"
        };

        // Get username and account type of current user
        pair = null;
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe((LifecycleOwner) view.getContext(), data -> {
            pair = data;
        });

        username = pair.getUsername();
        accountType = pair.getAccountType();

        // Workaround to enable the visibility of the document
        temp = new HashMap<>();
        temp.put("exists", true);

        db.collection("client_homes").document(username).update(temp);

        db.collection("client_homes")
                .document(username)
                .collection("pro_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        HashSet<String> connected = new HashSet<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            if (documentSnapshot.getBoolean("is_connected") != null) {
                                if ((boolean) documentSnapshot.getData().get("is_connected")) {
                                    connected.add(documentSnapshot.getId());
                                }
                            }
                        }

                        temp2 = new HashMap<>();
                        temp2.put("is_connected", false);
                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!connected.contains(document.getId()) && (boolean) document.getData().get("verified")) {

                                            db.collection("client_homes")
                                                    .document(username)
                                                    .collection("pro_list")
                                                    .document(document.getId())
                                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                            if (value.exists()) {
//                                                                db.collection("client_homes")
//                                                                        .document(username)
//                                                                        .collection("pro_list")
//                                                                        .document(document.getId())
//                                                                        .update(temp2);
                                                            } else {
                                                                db.collection("client_homes")
                                                                        .document(username)
                                                                        .collection("pro_list")
                                                                        .document(document.getId())
                                                                        .set(temp2);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                }
                            }
                        });

                    }
                });

        l.setVisibility(view.GONE);
        l2.setVisibility(view.GONE);

        home_settings_FAB.shrink();
        location_settings_FAB.shrink();

        isAllFABVisible = false;
        isAllFABVisible2 = false;
        isAllFABVisible3 = false;

        home_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible) {
                    home_settings_FAB.extend();
                    l.setVisibility(view.VISIBLE);
                    isAllFABVisible = true;
                } else {
                    home_settings_FAB.shrink();
                    l.setVisibility(view.GONE);
                    isAllFABVisible = false;
                }
            }
        });

        location_settings_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible2) {
                    location_settings_FAB.extend();
                    l2.setVisibility(view.VISIBLE);
                    isAllFABVisible2 = true;
                } else {
                    location_settings_FAB.shrink();
                    l2.setVisibility(view.GONE);
                    isAllFABVisible2 = false;
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPasser.setUsername1(null);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        cbMed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[0]);
                } else {
                    removeFromBitArray(100, words[0]);
                }
                updateProList();
                processNewList();
            }
        });

        cbTech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[1]);
                } else {
                    removeFromBitArray(100, words[1]);
                }
                updateProList();
                processNewList();
            }
        });

        cbSkilled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[2]);
                } else {
                    removeFromBitArray(100, words[2]);
                }
                updateProList();
                processNewList();
            }
        });

        cbBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[3]);
                } else {
                    removeFromBitArray(100, words[3]);
                }
                updateProList();
                processNewList();
            }
        });

        cbEduc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[4]);
                } else {
                    removeFromBitArray(100, words[4]);
                }
                updateProList();
                processNewList();
            }
        });

        cbLaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[5]);
                } else {
                    removeFromBitArray(100, words[5]);
                }
                updateProList();
                processNewList();
            }
        });

        cbFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[6]);
                } else {
                    removeFromBitArray(100, words[6]);
                }
                updateProList();
                processNewList();
            }
        });

        cbArts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    addToBitArray(100, words[7]);
                } else {
                    removeFromBitArray(100, words[7]);
                }
                updateProList();
                processNewList();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        processNewList();
    }

    private void processNewList() {
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
                            if (document.getData().get("is_connected") != null) {
                                if ((boolean) document.getData().get("is_connected")) {
                                    connected.add(document.getId());
                                }
                            }

                        }

                        newList = new ArrayList<>();
                        db.collection("professionals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    newList = new ArrayList<>();
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        String temp = documentSnapshot.getData().get("field").toString();

                                        String dbField = "";
                                        for (int i = 0; i < temp.length(); i++) {
                                            if (!Character.isAlphabetic(temp.charAt(i))) {
                                                break;
                                            }
                                            dbField += temp.charAt(i);
                                        }

                                        if (!connected.contains(documentSnapshot.getId()) && isPresent(100, dbField) && (boolean) documentSnapshot.getData().get("verified")) {
                                            String fullName = documentSnapshot.getData().get("first_name") + " " + documentSnapshot.getData().get("last_name");
                                            String specific_job = documentSnapshot.getData().get("specific_job").toString();
                                            String field = documentSnapshot.getData().get("field").toString();

                                            // Get profile picture of current user
                                            StorageReference storageRef = storage.getReference();
                                            StorageReference pathReference = storageRef.child("profilePics/" + documentSnapshot.getId().toString() + "_profile.jpg");
                                            newList.add(new NewListItems(
                                                    documentSnapshot.getId(),
                                                    fullName,
                                                    specific_job,
                                                    field,
                                                    pathReference
                                            ));

                                        }
                                    }
                                    rvNewList.setAdapter(new NewListAdapter(getContext(), newList, parentFragmentManager));

                                }
                            }
                        });

                    }
                });
    }

    private void updateProList() {
        boolean[] marked = new boolean[8];
        Arrays.fill(marked, false);
        marked[0] = cbMed.isChecked();
        marked[1] = cbTech.isChecked();
        marked[2] = cbSkilled.isChecked();
        marked[3] = cbBusiness.isChecked();
        marked[4] = cbEduc.isChecked();
        marked[5] = cbLaw.isChecked();
        marked[6] = cbFood.isChecked();
        marked[7] = cbArts.isChecked();
        boolean isAllUnchecked = true;
        for (int i = 0; i < 8; i++) {
            if (marked[i]) {
                isAllUnchecked = false;
                break;
            }
        }
        if (isAllUnchecked) {
            enableFilter = false;
        } else {
            enableFilter = true;
        }
    }

    private void clearBitArray() {
        Arrays.fill(bitArray, false);
    }

    private void fillBitArray() {
        Arrays.fill(bitArray, true);
    }

    private boolean isPresent(int n, String word) {
        if (!enableFilter) {
            return true;
        }

        int index1 = hash1(word, n);
        int index2 = hash2(word, n);
        int index3 = hash3(word, n);
        int index4 = hash4(word, n);

        return bitArray[index1] && bitArray[index2] && bitArray[index3] && bitArray[index4];
    }

    private void addToBitArray(int n, String word) {
        int index1 = hash1(word, n);
        int index2 = hash2(word, n);
        int index3 = hash3(word, n);
        int index4 = hash4(word, n);

        bitArray[index1] = true;
        bitArray[index2] = true;
        bitArray[index3] = true;
        bitArray[index4] = true;
    }

    private void removeFromBitArray(int n, String word) {
        int index1 = hash1(word, n);
        int index2 = hash2(word, n);
        int index3 = hash3(word, n);
        int index4 = hash4(word, n);

        bitArray[index1] = false;
        bitArray[index2] = false;
        bitArray[index3] = false;
        bitArray[index4] = false;
    }

    private int hash1(String word, int n) {
        long hash = 0;
        for (int i = 0; i < word.length(); i++) {
            hash = (hash + ((int) word.charAt(i)));
            hash %= n;
        }
        return (int) hash;
    }

    private int hash2(String word, int n) {
        long hash = 1;
        for (int i = 0; i < word.length(); i++) {
            hash = hash + (long) Math.pow(19, i) * word.charAt(i);
            hash %= n;
        }
        return (int) hash;
    }

    private int hash3(String word, int n) {
        long hash = 7;
        for (int i = 0; i < word.length(); i++) {
            hash = (hash * 31 + word.charAt(i)) % n;
        }
        hash %= n;
        return (int) hash;
    }

    private int hash4(String word, int n) {
        long hash = 3, p = 7;
        for (int i = 0; i < word.length(); i++) {
            hash += hash * 7 + word.charAt(0) * (long) Math.pow(p, i);
            hash %= n;
        }
        return (int) hash;
    }
}