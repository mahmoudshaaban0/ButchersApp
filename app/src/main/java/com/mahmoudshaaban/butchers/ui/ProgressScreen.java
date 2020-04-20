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

        lottieAnimationView = findViewById(R.id.lottie_layer_name);



        startCheckAnimation();


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
