package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mahmoudshaaban.butchers.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {

    private Uri mImageUri;
    String myUrl = "";
    private StorageTask storageTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);


        storageReference = FirebaseStorage.getInstance().getReference().child("Story");
        CropImage.activity()
                .setAspectRatio(9,14)
                .start(AddStoryActivity.this);



    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void StoryPublish(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (mImageUri != null){
            final StorageReference imageReferance = storageReference.child(System.currentTimeMillis()
            + "." + getFileExtention(mImageUri));

            storageTask = imageReferance.putFile(mImageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReferance.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();


                        String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Story")
                                .child(myID);

                        String storyID = reference.push().getKey();
                        long timeend = System.currentTimeMillis() + 86400000; // 1 day
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageuri" , myUrl);
                        hashMap.put("timestart" , ServerValue.TIMESTAMP);
                        hashMap.put("timeend" , timeend);
                        hashMap.put("storyid" , storyID);
                        hashMap.put("userid" , myID);


                        reference.child(storyID).setValue(hashMap);
                        pd.dismiss();
                        finish();

                    } else {
                        Toast.makeText(AddStoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            StoryPublish();
        } else {
            Toast.makeText(this, "Something gone Wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStoryActivity.this,MainActivity.class));
            finish();
        }
    }
}
