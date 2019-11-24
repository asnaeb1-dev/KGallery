package com.abhigyan.user.galleryapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.galleryapp.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView appIcon;
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appIcon = findViewById(R.id.app_icon);
        appName = findViewById(R.id.app_name);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                animate();

            }
        }, 3000);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();

            }
        }, 3500);

    }
    private void animate(){
        appIcon.animate().translationXBy(2000f).setDuration(1000);
        appName.animate().translationXBy(-2000f).setDuration(1000);
    }
}
