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
    TextView project_Name,project_Description,team_count,task_completed,openm,resolvedm,reopenedm,inprogressm,closedm;
    private boolean doubleBackToExitPressedOnce = false;
    private String description = null;
    private String pn,task_id;
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




        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        String projectName = getArguments().getString("projectName");
        project_Name  =  (TextView)view.findViewById(R.id.projectName);
        project_Description  =   (TextView)view.findViewById(R.id.projectDescription);
        team_count = (TextView) view.findViewById(R.id.team_count);
        task_completed = (TextView) view.findViewById(R.id.task_completed);
        openm = (TextView) view.findViewById(R.id.open);
        closedm = (TextView) view.findViewById(R.id.closed);
        resolvedm = (TextView) view.findViewById(R.id.resolved);
        reopenedm = (TextView) view.findViewById(R.id.reopened);
        inprogressm = (TextView) view.findViewById(R.id.in_progress);

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
                    description = (String) snapshot.getData().get("project_desc");
                    task_id = (String) snapshot.getData().get("task_id");
                    ArrayList<String> members = (ArrayList<String>) snapshot.getData().get("members");
                    project_Name.setText(pn);
                    project_Description.setText(description);
                    if((1 <= members.size())&&( members.size()<= 9)){
                        team_count.setText("0"+String.valueOf(members.size()));
                    }
                    else{
                        team_count.setText(String.valueOf(members.size()));
                    }

                    DocumentReference ref = firebaseFirestore.collection("tasks").document(task_id);
                    ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){
                                Log.e("HOME FRAGMENT","HUA KUCH GADBAD");
                                return;
                            }
                            if(value != null && value.exists()){
                                long closed = (long) value.getData().get("CLOSED");
                                long open = (long) value.getData().get("OPEN");
                                long inprogress = (long) value.getData().get("IN-PROGRESS");
                                long resolved = (long) value.getData().get("RESOLVED");
                                long reopened = (long) value.getData().get("REOPENED");
                                long comptask = (long) value.getData().get("total_task");
                                if((0 <= comptask)&&( comptask <= 9)){
                                    task_completed.setText("0"+String.valueOf(comptask));
                                }
                                else{
                                    task_completed.setText(String.valueOf(comptask));
                                }

                                if((0 <= closed)&&( closed <= 9)){
                                    closedm.setText("   CLOSED                                        0"+String.valueOf(closed));
                                }
                                else{
                                    closedm.setText("   CLOSED                                         "+String.valueOf(closed));
                                }

                                if((0 <= open)&&( open <= 9)){
                                    openm.setText("   OPEN                                            0"+String.valueOf(open));
                                }
                                else{
                                    openm.setText("   OPEN                                            "+String.valueOf(open));
                                }
                                if((0 <= inprogress)&&( inprogress <= 9)){
                                    inprogressm.setText("   IN-PROGRESS                            0"+String.valueOf(inprogress));
                                }
                                else{
                                    inprogressm.setText("   IN-PROGRESS                            s"+String.valueOf(inprogress));
                                }
                                if((0 <= resolved)&&( resolved <= 9)){
                                    resolvedm.setText("   RESOLVED                                   0"+String.valueOf(resolved));
                                }
                                else{
                                    resolvedm.setText("   RESOLVED                                    "+String.valueOf(resolved));
                                }
                                if((0 <= reopened)&&( reopened <= 9)){
                                    reopenedm.setText("   REOPENED                                  0"+String.valueOf(reopened));
                                }
                                else{
                                    reopenedm.setText("   REOPENED                                   "+String.valueOf(reopened));
                                }




                            }
                        }
                    });

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
                Intent intent = new Intent(getContext(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }


}