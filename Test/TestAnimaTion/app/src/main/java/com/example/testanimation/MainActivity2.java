package com.example.testanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Activity 2");
        RelativeLayout layout = findViewById(R.id.layout);

        // lbr animation
        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .repeat(0)
                .playOn(layout);
    }
}