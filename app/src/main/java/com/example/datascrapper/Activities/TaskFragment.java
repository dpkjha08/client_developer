package com.example.datascrapper.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.datascrapper.Holder.SettingsFragmentHolder;
import com.example.datascrapper.Holder.TaskFragmentHolder;
import com.example.datascrapper.Model.SettingsFragmentModel;
import com.example.datascrapper.Model.TaskFragmentModel;
import com.example.datascrapper.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTaskFragment addTaskFragment;
    public FloatingActionButton fab;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    TextView project_Name,project_Description;
    private String pn,description,task_id;
    ArrayList<String> members,membersName;
    private String projectName,taskIdForRecyclerView,projectDescription;

    //RecyclerView
    private RecyclerView allUsers;
    private FirestoreRecyclerOptions<TaskFragmentModel> options;
    TaskFragmentHolder adapter;


    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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
        View  view  =   inflater.inflate(R.layout.fragment_task, container, false);
        projectName = getArguments().getString("projectName");
        taskIdForRecyclerView = getArguments().getString("taskIdForRecyclerView");
        projectDescription = getArguments().getString("projectDescription");
        ////////////////////////////////////////////////////////////////////////////////////////////////
        Log.e("ProjectName",projectName);
        Log.e("Task ID",taskIdForRecyclerView);
        Query query  = firebaseFirestore.collection("tasks")
                        .document(taskIdForRecyclerView).collection("all_task");

        allUsers = (RecyclerView)view.findViewById(R.id.all_task);
        allUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<TaskFragmentModel> options = new FirestoreRecyclerOptions.Builder<TaskFragmentModel>()
                .setQuery(query,TaskFragmentModel.class)
                .build();


        adapter = new TaskFragmentHolder(options);
        allUsers.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////////////////////
        project_Name  =  (TextView)view.findViewById(R.id.projectName);
        DocumentReference docRef  = firebaseFirestore.collection("users")
                                    .document(String.valueOf(mAuth.getCurrentUser().getEmail()))
                                    .collection("projects")
                                    .document(projectName);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    pn = (String) snapshot.getData().get("project_name");
                    description = (String) snapshot.getData().get("project_desc");
                    task_id = (String) snapshot.getData().get("task_id");
                    members = (ArrayList<String>) snapshot.getData().get("members");
                    project_Name.setText(pn);
                    membersName = new ArrayList<>();
                    for(String mem:members){
                        DocumentReference documentReference  = firebaseFirestore.collection("users")
                                                               .document(mem);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.w("Bas u hi", "Listen failed.", error);
                                    return;
                                }
                                if (value != null && value.exists()) {
                                    String fullName = (String) value.getData().get("full_name");
                                    Log.e("Task Fragment", fullName) ;
                                    membersName.add(fullName);
                                    Log.e("TAsk Fragment inner", String.valueOf(membersName));
                                }

                            }
                        });

                        Log.e("TAsk Fragment outer", String.valueOf(membersName));

                    }

//                    project_Description.setText(description);
                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });


        /* --------------Change Fragment--------------- */
        addTaskFragment = new AddTaskFragment();
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTaskFragment addTaskFragment = new AddTaskFragment();
                Bundle args = new Bundle();
                args.putString("projectName",pn);
                args.putString("projectDescription",description);
                args.putStringArrayList("members",members);
                args.putStringArrayList("membersName",membersName);
                args.putString("task_id",task_id);
                addTaskFragment.setArguments(args);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame,addTaskFragment);
                fr.commit();
            }
        });
        /* --------------Change Fragment End--------------- */

//        Query query  = firebaseFirestore.collection("users")
//                .document(String.valueOf(mAuth.getCurrentUser().getEmail()))
//                .collection("projects").document(projectName).collection("project_user");



        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();

    }


}