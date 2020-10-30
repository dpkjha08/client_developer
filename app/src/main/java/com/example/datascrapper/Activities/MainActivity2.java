package com.example.datascrapper.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private TaskFragment taskFragment;
    private  SettingsFragment settingsFragment;
    public Bundle extras;
    public Bundle bundle;
    public String projectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav  = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        taskFragment = new TaskFragment();
        settingsFragment = new SettingsFragment();

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Log.e("Inside saved Home","Welcome brodas");
                        extras = getIntent().getExtras();
                        projectName = extras.getString("projectName");
                        bundle = new Bundle();
                        bundle.putString("projectName",projectName);
                        homeFragment.setArguments(bundle);
                        setFragment(homeFragment);
                        return true;

                    case R.id.nav_task:
                        Log.e("Inside saved Task","Welcome brodas");
                        setFragment(taskFragment);
                        return true;

                    case R.id.nav_settings:
                        Log.e("Inside saved Settings","Welcome brodas");
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        extras = getIntent().getExtras();
                        projectName = extras.getString("projectName");
                        bundle = new Bundle();
                        bundle.putString("projectName",projectName);
                        settingsFragment.setArguments(bundle);
                        setFragment(settingsFragment);
                        return true;
                    default:
                        return false;

                }

            }

        });

        if (savedInstanceState == null) {
            Log.e("Inside saved Instance","Welcome brodas");
            mMainNav.setSelectedItemId(R.id.nav_home); // change to whichever id should be default
        }

    }

    private void setFragment(Fragment fragment) {
        Log.w("Set Fragment","Something");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

}