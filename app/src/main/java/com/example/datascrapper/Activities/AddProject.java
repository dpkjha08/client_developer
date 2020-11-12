package com.example.datascrapper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.datascrapper.Auth.SignUp;
import com.example.datascrapper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProject extends AppCompatActivity {

    TextInputLayout project_name, project_desc;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID,email,projectName,projectDesc;
    private boolean doubleBackToExitPressedOnce = false;

    List<String> members = new ArrayList<String>();
    @ServerTimestamp Date time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
    }

    public void addProject(View view){
        project_name = findViewById(R.id.project_name);
        project_desc = findViewById(R.id.project_desc);

        projectName = project_name.getEditText().getText().toString().trim();
        projectDesc = project_desc.getEditText().getText().toString().trim();

        mAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        email  = mAuth.getCurrentUser().getEmail();
        Log.e("userId","ID is : " + userID);
        Log.e("Email","ID is : " + email);
        Log.e("Project Name","ID is : " + projectName);

        DocumentReference documentReference = fStore.collection("users").document(email)
                .collection("projects").document(projectName);
        Map<String,Object> project = new HashMap<>();
        project.put("project_name",projectName);
        project.put("project_desc",projectDesc);
        project.put("created_at", FieldValue.serverTimestamp());
        members.add(email);
        project.put("members",members);
        documentReference.set(project)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ////////////////////////////////////////////
                        DocumentReference doc = fStore.collection("users").document(email)
                                .collection("projects").document(projectName).collection("project_user").document(email);
                        Map<String,Object> project_user = new HashMap<>();
                        project_user.put("full name","Deepak Jha");
                        project_user.put("email",email);
                        project_user.put("tasks completed",0);
                        project_user.put("tasks pending",0);
                        project_user.put("total tasks",0);
                        doc.set(project_user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Intent intent = new Intent(AddProject.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                        Log.e("Project", "Added Successful");
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddProject.this,"Something went wrong while adding user",Toast.LENGTH_SHORT).show();
                                    Log.e("Project", "Adding Failed");
                                }
                            });


                        ////////////////////////////////////////////

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProject.this,"Something went wrong outside.",Toast.LENGTH_SHORT).show();
                        Log.e("Project", "Adding Failed");
                    }
                });

        Log.e("Document","id is "+documentReference);

    }

    // Toast -> press back button again to exit
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

}