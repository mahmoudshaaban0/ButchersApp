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
import com.mahmoudshaaban.butchers.pojo.Messages;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.mahmoudshaaban.butchers.ui.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SendsMessagesAdapter  extends RecyclerView.Adapter<SendsMessagesAdapter.ViewHolder> {

    List<Users> mdata;
    Context mContext;
    FirebaseUser firebaseUser;
    private boolean ischat;
    String theLastMessage;
    private String reciverID;


    public SendsMessagesAdapter(List<Users> mdata, Context mContext, boolean ischat) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.ischat = ischat;

    }

    @NonNull
    @Override
    public SendsMessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_item, parent,false);

        SharedPreferences pref = mContext.getSharedPreferences("PREFS", MODE_PRIVATE);
        reciverID = pref.getString("profileid", "none");

        return new SendsMessagesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SendsMessagesAdapter.ViewHolder holder, int position) {
        final Users users = mdata.get(position);
        holder.usernamee.setText(mdata.get(position).getName());
        holder.messagee.setText(mdata.get(position).getStatues());
        Picasso.get().load(mdata.get(position).getImage()).into(holder.circleImageView222);

        if (ischat){
            if (users.getCheckonline().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        if (ischat){
            lastmessage(users.getId(),holder.messagee);
        } else{
            holder.messagee.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit();
                editor.putString("profileid" , users.getId());
                editor.apply();

                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userid",users.getId());
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {


        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView222 , img_on , img_off;
        public TextView usernamee , messagee;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView222 = itemView.findViewById(R.id.receiver_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            usernamee = itemView.findViewById(R.id.receiver_name);
            messagee = itemView.findViewById(R.id.receiver_message);



        }

    }



    // check for last messages
    private void lastmessage(final String userid , final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message").child(firebaseUser.getUid()).child(reciverID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Messages messages = snapshot.getValue(Messages.class);
                    if (reciverID.equals(firebaseUser.getUid()) && messages.getFrom().equals(userid) ||
                            reciverID.equals(userid) && messages.getFrom().equals(firebaseUser.getUid())){
                        theLastMessage = messages.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default" :
                        last_msg.setText("No message");
                        break;

                        default:
                            last_msg.setText(theLastMessage);
                            break;
                }

                theLastMessage = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
