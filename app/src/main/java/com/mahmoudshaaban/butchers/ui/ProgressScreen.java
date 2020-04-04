package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Clients;
import com.mahmoudshaaban.butchers.pojo.Prevalent;

import io.paperdb.Paper;

public class ProgressScreen extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ProgressDialog loadingdialog;
    Clients userData;
    private String data = "Clients";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_screen);

        Paper.init(this);
        lottieAnimationView = findViewById(R.id.lottie_layer_name);
        loadingdialog = new ProgressDialog( this);

        String userEmailKey = Paper.book().read(Prevalent.userEmailAddress);
        String userPasswordKey = Paper.book().read(Prevalent.userPassword);




        if (userEmailKey != "" && userPasswordKey != ""){
            if (!TextUtils.isEmpty(userEmailKey) && !TextUtils.isEmpty(userPasswordKey)){
                Allowacess(userEmailKey,userPasswordKey);


            } else {
                Intent intent = new Intent(ProgressScreen.this,MainActivity.class);
                startActivity(intent);
            }
        }

        startCheckAnimation();


    }

    private void Allowacess(final String userEmailKey, final String userPasswordKey) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(data).child(userPasswordKey).exists()) {

                    userData = dataSnapshot.child(data).child(userPasswordKey).getValue(Clients.class);
                    if (userData.getEmail().equals(userEmailKey)) {
                        if (userData.getPassword().equals(userPasswordKey)) {
                            if (data.equals("Clients")) {
                                Toast.makeText(ProgressScreen.this, "Logged in successfully ... ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProgressScreen.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);
                                finish();

                            }
                        }

                    }
                }
                else {
                    Toast.makeText(ProgressScreen.this, "This account doesn't exist", Toast.LENGTH_SHORT).show();

                }


            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void startCheckAnimation() {

            ValueAnimator animator = ValueAnimator.ofFloat(0f,1f).setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lottieAnimationView.setProgress((Float)animation.getAnimatedValue());

                }
            });
            if (lottieAnimationView.getProgress() == 0f){
                animator.start();
            } else {
                lottieAnimationView.setProgress(0f);
            }




    }
}
