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

public class Downloadimages extends AppCompatActivity {

    EditText otherPID;
    Button otherFileButton, saveOtherFileButton;
    ImageView otherFileView;

    FirebaseFirestore objectFirebaseFirestore1;
    DocumentReference objectDocumentReference1;

    FirebaseStorage firebaseStorage2;
    StorageReference storageReference2;
    StorageReference ref1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadimages);

        try{
            otherPID = findViewById(R.id.imagepid);
            otherFileButton = findViewById(R.id.otherimagebutton);
            otherFileView = findViewById(R.id.downloaded_file);
            saveOtherFileButton = findViewById(R.id.othersavebutton);

            objectFirebaseFirestore1 = FirebaseFirestore.getInstance();


        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        otherFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    downloadFile();
                }
                catch (Exception e){
                    Toast.makeText(Downloadimages.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveOtherFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });
    }

    public void downloadFile(){
        try{
            if(!otherPID.getText().toString().isEmpty()){
                objectDocumentReference1 = objectFirebaseFirestore1.collection("other_images").document(otherPID.getText().toString());
                objectDocumentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkOfImage = documentSnapshot.getString("url");
                        Glide.with(Downloadimages.this).load(linkOfImage).into(otherFileView);
                        Toast.makeText(Downloadimages.this, "Wait for the file to be loaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Downloadimages.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Please provide patient ID", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFile(){
        storageReference2 = FirebaseStorage.getInstance().getReference();
        ref1=storageReference2.child("Otherfiles/" + otherPID.getText().toString() + ".jpg");

        ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(Downloadimages.this, otherPID.getText().toString(), ".jpeg", DIRECTORY_DOWNLOADS,url);
                Toast.makeText(Downloadimages.this, "Download Started", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Downloadimages.this, "Error occurred in downloading", Toast.LENGTH_SHORT).show();

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