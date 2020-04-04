package com.mahmoudshaaban.butchers.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Prevalent;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.viewholder.UsersViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

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


    public FindFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_find_friends, container, false);

        users = view.findViewById(R.id.clients_recycler);
        follow_btn = view.findViewById(R.id.follow_btn);
        users.setLayoutManager(new LinearLayoutManager(getContext()));
        userref = FirebaseDatabase.getInstance().getReference().child("Clients");
        mAuth = FirebaseAuth.getInstance();






        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(userref,Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                holder.username.setText(model.getUsername());
                holder.statues.setText(model.getStatues());
                Picasso.get().load(model.getImage()).into(holder.circleImageView22);
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_item, parent,false);
                return new UsersViewHolder(v);
            }
        };
        users.setAdapter(adapter);
        adapter.startListening();



    }
}
