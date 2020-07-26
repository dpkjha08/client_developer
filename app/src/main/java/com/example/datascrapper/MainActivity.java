package com.example.datascrapper;
//package com.example.datascrapper.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datascrapper.Auth.Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //set splash screen time
    private static int SPLASH_SCREEN = 4000;


    //Variables for Animations
    Animation topAnimation,bottomAnimation;
    ImageView logo;
    TextView name,slogan;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // To remove Status Bar
        setContentView(R.layout.activity_main);

            //Adding Splash Animation or Get animation from the xml files
//        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Get image,text using Id
//        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);

        //function to goto next screen after splashing screen for 5 secs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                mAuth = FirebaseAuth.getInstance();
//                FirebaseAuth.getInstance().signOut();
                if(mAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(MainActivity.this,SelectProject.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);

    }
}