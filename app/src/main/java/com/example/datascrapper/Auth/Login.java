package com.example.datascrapper.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.Dashboard;
import com.example.datascrapper.R;
import com.example.datascrapper.SelectProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button call_signup,go;
    TextView name,slogan,login_welcome,login_to_continue;
    String emailAddress,finalPassword;
    TextInputLayout email,password;
    private boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(0xfff);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.name);
        login_to_continue = findViewById(R.id.login_to_continue);
        email = findViewById(R.id.email);
        password  = findViewById(R.id.password);
        go = findViewById(R.id.go);
        call_signup = findViewById(R.id.signup_here);
        call_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("Inside","Click Successful");
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }

        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNow();
            }
        });
    }

    //If user already logged in
    @Override
    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this, SelectProject.class));
        }
    }


    // Toast -> press back button again to exit
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

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        email.setError(null);
        email.setErrorEnabled(false);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        password.setError(null);
        password.setErrorEnabled(false);
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void loginNow(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        emailAddress = email.getEditText().getText().toString().trim();
        finalPassword = password.getEditText().getText().toString().trim();

        if(!validatePassword() | !validateEmail() )
        {
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(emailAddress,finalPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(Login.this,SelectProject.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}