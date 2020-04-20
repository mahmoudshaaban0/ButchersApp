package com.mahmoudshaaban.butchers.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.adapters.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriendsFragment extends Fragment {
    View view;
    RecyclerView users;
    private DatabaseReference userref;
    private String currentuser;
    FirebaseAuth mAuth;
    public String sender;
    Button follow_btn;
    List<Users> usersList;
    UsersAdapter usersAdapter;
    EditText search_barr;



    public FindFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_find_friends, container, false);

        users = view.findViewById(R.id.clients_recycler);
        search_barr = view.findViewById(R.id.search_bar);
        follow_btn = view.findViewById(R.id.follow_btn);
        users.setLayoutManager(new LinearLayoutManager(getContext()));
        userref = FirebaseDatabase.getInstance().getReference().child("Guests");
        mAuth = FirebaseAuth.getInstance();







        final DatabaseReference root_posts= FirebaseDatabase.getInstance().getReference().child("Guests");
        root_posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Users posts = dataSnapshot1.getValue(Users.class);
                        usersList.add(posts);


                    }
                     usersAdapter = new UsersAdapter(usersList,getContext());
                    users.setAdapter(usersAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        readUsers();
        search_barr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });








        // Inflate the layout for this fragment

        return view;
    }
    public void searchUsers(String s){
        Query query = FirebaseDatabase.getInstance().getReference().child("Guests").orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    usersList.add(users);

                }
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_barr.getText().toString().equals("")){
                    usersList.clear();
                    for (DataSnapshot dataSnapshot1 :  dataSnapshot.getChildren()){
                        Users users = dataSnapshot1.getValue(Users.class);
                        usersList.add(users);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }
}
