package com.mahmoudshaaban.butchers.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Guests;
import com.mahmoudshaaban.butchers.pojo.Story;
import com.mahmoudshaaban.butchers.pojo.StoryRv;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.ui.AddStoryActivity;
import com.mahmoudshaaban.butchers.ui.StoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryRvAdapter extends RecyclerView.Adapter<StoryRvAdapter.ViewHolder> {
   private Context mcontext;
   private List<StoryRv> mStory;

    public StoryRvAdapter(Context mcontext, List<StoryRv> mStory) {
        this.mcontext = mcontext;
        this.mStory = mStory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType == 0) {
           View view = LayoutInflater.from(mcontext).inflate(R.layout.add_story_item,parent,false);
           return new StoryRvAdapter.ViewHolder(view);
       } else {
           View view = LayoutInflater.from(mcontext).inflate(R.layout.storyrv_item,parent,false);
           return new StoryRvAdapter.ViewHolder(view);

       }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final StoryRv storyRv = mStory.get(position);

        userInfo(holder,storyRv.getUserid(), position);

        if (holder.getAdapterPosition() != 0){
            seenStory(holder , storyRv.getUserid());
        }
        if (holder.getAdapterPosition() == 0){
            myStory(holder.addstory_text,holder.story_plus,false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0){
                    myStory(holder.addstory_text,holder.story_plus,true);
                } else {
                    Intent intent = new Intent(mcontext, StoryActivity.class);
                    intent.putExtra("userid" , storyRv.getUserid());
                    mcontext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView story_photo , story_plus , story_photo_seen;
        public TextView story_username , addstory_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_username = itemView.findViewById(R.id.story_username);
            addstory_text = itemView.findViewById(R.id.add_storytext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void userInfo(final ViewHolder viewHolder , String userid , final int pos){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                Picasso.get().load(users.getImage()).into(viewHolder.story_photo);
                if (pos != 0){
                    Picasso.get().load(users.getImage()).into(viewHolder.story_photo_seen);
                    viewHolder.story_username.setText(users.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void myStory(final TextView textView , final ImageView imageView, final boolean click){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                long timecurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StoryRv storyRv = snapshot.getValue(StoryRv.class);
                    if (timecurrent > storyRv.getTimestart() && timecurrent < storyRv.getTimeend()){
                        count ++;
                    }
                }

                if (click){
                   if (count > 0){
                       AlertDialog alertDialog = new AlertDialog.Builder(mcontext).create();
                       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View story", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent intent = new Intent(mcontext, StoryActivity.class);
                               intent.putExtra("userid" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                               mcontext.startActivity(intent);
                           dialog.dismiss();
                           }
                       });
                       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add story", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent intent = new Intent(mcontext, AddStoryActivity.class);
                               mcontext.startActivity(intent);
                               dialog.dismiss();
                           }
                       });
                       alertDialog.show();
                   } else {
                       Intent intent = new Intent(mcontext, AddStoryActivity.class);
                       mcontext.startActivity(intent);
                   }
                }

                else {
                    if (count > 0){
                        textView.setText("My Story");
                        imageView.setVisibility(View.GONE);
                    } else {
                        textView.setText("Add story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void seenStory(final ViewHolder viewHolder, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Story").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (!snapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()
                            && System.currentTimeMillis() < snapshot.getValue(StoryRv.class).getTimeend()) {
                        i++;
                    }
                }
                if (i > 0){
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.story_photo_seen.setVisibility(View.GONE);
                } else {
                    viewHolder.story_photo.setVisibility(View.GONE);
                    viewHolder.story_photo_seen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
