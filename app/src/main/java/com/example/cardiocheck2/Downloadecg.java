package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Downloadecg extends AppCompatActivity {

    EditText imagePID;
    Button imageECGButton;
    ImageView imageECGView;

    FirebaseFirestore objectFirebaseFirestore;
    DocumentReference objectDocumentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadecg);

        try{
            imagePID = findViewById(R.id.ecgpid);
            imageECGButton = findViewById(R.id.ecgimagebutton);
            imageECGView = findViewById(R.id.downloaded_image);

            objectFirebaseFirestore = FirebaseFirestore.getInstance();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        imageECGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });
    }

    public void downloadImage(){
        try{
            if(!imagePID.getText().toString().isEmpty()){
                objectDocumentReference = objectFirebaseFirestore.collection("ecg_images").document(imagePID.getText().toString());
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkOfImage = documentSnapshot.getString("url");
                        Glide.with(Downloadecg.this).load(linkOfImage).into(imageECGView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Downloadecg.this, "Failed to download image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Please provide patient ID", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}