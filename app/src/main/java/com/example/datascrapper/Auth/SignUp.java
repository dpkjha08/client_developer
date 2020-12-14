package com.example.datascrapper.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorJoiner;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.Activities.Constants;
import com.example.datascrapper.Activities.Dashboard;
import com.example.datascrapper.Activities.DatabaseHelper;
import com.example.datascrapper.Activities.FetchAddressIntentService;
import com.example.datascrapper.Activities.MainActivity;
import com.example.datascrapper.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button callLogin;
    ImageButton gpsbutton;
    TextInputLayout full_name, email, password, confirm_password, get_location;
    TextInputEditText inner_gps;
    String fullName, emailAddress, finalPassword, confrimPassword,getLocation;
    TextView login_to_continue;
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    List<String> projects = new ArrayList<>();
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseHelper databaseHelper;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        setContentView(R.layout.activity_sign_up);

//        resultReceiver = new AddressResultReceiver(new Handler());
        Log.e("DATABASE","inSignUpActivity");
        databaseHelper =  new DatabaseHelper(this);

        callLogin = findViewById(R.id.login_here);
        gpsbutton = findViewById(R.id.gps_button);
        inner_gps = findViewById(R.id.inner_gps);
        login_to_continue = findViewById(R.id.login_to_continue);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        gpsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    geoLocation();
                } else {
                    ActivityCompat.requestPermissions(
                            SignUp.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private void geoLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null){
                    try{
                        Geocoder geocoder = new Geocoder(SignUp.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        inner_gps.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        get_location = findViewById(R.id.get_location);


        mAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = full_name.getEditText().getText().toString().trim();
        emailAddress = email.getEditText().getText().toString().trim();
        finalPassword = password.getEditText().getText().toString().trim();
        confrimPassword = confirm_password.getEditText().getText().toString().trim();
        getLocation = get_location.getEditText().getText().toString().trim();

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

                        boolean isInserted = databaseHelper.insertData(emailAddress,fullName,getLocation);
                        if(isInserted == true)
                            Toast.makeText(SignUp.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignUp.this,"Data not Inserted",Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(SignUp.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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


