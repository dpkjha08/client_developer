package com.example.datascrapper.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ProjectDetails extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    TextView project_Name,project_Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String projectName = extras.getString("projectName");
            mAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference docRef  = firebaseFirestore.collection("users").document(String.valueOf(mAuth.getCurrentUser().getEmail())).collection("projects").document("projectName");

            project_Name  = findViewById(R.id.projectName);
            project_Description  =  findViewById(R.id.projectDescription);
            project_Name.setText("PROJECT NAME");
            project_Description.setText("Project Description");

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("Bas u hi", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("Bas u hi", "Current data: " + snapshot.getData());
                    } else {
                        Log.d("Bas u hi", "Current data: null");
                    }
                }
            });



        }
        else{
            Toast.makeText(ProjectDetails.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }


    }
}