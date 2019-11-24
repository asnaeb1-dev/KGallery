package com.abhigyan.user.galleryapp.Activities;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.abhigyan.user.galleryapp.R;
import com.ankushgrover.hourglass.Hourglass;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private ArrayList<String> imgData = new ArrayList<>();
    private ImageView slideShowIV;
    private FloatingActionButton faab;
    private int slideShowLength;
    private Animation anim;
    private int position = 0;
    private boolean isPlaying = false, isOver = false;

    private Hourglass hourglass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        faab = findViewById(R.id.playPause);
        slideShowIV = findViewById(R.id.slideShowIV);
        imgData = getIntent().getStringArrayListExtra("image_list");
        anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        slideShowLength = (imgData.size()+1)*3000;
        faab.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));
    }

    @Override
    protected void onStart() {
        super.onStart();
        playSlideShow();
        isPlaying = true;
        faab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOver) {
                    if (isPlaying) {
                        hourglass.pauseTimer();
                        isPlaying = false;
                        faab.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_play));

                    } else {
                        hourglass.startTimer();
                        isPlaying = true;
                        faab.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));
                    }
                }else{
                    isOver = false;
                    playSlideShow();
                }
            }
        });
    }

    private void playSlideShow(){
         hourglass = new Hourglass(slideShowLength, 3000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                // Update UI
                if(position<imgData.size()) {
                    position = position + 1;
                    isOver = false;
                    Glide.with(getApplicationContext()).asDrawable().load(imgData.get(position)).into(slideShowIV);
                    slideShowIV.startAnimation(anim);
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTimerFinish() {
                // Timer finished
                faab.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_white_24dp));
                Glide.with(getApplicationContext()).asDrawable().load(imgData.get(position)).into(slideShowIV);
                isOver = true;
            }
        };

        hourglass.startTimer();
    }
}
