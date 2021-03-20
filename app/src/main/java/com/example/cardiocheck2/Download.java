package com.example.cardiocheck2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Download extends AppCompatActivity {

    FirebaseAuth dfAuth;
    FirebaseFirestore dfStore;
    EditText detid;
    TextView dtvName, dtvAge, dtvGender, dtvContact, dtvBP, dtvSpO2, dtvDisease, dtvComments, dtvHospital;
    Button dtvDownload, dtvECG;
    String patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        detid = findViewById(R.id.dpid);
        dtvName = findViewById(R.id.eName);
        dtvAge = findViewById(R.id.eAge);
        dtvGender = findViewById(R.id.eGender);
        dtvContact = findViewById(R.id.eContact);
        dtvBP = findViewById(R.id.eBP);
        dtvSpO2 = findViewById(R.id.eSpO2);
        dtvDisease = findViewById(R.id.eDisease);
        dtvDownload = findViewById(R.id.downbutton);
        dtvECG = findViewById(R.id.ecgbutton);
        dtvComments = findViewById(R.id.eComments);
        dtvHospital = findViewById(R.id.eHospital);

        dfAuth = FirebaseAuth.getInstance();
        dfStore = FirebaseFirestore.getInstance();

        patientID = detid.getText().toString();

        dtvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloaddetails();


            }
        });

        dtvECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Downloadecg.class));
            }
        });


    }

    public void downloaddetails(){
        try {
            DocumentReference ddocumentReference = dfStore.collection("patients_details").document(detid.getText().toString());
            System.out.println(detid.getText().toString());
            System.out.println("patientID");
            ddocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    dtvName.setText(value.getString("name"));
                    dtvGender.setText(value.getString("gender"));
                    dtvAge.setText(value.getString("age"));
                    dtvContact.setText(value.getString("contact"));
                    dtvBP.setText(value.getString("BP"));
                    dtvSpO2.setText(value.getString("SpO2"));
                    dtvDisease.setText(value.getString("history"));
                    dtvComments.setText(value.getString("comments"));
                    dtvHospital.setText(value.getString("hospital"));

                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}