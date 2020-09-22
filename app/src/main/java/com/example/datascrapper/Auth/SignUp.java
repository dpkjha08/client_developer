package com.example.datascrapper.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.datascrapper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button callLogin;
    TextInputLayout full_name,email,password,confirm_password;
    String fullName,emailAddress,finalPassword,confrimPassword;
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    List<String> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        setContentView(R.layout.activity_sign_up);

        callLogin = findViewById(R.id.login_here);

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
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

    private Boolean validateName() {
        String val = full_name.getEditText().getText().toString();
        full_name.setError(null);
        full_name.setErrorEnabled(false);
        if (val.isEmpty()) {
            full_name.setError("Field cannot be empty");
            return false;
        }
        else {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            return true;
        }
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
        password.setError(null);
        password.setErrorEnabled(false);
        confirm_password.setError(null);
        confirm_password.setErrorEnabled(false);
        String val = password.getEditText().getText().toString();
        String val2 = confirm_password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else if(!val.equals(val2)) {
            confirm_password.setError("Passwords mismatch");
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view){

        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        mAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = full_name.getEditText().getText().toString().trim();
        emailAddress = email.getEditText().getText().toString().trim();
        finalPassword = password.getEditText().getText().toString().trim();
        confrimPassword = confirm_password.getEditText().getText().toString().trim();

        if(!validateName() | !validatePassword()  | !validateEmail() )
        {
            return;
        }
        else{
            full_name.clearFocus();
            email.clearFocus();
            password.clearFocus();
            confirm_password.clearFocus();
            mAuth.createUserWithEmailAndPassword(emailAddress,confrimPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(emailAddress);
                        Map<String,Object> user = new HashMap<>();
                        user.put("full_name",fullName);
                        user.put("email",emailAddress);
                        user.put("password",confrimPassword);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG","USER REGISTERED SUCCESSFULLY");
                            }
                        });
//                        Intent intent = new Intent(SignUp.this, SelectProject.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
                    }
                    else{
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(SignUp.this,"Email Already Exist",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }



    }

}
