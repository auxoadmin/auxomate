package com.auxomate.mynewself.mynewself.activities;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.auxomate.mynewself.mynewself.R;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIMER = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent streamPlayerHome = new Intent(SplashActivity.this,WelcomeActivity.class);
                startActivity(streamPlayerHome);
                finish();
            }
        }, SPLASH_TIMER);
    }
}
