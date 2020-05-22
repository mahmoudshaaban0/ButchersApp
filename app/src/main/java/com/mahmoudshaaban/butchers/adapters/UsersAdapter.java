package com.mahmoudshaaban.butchers.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.ui.DetailProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<Users> mdata;
    Context mContext;
    FirebaseUser firebaseUser;

    public UsersAdapter(List<Users> mdata, Context mContext) {
        this.mdata = mdata;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Users user = mdata.get(position);

        holder.button.setVisibility(View.VISIBLE);


        holder.username.setText(mdata.get(position).getName());
        holder.statues.setText(mdata.get(position).getStatues());
        Picasso.get().load(mdata.get(position).getImage()).placeholder(R.drawable.profile_image).into(holder.circleImageView22);

        isFollowing(user.getId(),holder.button);

        if (user.getId() != null && user.getId().equals(firebaseUser.getUid())){
            holder.button.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit();
                editor.putString("profileid" , user.getId());

                editor.apply();



                Intent myactivity = new Intent(mContext.getApplicationContext(), DetailProfileActivity.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                myactivity.putExtra("name", mdata.get(position).getName());
                myactivity.putExtra("image", mdata.get(position).getImage());


                mContext.getApplicationContext().startActivity(myactivity);


            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.button.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                } else {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView22;
        public TextView username , statues;
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView22 = itemView.findViewById(R.id.circleImageView2);
            username = itemView.findViewById(R.id.nameee);
            statues = itemView.findViewById(R.id.stutussss);
            button = itemView.findViewById(R.id.follow_btn);

        }
    }

    private void isFollowing(final String userid , final Button btn){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                if (dataSnapshot.child(userid).exists()){
                    btn.setText("Following ");
                    btn.setBackgroundResource(R.drawable.stroke_btn);
                } else {
                    btn.setText("Follow");
                    btn.setBackgroundResource(R.drawable.defult_state);
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
