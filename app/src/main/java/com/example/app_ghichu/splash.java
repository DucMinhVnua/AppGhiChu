package com.example.app_ghichu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class splash extends AppCompatActivity {

    ImageView ivNote;
    LottieAnimationView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivNote = findViewById(R.id.iv_note);
        gif = findViewById(R.id.gif);

        ivNote.animate().translationY(-1000).setDuration(2000).setStartDelay(3000);
        gif.animate().translationY(1000).setDuration(2000).setStartDelay(3000);

        // Delay đợi chạy hết animation rồi intent
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent tới MainActivity
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
            }
        }, 4400);
    }
}