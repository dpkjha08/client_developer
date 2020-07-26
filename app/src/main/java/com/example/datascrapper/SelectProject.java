package com.example.datascrapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.Auth.SignUp;
import com.example.datascrapper.Holder.myProjectsHolder;
import com.example.datascrapper.Holder.myUserHolder;
import com.example.datascrapper.Model.ProjectsModel;
import com.example.datascrapper.Model.UsersModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

public class SelectProject extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    private FirestoreRecyclerOptions<ProjectsModel> options;
    private FirestoreRecyclerAdapter<ProjectsModel, myProjectsHolder> adapter;
    private RecyclerView select_project;
    String current_user, projectName;
    TextView project_name;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(0xfff);
        setContentView(R.layout.activity_select_project);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getEmail();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("users").document(current_user)
                .collection("projects");

        select_project = findViewById(R.id.select_project);
        select_project.setHasFixedSize(true);
        select_project.setLayoutManager(new LinearLayoutManager(this));

        options = new FirestoreRecyclerOptions.Builder<ProjectsModel>().setQuery(query, ProjectsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProjectsModel, myProjectsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myProjectsHolder holder, int position, @NonNull ProjectsModel model) {
                holder.project_name.setText(model.getProject_name());
                holder.project_desc.setText(model.getProject_desc());
                holder.project_name.setTag(model.getProject_name());
            }

            @NonNull
            @Override
            public myProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //TODO Here
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_project,parent,false);
                return new myProjectsHolder(view);
            }
        };
        adapter.startListening();
        select_project.setAdapter(adapter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    //
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void addProject(View view){
        Intent intent = new Intent(this, AddProject.class);
        startActivity(intent);
        finish();
    }
    public void projectDetails(View view){

        projectName = String.valueOf(view.getTag());
//        project_name = findViewById(R.id.project_name);
//        projectName = project_name.getText().toString().trim();
        Toast.makeText(this,"Project Name"+projectName,Toast.LENGTH_SHORT).show();
    }
}