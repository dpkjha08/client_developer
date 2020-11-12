package com.example.datascrapper.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datascrapper.Auth.Login;
import com.example.datascrapper.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView name;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    TextView project_Name,project_Description;
    private boolean doubleBackToExitPressedOnce = false;
    private String description = null;
    private String pn;
    MaterialCardView teamStrength,taskCompleted;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;



        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        String projectName = getArguments().getString("projectName");
        project_Name  =  (TextView)view.findViewById(R.id.projectName);
        project_Description  =   (TextView)view.findViewById(R.id.projectDescription);
//        teamStrength = (MaterialCardView)view.findViewById(R.id.teamStrength);
//        teamStrength.set
//        teamStrength.setWidth((width*30)/100);
//        taskCompleted = (MaterialCardView)view.findViewById(R.id.taskCompleted);
//        teamStrength.setWidth((width*60)/100);

        DocumentReference docRef  = firebaseFirestore.collection("users").document(String.valueOf(mAuth.getCurrentUser().getEmail())).
                                    collection("projects").document(projectName);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.e("Bas u hi", "Current data: " + snapshot.getData());
                    pn = (String) snapshot.getData().get("project_name");
                    Log.e("Bas u hi", "Project Name: " + snapshot.getData().get("project_desc"));
                    description = (String) snapshot.getData().get("project_desc");
                    Log.e("Bas u hi", "Project Desciption: " + snapshot.getData().get("members"));
                    ArrayList<String> members = (ArrayList<String>) snapshot.getData().get("members");
//                    System.out.println(members);

                    project_Name.setText(pn);
                    project_Description.setText(description);
                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });

        ImageView img = (ImageView) view.findViewById(R.id.logoutImage);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }


}