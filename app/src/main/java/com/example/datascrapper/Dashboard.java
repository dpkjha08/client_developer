package com.example.datascrapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.datascrapper.Holder.myUserHolder;
import com.example.datascrapper.Model.UsersModel;
import com.example.datascrapper.Auth.Login;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Dashboard extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    Button logout;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerOptions<UsersModel>options;
    private FirestoreRecyclerAdapter<UsersModel, myUserHolder>adapter;
    private RecyclerView firestore_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(0xfff);
        setContentView(R.layout.activity_dashboard);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("users");
        firestore_list = findViewById(R.id.firestore_list);
        firestore_list.setHasFixedSize(true);
        firestore_list.setLayoutManager(new LinearLayoutManager(this));

        options = new FirestoreRecyclerOptions.Builder<UsersModel>()
                .setQuery(query,UsersModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<UsersModel, myUserHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myUserHolder holder, int position, @NonNull UsersModel model) {
                    holder.list_email.setText(model.getEmail());
                    holder.list_full_name.setText(model.getFull_name());
            }

            @NonNull
            @Override
            public myUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new myUserHolder(view);
            }
        };
        adapter.startListening();
        firestore_list.setAdapter(adapter);

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void logoutNow(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
