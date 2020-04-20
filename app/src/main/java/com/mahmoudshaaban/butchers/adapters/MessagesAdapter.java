package com.mahmoudshaaban.butchers.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Messages;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageviewHolder> {

    private List<Messages> usermessageslist;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;




    public MessagesAdapter (List<Messages> usermessageslist) {
      this.usermessageslist = usermessageslist;
    }


    public class MessageviewHolder extends RecyclerView.ViewHolder{

        public TextView sendermessagetext , recivermessagetext;
        public ImageView messagesenderPicture , messagereceiverPicture;



        public MessageviewHolder(@NonNull View itemView) {
            super(itemView);
            sendermessagetext = itemView.findViewById(R.id.sender_messsage_text);
            recivermessagetext = itemView.findViewById(R.id.receiver_message_text);
            messagereceiverPicture = itemView.findViewById(R.id.message_reciver_image_view);
            messagesenderPicture = itemView.findViewById(R.id.message_sender_image_view);

        }
    }

    @NonNull
    @Override
    public MessageviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageviewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageviewHolder holder, int position) {
        String messagesenderid = mAuth.getCurrentUser().getUid();
        Messages messages = usermessageslist.get(position);


        String fromuserid = messages.getFrom();
        String fromusertype = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Guests").child(fromuserid);


        holder.recivermessagetext.setVisibility(View.GONE);
        holder.sendermessagetext.setVisibility(View.GONE);
        holder.messagesenderPicture.setVisibility(View.GONE);
        holder.messagereceiverPicture.setVisibility(View.GONE);

        if (fromusertype.equals("text")){


        if (fromuserid.equals(messagesenderid)){
            holder.sendermessagetext.setVisibility(View.VISIBLE);

            holder.sendermessagetext.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.sendermessagetext.setTextColor(Color.WHITE);
            holder.sendermessagetext.setText(messages.getMessage());

        }
        else {


            holder.recivermessagetext.setVisibility(View.VISIBLE);

            holder.recivermessagetext.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.recivermessagetext.setTextColor(Color.BLACK);
            holder.recivermessagetext.setText(messages.getMessage());


             }


        }

        else if (fromusertype.equals("image")){
            if (fromuserid.equals(messagesenderid)) {

                holder.messagesenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messagesenderPicture);

            }
            else {
                holder.messagereceiverPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messagereceiverPicture);
            }


        }



    }


    @Override
    public int getItemCount() {
        return usermessageslist.size();
    }

}
