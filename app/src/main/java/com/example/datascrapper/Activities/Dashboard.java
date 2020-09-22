package com.example.datascrapper.Activities;

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

import com.example.datascrapper.Auth.Login;
import com.example.datascrapper.Holder.DashboardHolder;
import com.example.datascrapper.Model.DashboardModel;
import com.example.datascrapper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    private FirestoreRecyclerOptions<DashboardModel>options;
    private FirestoreRecyclerAdapter<DashboardModel, DashboardHolder>adapter;
    private RecyclerView dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
//        Log.d("Name",String.valueOf(mAuth.getCurrentUser().getEmail()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query  = firebaseFirestore.collection("users").document(String.valueOf(mAuth.getCurrentUser().getEmail())).collection("projects");
//        Query query  = firebaseFirestore.collection("users").document("dpk@gmail.com").collection("projects");
        dashboard = findViewById(R.id.dashboard);
        dashboard.setHasFixedSize(true);
        dashboard.setLayoutManager(new LinearLayoutManager(Dashboard.this));

        options = new FirestoreRecyclerOptions.Builder<DashboardModel>()
                .setQuery(query,DashboardModel.class)
                .build();
        final ArrayList<String> projectName = new ArrayList<>();
        adapter = new FirestoreRecyclerAdapter<DashboardModel, DashboardHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DashboardHolder holder, int position, @NonNull DashboardModel model) {
                holder.project_name.setText(model.getProject_name());
                holder.project_desc.setText(model.getProject_desc());
                holder.project_name.setTag(model.getProject_name());
                projectName.add(model.getProject_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String project = v.getTag().toString();
                        int pro = holder.getAdapterPosition();
//                      Log.d("Position", String.valueOf(pro));
//                      Toast.makeText(Dashboard.this, projectName.get(pro),Toast.LENGTH_SHORT).show();
                        Intent toProject = new Intent(Dashboard.this, ProjectDetails.class);
                        toProject.putExtra("projectName",projectName.get(pro));
                        startActivity(toProject);
                        finish();
                    }
                });
////                int pr = holder.getAdapterPosition();
//                Toast.makeText(Dashboard.this, pr, Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_project,parent,false);
                return new DashboardHolder(view);
            }
        };

        adapter.startListening();
        dashboard.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void logoutNow(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Dashboard.this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public void addProject(View view){
        Intent intent = new Intent(this, AddProject.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}