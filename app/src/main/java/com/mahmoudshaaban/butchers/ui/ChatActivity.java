package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.adapters.SendsMessagesAdapter;
import com.mahmoudshaaban.butchers.pojo.Messages;
import com.mahmoudshaaban.butchers.adapters.MessagesAdapter;
import com.mahmoudshaaban.butchers.pojo.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private String messageSenderId , reciverID , MessageReceivername , messagereceiverimage;
    ImageView sendmessagebutton , send_photo , send_pdf;
    private DatabaseReference mrootref;
    EditText messageinputtext;
    private final List<Messages> messagesList = new ArrayList<>();
    private MessagesAdapter messagesAdapter;
    private Toolbar mtoolbar;
    private RecyclerView usermessagelist;
    CircleImageView circleImageView;
    TextView textView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseUser firebaseUser;
    private Uri fileUri;
    private String myuri = "";
    private StorageTask uploadTask;
    ProgressDialog progressDialog;
    Intent intent;
    Users users;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarview = layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionbarview);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        sendmessagebutton = findViewById(R.id.send_message_text);
        messageinputtext = findViewById(R.id.editText);
        reference = FirebaseDatabase.getInstance().getReference();

        circleImageView = findViewById(R.id.custom_profile_image);
        textView = findViewById(R.id.custom_profile_name);

        send_photo = findViewById(R.id.send_photo);
        send_pdf = findViewById(R.id.send_pdf);

        messageSenderId = mAuth.getCurrentUser().getUid();
        mrootref = FirebaseDatabase.getInstance().getReference();
        messagesAdapter = new MessagesAdapter(messagesList  );

        usermessagelist = findViewById(R.id.messages_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        usermessagelist.setLayoutManager(linearLayoutManager);
        usermessagelist.setAdapter(messagesAdapter);
        progressDialog = new ProgressDialog(this);


        SharedPreferences pref = getSharedPreferences("PREFS", MODE_PRIVATE);
        reciverID = pref.getString("profileid", "none");

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);



        mrootref.child("Message").child(messageSenderId).child(reciverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messagesAdapter.notifyDataSetChanged();

                        usermessagelist.smoothScrollToPosition(usermessagelist.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

      ;


            reference = FirebaseDatabase.getInstance().getReference().child("Guests").child(reciverID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("image")){
                            users = dataSnapshot.getValue(Users.class);

                            textView.setText(users.getName());
                            Picasso.get().load(users.getImage()).placeholder(R.drawable.profile_image).into(circleImageView);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });











        send_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,"Select Image") , 438);
            }
        });





        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageinputtext.equals("")){
                    sendmessagebutton.setEnabled(false);
                } else {
                    sendMessage();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            progressDialog.setTitle("Sending File");
            progressDialog.setMessage("Please wait , while we are sending that file");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            fileUri = data.getData();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

            final String messagesenderref = "Message/" + messageSenderId + "/" + reciverID;
            final String messagreceiverref = "Message/" + reciverID + "/" + messageSenderId;

            DatabaseReference usermessageref = mrootref.child("Messages")
                    .child(messagesenderref)
                    .child(reciverID).push();

            final String messagekey = usermessageref.getKey();

            final StorageReference filepath = storageReference.child(messagekey + "." + "jpg");

            uploadTask = filepath.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return filepath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    myuri = downloadUri.toString();


                    Map messageImage = new HashMap();
                    messageImage.put("message",myuri);
                    messageImage.put("name",fileUri.getLastPathSegment());
                    messageImage.put("type","image");
                    messageImage.put("from",messageSenderId);
                    messageImage.put("to",reciverID);

                    Map messagebodydetails = new HashMap();
                    messagebodydetails.put(messagesenderref + "/" + messagekey , messageImage);
                    messagebodydetails.put(messagreceiverref + "/" + messagekey , messageImage);


                    mrootref.updateChildren(messagebodydetails).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(ChatActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }

                            messageinputtext.setText("");
                        }
                    });


                }
            });


        }

    }

    private void sendMessage() {
        String messagetext = messageinputtext.getText().toString();
        String messagesenderref = "Message/" + messageSenderId + "/" + reciverID;
        String messagreceiverref = "Message/" + reciverID + "/" + messageSenderId;


        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference().child("ChatList")
                .child(firebaseUser.getUid())
                .child(reciverID);

        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatref.child("id").setValue(reciverID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        DatabaseReference usermessageref = mrootref.child("Messages")
                .child(messagesenderref)
                .child(reciverID).push();


        String messagekey = usermessageref.getKey();

        Map messagetextbody = new HashMap();
        messagetextbody.put("message",messagetext);
        messagetextbody.put("type","text");
        messagetextbody.put("from",messageSenderId);

        Map messagebodydetails = new HashMap();
        messagebodydetails.put(messagesenderref + "/" + messagekey , messagetextbody);
        messagebodydetails.put(messagreceiverref + "/" + messagekey , messagetextbody);



        mrootref.updateChildren(messagebodydetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(ChatActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }

                messageinputtext.setText("");
            }
        });



    }

    private void statues(String stus){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("checkonline" , stus);
        reference.updateChildren(hashMap);


    }


    @Override
    protected void onResume() {
        super.onResume();
        statues("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statues("offline");
    }
}

