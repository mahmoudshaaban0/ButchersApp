package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.databinding.ActivityProfileBinding;
import com.mahmoudshaaban.butchers.pojo.Prevalent;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    private String checker = "";
    private Uri imagURI;
    private StorageTask uploadtask;
    private StorageReference StoreImage;
    private String myUri = "";
    DatabaseReference databaseReference;
    public static final int GALLERY_PICK = 1;
    private StorageReference storageprofilepicturereference;
    private DatabaseReference Rootref;
    CircleImageView circleImageView;
    EditText username, statues, phone;
    TextView changeimagetext;
    TextView donetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.edit_username);
        statues = findViewById(R.id.edit_statues);
        phone = findViewById(R.id.edit_Phone);
        donetext = findViewById(R.id.done_text);
        circleImageView = findViewById(R.id.circleImageView);
        changeimagetext = findViewById(R.id.change_Profile_Image_Text);

        userInfoDisplay(circleImageView , username , statues, phone);


        Rootref = FirebaseDatabase.getInstance().getReference();
        storageprofilepicturereference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        donetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {

                    userInfoSaved();

                } else {

                    updateOnlyUserInfo();
                }



            }
        });
        changeimagetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(ProfileActivity.this);
            }
        });


    }



    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Clients");
        Map<String , Object> usermap = new HashMap<>();
        usermap.put("Username",username.getText().toString());
        usermap.put("Statues",statues.getText().toString());
        usermap.put("Phone",phone.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPassword()).updateChildren(usermap);
        Toast.makeText(ProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imagURI = result.getUri();

            circleImageView.setImageURI(imagURI);
        }
        else {
            Toast.makeText(this, "Error: Try again", Toast.LENGTH_SHORT).show();

        }
    }

    private void userInfoSaved() {


        if (TextUtils.isEmpty(username.getText().toString())) {
            Toast.makeText(this, "name is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(statues.getText().toString())) {
            Toast.makeText(this, "Statues is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone.getText().toString())) {
            Toast.makeText(this, "Phone is mandatory", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadimage();
        }


    }

    private void uploadimage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait , while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imagURI != null) {
            final StorageReference fileRef = storageprofilepicturereference
                    .child(Prevalent.currentOnlineUser.getPassword() + ".jpg");
            uploadtask = fileRef.putFile(imagURI);


            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                    Uri downloaduri = (Uri) task.getResult();
                    myUri = downloaduri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Clients");
                    Map<String , Object> usermap = new HashMap<>();
                    usermap.put("Username",username.getText().toString());
                    usermap.put("Statues",statues.getText().toString());
                    usermap.put("Phone",phone.getText().toString());
                    usermap.put("image",myUri);

                    ref.child(Prevalent.currentOnlineUser.getPassword()).updateChildren(usermap);
                    progressDialog.dismiss();



                } else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Error: Occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void userInfoDisplay(final CircleImageView circleImageView, final EditText username, final EditText statues, final EditText phone) {

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(Prevalent.currentOnlineUser.getPassword());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // if phonenumber exists
                if (dataSnapshot.exists()){
                    // we make second if statment because in model class user we don't have an image varriable
                    if (dataSnapshot.child("image").exists()){
                        // make sure you use the same name in the database child

                        String image = dataSnapshot.child("image").getValue().toString();
                        String name =  dataSnapshot.child("Username").getValue().toString();
                        String phoneee = dataSnapshot.child("Phone").getValue().toString();
                        String statues1 = dataSnapshot.child("Statues").getValue().toString();

                        Picasso.get().load(image).into(circleImageView);
                        username.setText(name);
                        statues.setText(statues1);
                        phone.setText(phoneee);




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}






