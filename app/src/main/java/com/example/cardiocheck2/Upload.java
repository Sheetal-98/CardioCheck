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
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Upload extends AppCompatActivity {

    EditText mID, mName, mGender, mAge, mContact, mBP, mSpO2, mDisease, mComments, mHospital;
    Button mUpload;
    //ImageView uploadPicIV;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    StorageReference objectStorageReference;

    final int IMAGE_REQUEST = 71;
    Uri imageLocationPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mID = findViewById(R.id.pid);
        mName = findViewById(R.id.pName);
        mGender = findViewById(R.id.pGender);
        mAge = findViewById(R.id.pAge);
        mContact = findViewById(R.id.pContact);
        mBP = findViewById(R.id.pBP);
        mSpO2 = findViewById(R.id.pSpO2);
        mDisease = findViewById(R.id.pDisease);
        mComments = findViewById(R.id.pComments);
        mHospital = findViewById(R.id.pHospital);
        mUpload = findViewById(R.id.ubutton);
        //mChoose = findViewById(R.id.cbutton);
        //uploadPicIV =findViewById(R.id.imageID);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        objectStorageReference = FirebaseStorage.getInstance().getReference("ECGimages");

//        mChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mID.getText().toString().trim();
                String name = mName.getText().toString().trim();
                String gender = mGender.getText().toString().trim();
                String age = mAge.getText().toString().trim();
                String contact = mContact.getText().toString().trim();
                String bp = mBP.getText().toString().trim();
                String spo2 = mSpO2.getText().toString().trim();
                String disease = mDisease.getText().toString().trim();
                String comments = mComments.getText().toString().trim();
                String hospital = mHospital.getText().toString().trim();

                if(TextUtils.isEmpty(id)){
                    mID.setError("Patient ID is required");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    mName.setError("Patient Name is required");
                    return;
                }
                if(TextUtils.isEmpty(gender)){
                    mGender.setError("Patient Gender is required");
                    return;
                }
                if(TextUtils.isEmpty(age)){
                    mAge.setError("Patient Age is required");
                    return;
                }
                if(TextUtils.isEmpty(contact)){
                    mContact.setError("Patient Contact No. is required");
                    return;
                }
                if(TextUtils.isEmpty(bp)){
                    mBP.setError("Patient BP value is required");
                    return;
                }
                if(TextUtils.isEmpty(spo2)){
                    mSpO2.setError("Patient Oxygen Saturation level is required");
                    return;
                }
                if(TextUtils.isEmpty(disease)){
                    mDisease.setError("Patient Disease History is required");
                    return;
                }
                if(TextUtils.isEmpty(comments)){
                    mComments.setError("Comments is required");
                    return;
                }
                if(TextUtils.isEmpty(hospital)){
                    mHospital.setError("Hospital name is required");
                    return;
                }

                DocumentReference documentReference = fStore.collection("patients_details").document(id);
                Map<String, Object> patient = new HashMap<>();
                patient.put("ID", id);
                patient.put("name", name);
                patient.put("gender", gender);
                patient.put("age", age);
                patient.put("contact", contact);
                patient.put("BP", bp);
                patient.put("SpO2", spo2);
                patient.put("history", disease);
                patient.put("comments", comments);
                patient.put("hospital", hospital);
                documentReference.set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Upload.this, "Patient details added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                //uploadImage();



            }
        });
    }
    public void selectImage(){
        try{
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_REQUEST);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageLocationPath = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath);
                //uploadPicIV.setImageBitmap(objectBitmap);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage(){
        try{
            if(!mID.getText().toString().isEmpty() && imageLocationPath !=null){
                String nameOfImage = mID.getText().toString()+"."+getExtension(imageLocationPath);
                StorageReference imageRef = objectStorageReference.child(nameOfImage);

                UploadTask objectUploadTask = imageRef.putFile(imageLocationPath);
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
                            fStore.collection("ecg_images").document(mID.getText().toString()).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Upload.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Upload.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(Upload.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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