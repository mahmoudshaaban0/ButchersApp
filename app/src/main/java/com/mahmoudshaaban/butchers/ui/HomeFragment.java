package com.mahmoudshaaban.butchers.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.adapters.PersonAdapter;
import com.mahmoudshaaban.butchers.adapters.StoryRvAdapter;
import com.mahmoudshaaban.butchers.pojo.CategoriesModel;
import com.mahmoudshaaban.butchers.pojo.Posts;
import com.mahmoudshaaban.butchers.pojo.Story;
import com.mahmoudshaaban.butchers.pojo.StoryRv;
import com.mahmoudshaaban.butchers.adapters.PostsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View rootview;
    private DrawerLayout drawerLayout;
    RecyclerView mrecycler, posts_recycler ,storiesrecycler , instagram_story_recyclerview;
    FloatingActionButton post_icon;
    private DatabaseReference productref;
    LinearLayoutManager layoutManager;
    FragmentManager fragmentManager;
    List<CategoriesModel> categoriesModels;
    FirebaseAuth mAuth;
    List<Story> stories;
    List<Posts> postsssss;
    List<StoryRv> storyRvs;
    private static final String TAG = "HomeActivity";
    private List<String> followinglist;
    public FirebaseUser currentUser;

    FirebaseUser firebaseUser;
   private StoryRvAdapter storyRvAdapter;
   private String userID;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoriesModels = new ArrayList<>();
        categoriesModels.add(new CategoriesModel(R.drawable.dominik, "Miami Party"));
        categoriesModels.add(new CategoriesModel(R.drawable.playing, "David Guetta"));
        categoriesModels.add(new CategoriesModel(R.drawable.jackdanials, "Black Label"));
        categoriesModels.add(new CategoriesModel(R.drawable.retouch, "Smoking"));
        stories = new ArrayList<>();
        stories.add(new Story(R.drawable.woman_one));
        stories.add(new Story(R.drawable.woman_two));
        stories.add(new Story(R.drawable.woman_two));
        stories.add(new Story(R.drawable.woman_two));

        postsssss = new ArrayList<>();
        followinglist = new ArrayList<>();
        storyRvs = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        productref = FirebaseDatabase.getInstance().getReference().child("Posts");
        // toolbar
        mrecycler = rootview.findViewById(R.id.myrecycler);
        instagram_story_recyclerview = rootview.findViewById(R.id.recycler_view_story);
        instagram_story_recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        instagram_story_recyclerview.setLayoutManager(linearLayoutManager);
        storyRvAdapter = new StoryRvAdapter(getContext(),storyRvs);
        instagram_story_recyclerview.setAdapter(storyRvAdapter);
        mAuth = FirebaseAuth.getInstance();
         firebaseUser = mAuth.getCurrentUser();


/*
        storiesrecycler = rootview.findViewById(R.id.stories_recycler);
*/

        LinearLayoutManager layoutManager22 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false );

        post_icon = rootview.findViewById(R.id.post_icon);
        posts_recycler = rootview.findViewById(R.id.posts_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        posts_recycler.setLayoutManager(layoutManager);
        posts_recycler.setHasFixedSize(true);

         LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false );
        mrecycler.setLayoutManager(layoutManager2);



        checkfollowing();


        return rootview;
    }

    private void checkfollowing(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("following");


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    followinglist.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        followinglist.add(snapshot.getKey());

                    }

                    readPosts();
                    readStory();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    private void readStory(){
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                storyRvs.clear();
                storyRvs.add(new StoryRv("",0,0,"",FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id : followinglist){
                    int countStory = 0;
                    StoryRv storyRv = null;
                    for (DataSnapshot snapshot : dataSnapshot.child(id).getChildren()){
                        storyRv = snapshot.getValue(StoryRv.class);
                        if (timecurrent > storyRv.getTimestart() && timecurrent < storyRv.getTimeend()){
                            countStory++;
                        }
                    }

                    if (countStory > 0){
                        storyRvs.add(storyRv);
                    }
                }
                storyRvAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readPosts(){
        final DatabaseReference root_posts= FirebaseDatabase.getInstance().getReference().child("Posts");
        root_posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Posts posts =dataSnapshot1.getValue(Posts.class);
                        for (String id : followinglist){
                            if (posts.getPublisher().equals(id)){
                                postsssss.add(posts);


                            }


                        }




                    }
                    PostsAdapter postsAdapter = new PostsAdapter(getContext(),postsssss);
                    posts_recycler.setAdapter(postsAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









/*
        storiesrecycler.setLayoutManager(layoutManager22);
*/
/*
        StroryAdapter stories_recycler2 = new StroryAdapter(getContext(),stories);
        storiesrecycler.setAdapter(stories_recycler2);*/
        PersonAdapter mAdapter = new PersonAdapter(getContext(), categoriesModels);
        // 4. set adapter
        mrecycler.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mrecycler,true);
        post_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        }


    }




}
