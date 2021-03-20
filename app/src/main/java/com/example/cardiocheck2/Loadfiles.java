package com.example.cardiocheck2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Loadfiles extends AppCompatActivity {

    Button mloadecgimages, mloadotherimages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadfiles);

        mloadecgimages = findViewById(R.id.load_ecg_images_btn);
        mloadotherimages = findViewById(R.id.load_other_images_btn);

        mloadecgimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Loadecgimages.class));
            }
        });
    }
}