package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Uploadfiles extends AppCompatActivity {

    Button mChooseFile, mUploadFile;
    ImageView mOtherImage;
    EditText mpatientID1;

    final int IMAGE_REQUEST2 = 71;
    Uri imageLocationPath2;
    FirebaseFirestore fStore2;
    FirebaseAuth fAuth2;
    StorageReference objectStorageReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfiles);

        mChooseFile = findViewById(R.id.button4);

        mUploadFile = findViewById(R.id.button6);
        mOtherImage = findViewById(R.id.imageView4);
        mpatientID1 = findViewById(R.id.editTextTextPersonName);

        fAuth2 = FirebaseAuth.getInstance();
        fStore2 = FirebaseFirestore.getInstance();
        objectStorageReference2 = FirebaseStorage.getInstance().getReference("Otherfiles");

        mChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFiles();
            }
        });

        mUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFiles();
            }
        });


    }
    public void chooseFiles(){
        try{
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_REQUEST2);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageLocationPath2 = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath2);
                mOtherImage.setImageBitmap(objectBitmap);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadFiles(){
        try{
            if(!mpatientID1.getText().toString().isEmpty() && imageLocationPath2 !=null){
                String nameOfImage = mpatientID1.getText().toString()+"."+getExtension(imageLocationPath2);
                StorageReference imageRef = objectStorageReference2.child(nameOfImage);

                UploadTask objectUploadTask = imageRef.putFile(imageLocationPath2);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Map<String, String> objectMap = new HashMap<>();
                            objectMap.put("url", task.getResult().toString());
                            fStore2.collection("other_images").document(mpatientID1.getText().toString()).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Uploadfiles.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Uploadfiles.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(Uploadfiles.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
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

    private String getExtension (Uri uri){
        try{
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}