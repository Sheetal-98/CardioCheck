package com.example.cardiocheck2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadImagesDashboard extends AppCompatActivity {

    Button dChooseECG, dScanECG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images_dashboard);

        dChooseECG = findViewById(R.id.button7);
        dScanECG = findViewById(R.id.button8);

        dChooseECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Uploadimages.class));
            }
        });

        dScanECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Scanimages.class));
            }
        });
    }
}