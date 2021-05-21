package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Downloadecg extends AppCompatActivity {

    EditText imagePID;
    Button imageECGButton, saveECGButton;
    ImageView imageECGView;

    FirebaseFirestore objectFirebaseFirestore;
    DocumentReference objectDocumentReference;

    FirebaseStorage firebaseStorage1;
    StorageReference storageReference1;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadecg);

        try{
            imagePID = findViewById(R.id.ecgpid);
            imageECGButton = findViewById(R.id.ecgimagebutton);
            imageECGView = findViewById(R.id.downloaded_image);
            saveECGButton = findViewById(R.id.ecgsavebutton);

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

        saveECGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
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
                        Toast.makeText(Downloadecg.this, "Wait for the file to be loaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Downloadecg.this, "Failed to load image", Toast.LENGTH_SHORT).show();
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

    public void saveImage(){
        storageReference1 = FirebaseStorage.getInstance().getReference();
        ref=storageReference1.child("ECGimages/" + imagePID.getText().toString() + ".jpg");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(Downloadecg.this, imagePID.getText().toString(), ".jpeg", DIRECTORY_DOWNLOADS,url);
                Toast.makeText(Downloadecg.this, "Download Started", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Downloadecg.this, "Error occurred in downloading", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);

    }
}