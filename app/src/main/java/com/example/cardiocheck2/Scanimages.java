package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Scanimages extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 102;
    Button scanCamera, scanUpload;
    EditText scanPatientID;
    ImageView scanImageView;
    public static final int CAM_PERM_CODE =101;
    String currentPhotoPath;
    File f;
    Uri contentUri;
    StorageReference scanStoragReference;
    FirebaseFirestore fStore2;
    FirebaseAuth fAuth2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanimages);

        scanCamera = findViewById(R.id.sopencamera);
        scanUpload = findViewById(R.id.suploadbutton);
        scanPatientID = findViewById(R.id.spid);
        scanImageView = findViewById(R.id.simageview);

        fAuth2 = FirebaseAuth.getInstance();
        fStore2 = FirebaseFirestore.getInstance();

        scanStoragReference = FirebaseStorage.getInstance().getReference();

        scanCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        scanUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });
    }
    private void askCameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.CAMERA}, CAM_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
//            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAM_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
//                openCamera();
            }
            else{
                Toast.makeText(this, "Camera Permission is required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void openCamera(){
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE);
//        //Toast.makeText(this, "accessed camera", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            scanImageView.setImageBitmap(image);
            if(resultCode == Activity.RESULT_OK){
                    f = new File(currentPhotoPath);
                    scanImageView.setImageURI(Uri.fromFile(f));
                    Log.d("tag", "Absolute url of image is" + Uri.fromFile(f));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }

        }
    }

    private void uploadImageToFirebase(){
//        StorageReference image = scanStoragReference.child("ECGimages/" + scanPatientID.getText().toString());
//        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d("tag", "onSuccess: Uploaded image url " + uri.toString());
//                    }
//                });
//                Toast.makeText(Scanimages.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Scanimages.this, "Upload Failed", Toast.LENGTH_SHORT).show();
//            }
//        });

        try{
            if(!scanPatientID.getText().toString().isEmpty() && contentUri !=null){
                String nameOfImage = scanPatientID.getText().toString()+".jpg";
                StorageReference imageRef = scanStoragReference.child("ECGimages/" + nameOfImage);

                UploadTask objectUploadTask = imageRef.putFile(contentUri);
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
                            fStore2.collection("ecg_images").document(scanPatientID.getText().toString()).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Scanimages.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Scanimages.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(Scanimages.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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

        private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp +"_";
//        File storagDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath =image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photofile = null;
            try{
                photofile = createImageFile();
            }catch (IOException e){

            }
            if(photofile!=null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photofile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private String getExtension1 (Uri uri){
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