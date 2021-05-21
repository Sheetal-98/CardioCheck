package com.example.cardiocheck2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Loadecgimages2 extends AppCompatActivity {

    RecyclerView mRecyclerView2;
    ArrayList<DownModel2> downModelArrayList2 = new ArrayList<>();
    MyAdapter2 myAdapter2;

    FirebaseFirestore db2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadecgimages2);

        setUpRV();
        setUpFB();
        dataFromFirebase();



    }

    private void dataFromFirebase(){
        if(downModelArrayList2.size()>0)
            downModelArrayList2.clear();

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

        db2.collection("ecg_images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()) {
                            DownModel2 downModel2 = new DownModel2(documentSnapshot.getString("name"),
                                    documentSnapshot.getString("url"));
                            downModelArrayList2.add(downModel2);
                        }

                        myAdapter2 = new MyAdapter2(Loadecgimages2.this, downModelArrayList2);
                        mRecyclerView2.setAdapter(myAdapter2);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Loadecgimages2.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpFB(){
        db2=FirebaseFirestore.getInstance();
    }

    private void setUpRV(){
        mRecyclerView2 = findViewById(R.id.recycle2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
    }
}