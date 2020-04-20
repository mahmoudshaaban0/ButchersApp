package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Posts;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.adapters.PostsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProfileActivity extends AppCompatActivity {

    CircleImageView userphoto;
    TextView username, userstatues, posts, following, followers;
    Button follow_button;
    ImageView messagebtn;
    RecyclerView posts_recycler;
    LinearLayoutManager layoutManager;
     Toolbar mtoolbar;
    FirebaseUser firebaseUser;
    List<Posts> postsssss;
    String profileId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);





        userphoto = findViewById(R.id.userphoto);
        username = findViewById(R.id.username);
        userstatues = findViewById(R.id.userstatues);
        posts = findViewById(R.id.posts_number);
        following = findViewById(R.id.following_number);
        followers = findViewById(R.id.followers_number);
        messagebtn = findViewById(R.id.message_button);

        posts_recycler = findViewById(R.id.profile_posts_recycler);
        layoutManager = new LinearLayoutManager(this);
        posts_recycler.setLayoutManager(layoutManager);
        posts_recycler.setHasFixedSize(true);
        postsssss = new ArrayList<>();

        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProfileActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });


        follow_button = findViewById(R.id.follow_btn_detailactvitiy);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences pref = getSharedPreferences("PREFS", MODE_PRIVATE);
        profileId = pref.getString("profileid", "none");


        userInfo();
        getFollowers();
        getnrPosts();
        readPosts();


        if (profileId.equals(firebaseUser.getUid())) {
            follow_button.setVisibility(View.INVISIBLE);
            messagebtn.setVisibility(View.INVISIBLE);

        } else {
            checkFollowing();
        }


        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = follow_button.getText().toString();

                if (btn.equals("Follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                } else if (btn.equals("Following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }




    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests").child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (DetailProfileActivity.this == null) {
                    return;
                }

                Users users = dataSnapshot.getValue(Users.class);



                Picasso.get().load(users.getImage()).placeholder(R.drawable.profile_image).into(userphoto);
                username.setText(users.getName());
                userstatues.setText(users.getStatues());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollowing() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    follow_button.setText("Following");
                    follow_button.setBackgroundResource(R.drawable.stroke_btn);
                } else {
                    follow_button.setText("Follow");
                    follow_button.setBackgroundResource(R.drawable.defult_state);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFollowers() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("followers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("following");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getnrPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Posts postsss = dataSnapshot1.getValue(Posts.class);
                    if (postsss.getPublisher().equals(profileId)) {
                        i++;
                    }
                }

                posts.setText("" + i);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {

         final DatabaseReference root_posts = FirebaseDatabase.getInstance().getReference().child("Posts");
            root_posts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Posts posts = dataSnapshot1.getValue(Posts.class);
                            if (posts.getPublisher().equals(profileId)) {
                                postsssss.add(posts);

                            }

                        }


                    }
                    PostsAdapter postsAdapter = new PostsAdapter(DetailProfileActivity.this, postsssss);
                    posts_recycler.setAdapter(postsAdapter);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }
}
