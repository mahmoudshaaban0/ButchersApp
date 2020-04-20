package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.StoryRv;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    int count = 0;
    long pressTime = 0L;
    long limit = 500L;
    StoriesProgressView storiesProgressView;
    ImageView image, story_photo;
    TextView story_username;
    List<String> images;
    List<String> storyids;
    String userId;


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        story_photo = findViewById(R.id.story_photo);
        story_username = findViewById(R.id.story_username);

        userId = getIntent().getStringExtra("userid");

        getStories(userId);
        userInfo(userId);

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);



        View skip = findViewById(R.id.skip);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);





    }

    @Override
    public void onNext() {
        Picasso.get().load(images.get(++count)).into(image);
    }

    @Override
    public void onPrev() {
        if ((count - 1) < 0) return;
        Picasso.get().load(images.get(--count)).into(image);
    }

    @Override
    public void onComplete() {
        finish();

    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    private void getStories(String userid){
        images = new ArrayList<>();
        storyids = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Story")
                .child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                storyids.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StoryRv storyRv = snapshot.getValue(StoryRv.class);
                    long timecurrent = System.currentTimeMillis();

                    if (timecurrent > storyRv.getTimestart() && timecurrent < storyRv.getTimeend()){
                        images.add(storyRv.getImageuri());
                        storyids.add(storyRv.getStoryid());
                    }
                }

                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(StoryActivity.this);
                storiesProgressView.startStories(count);


                Picasso.get().load(images.get(count)).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userInfo(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests")
                .child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                Picasso.get().load(users.getImage()).into(story_photo);
                story_username.setText(users.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
