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

public class Uploadimages extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 102;
    Button mChooseECG, mUploadECGImage;
    ImageView mECGImage;
    EditText mpatientID;



    final int IMAGE_REQUEST1 = 71;
    Uri imageLocationPath1;
    FirebaseFirestore fStore1;
    FirebaseAuth fAuth1;
    StorageReference objectStorageReference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimages);
        mChooseECG = findViewById(R.id.button);
//        mScanECG = findViewById(R.id.button2);
        mUploadECGImage = findViewById(R.id.button3);
        mECGImage = findViewById(R.id.imageView3);
        mpatientID = findViewById(R.id.editTextpid);

        fAuth1 = FirebaseAuth.getInstance();
        fStore1 = FirebaseFirestore.getInstance();
        objectStorageReference1 = FirebaseStorage.getInstance().getReference("ECGimages");

        mChooseECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });

//        mScanECG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                askCameraPermissions();
//            }
//        });

        mUploadECGImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages();
            }
        });

    }
//    private void askCameraPermissions(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.CAMERA}, CAM_PERM_CODE);
//        }
//        else{
//            dispatchTakePictureIntent();
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == CAM_PERM_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                dispatchTakePictureIntent();
//            }
//            else{
//                Toast.makeText(this, "Camera Permission is required to Use Camera", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



    public void chooseImages(){
        try{
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_REQUEST1);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageLocationPath1 = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath1);
                mECGImage.setImageBitmap(objectBitmap);
            }
//            if( requestCode == CAMERA_REQUEST_CODE){
////                Bitmap image = (Bitmap) data.getExtras().get("data");
////                mECGImage.setImageBitmap(image);
//                if(resultCode == Activity.RESULT_OK){
////                    imageLocationPath1 = data.getData();
//                    File f = new File(currentPhotoPath);
//                    mECGImage.setImageURI(Uri.fromFile(f));
//                    Log.d("tag", "Absolute url of image is" + Uri.fromFile(f));
//
//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri contentUri = Uri.fromFile(f);
//                    mediaScanIntent.setData(contentUri);
//                    this.sendBroadcast(mediaScanIntent);
//                }
//            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp +"_";
////        File storagDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
//
//        currentPhotoPath =image.getAbsolutePath();
//        return image;
//    }

//    private void dispatchTakePictureIntent(){
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
//            File photofile = null;
//            try{
//                photofile = createImageFile();
//            }catch (IOException e){
//
//            }
//            if(photofile!=null){
//                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photofile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//            }
//        }
//    }

    public void uploadImages(){
        try{
            if(!mpatientID.getText().toString().isEmpty() && imageLocationPath1 !=null){
                String nameOfImage = mpatientID.getText().toString()+"."+getExtension(imageLocationPath1);
                StorageReference imageRef = objectStorageReference1.child(nameOfImage);

                UploadTask objectUploadTask = imageRef.putFile(imageLocationPath1);
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
                            fStore1.collection("ecg_images").document(mpatientID.getText().toString()).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Uploadimages.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Uploadimages.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(Uploadimages.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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