package com.mahmoudshaaban.butchers.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Clients;


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
