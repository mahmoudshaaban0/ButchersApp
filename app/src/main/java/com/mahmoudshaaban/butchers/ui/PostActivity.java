package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    ImageView product_image , postimage;
    EditText post_description;
    public static final int GALLERY_PICK = 1;
    private Uri imageuri;
    private StorageReference productImagesRef;
    Button post_btn;
    CardView cardView;

    private DatabaseReference ProductsRef;
    private String post_Desc , savecurrenttime , productkey , downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar2 = findViewById(R.id.toolbarDrawer);
        circleImageView = findViewById(R.id.circleImageView);
        product_image = findViewById(R.id.product_image);
        postimage = findViewById(R.id.post_image);
        post_description = findViewById(R.id.write_post);
        post_btn = findViewById(R.id.post_btn);
        cardView = findViewById(R.id.cardView);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(Prevalent.currentOnlineUser.getPassword());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(circleImageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePost();
            }
        });




    }




    public void openGallery(){
        Intent open_image = new Intent();
        open_image.setAction(Intent.ACTION_GET_CONTENT);
        open_image.setType("image/*");
        startActivityForResult(open_image,GALLERY_PICK );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            postimage.setImageURI(imageuri);
            postimage.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }

    }

    private void validatePost() {
        post_Desc = post_description.getText().toString();
        if (post_Desc.isEmpty()) {
            post_btn.setEnabled(false);
        } else {
            post_btn.setEnabled(true);
            StoreImageInformation();
        }

    }

    private void StoreImageInformation() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm a");
        savecurrenttime = currenttime.format(calendar.getTime());

        productkey = savecurrenttime;

        // StorageReference to create a folder in database
        final StorageReference filepath = productImagesRef.child(imageuri.getLastPathSegment() + productkey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(PostActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(PostActivity.this, "got product image url successfully", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabse();
                        }

                    }
                });

            }
        });

    }

    private void SaveProductInfoToDatabse() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(Prevalent.currentOnlineUser.getPassword());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()) {

                        String username = dataSnapshot.child("Username").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();


                        HashMap<String, Object> productmap = new HashMap<>();
                        productmap.put("Image", downloadImageUrl);
                        productmap.put("Pid", productkey);
                        productmap.put("Description", post_Desc);
                        productmap.put("Username" , username);
                        productmap.put("UserImage",image);
                        ProductsRef.child(productkey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(PostActivity.this, "Post Created", Toast.LENGTH_SHORT).show();

                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(PostActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }
}
