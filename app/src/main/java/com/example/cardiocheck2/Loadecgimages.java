package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Loadecgimages extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<DownModel>  downModelArrayList = new ArrayList<>();
    MyAdapter myAdapter;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadecgimages);

        setUpRV();
        setUpFB();
        dataFromFirebase();

    }

    private void dataFromFirebase(){
        if(downModelArrayList.size()>0)
            downModelArrayList.clear();

        //db=FirebaseFirestore.getInstance();

//        db.collection("ecg_images") CollectionRefernce
//            .get() Task<QuerySnapshot>
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for(DocumentSnapshot documentSnapshot: task.getResult()) {
//
//                            DownModel downModel = new DownModel(documentSnapshot.getString("name"),
//                                    documentSnapshot.getString("link"));
//                            downModelArrayList.add(downModel);
//                        }
//
//                        myAdapter = new MyAdapter(Loadecgimages.this, downModelArrayList);
//                        mRecyclerView.setAdapter(myAdapter);
//                    }
//        }) Task<QuerySnapshot>
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Loadecgimages.this, "ERROR", Toast.LENGTH_SHORT).show();
//                    }
//        }) Task<QuerySnapshot> ;

        db.collection("ecg_images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()) {
                            DownModel downModel = new DownModel(documentSnapshot.getString("name"),
                                    documentSnapshot.getString("url"));
                            downModelArrayList.add(downModel);
                        }

                        myAdapter = new MyAdapter(Loadecgimages.this, downModelArrayList);
                        mRecyclerView.setAdapter(myAdapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Loadecgimages.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpFB(){
        db=FirebaseFirestore.getInstance();
    }

    private void setUpRV(){
        mRecyclerView = findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}