package com.example.cardiocheck2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Uploadfilesdashboard extends AppCompatActivity {

    Button dChooseFile, dScanFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfilesdashboard);

        dChooseFile = findViewById(R.id.buttonChooseFile);
        dScanFile = findViewById(R.id.buttonScanFile);

        dChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Uploadfiles.class));
            }
        });

        dScanFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Scanfiles.class));
            }
        });
    }
}