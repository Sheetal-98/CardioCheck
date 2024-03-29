package com.example.cardiocheck2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Dashboard extends AppCompatActivity {
    TextView fullName, logoutUser;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button mUpload, mDownload, mUploadImages, mDownloadImages, mUploadFiles, mLoadFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fullName = findViewById(R.id.textView6);
        logoutUser = findViewById(R.id.textView8);
        mUpload = findViewById(R.id.upload_btn);
        mDownload = findViewById(R.id.download_btn);
        mUploadImages = findViewById(R.id.upload_images_btn);
        mDownloadImages = findViewById(R.id.download_images_btn);
        mUploadFiles = findViewById(R.id.upload_files_btn);
        mLoadFiles = findViewById(R.id.load_images_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                fullName.setText(documentSnapshot.getString("fName"));
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Upload.class));
            }
        });

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Download.class));
            }
        });

        mUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadImagesDashboard.class));
            }
        });

        mDownloadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Downloadimages.class));
            }
        });
        mUploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Uploadfilesdashboard.class));
            }
        });

        mLoadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Loadfiles.class));
            }
        });

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });
    }
}