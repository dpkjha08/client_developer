package com.example.datascrapper.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.controls.actions.FloatAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.Auth.Login;
import com.example.datascrapper.Holder.DashboardHolder;
import com.example.datascrapper.Holder.SettingsFragmentHolder;
import com.example.datascrapper.Model.DashboardModel;
import com.example.datascrapper.Model.SettingsFragmentModel;
import com.example.datascrapper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mancj.slideup.SlideUp;
import com.example.datascrapper.Holder.SettingsFragmentHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private TextView name;
    private FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    TextView project_Name, project_Description;
    private boolean doubleBackToExitPressedOnce = false;
    private String pn;
    private String projectName;
    public String projectDesc;
    public String projectX;
    public String taskId;
    long incomleteTask,completedTask,totalTask;
    public String tempx,tempy;
    List<String> members = new ArrayList<String>();
    List<String> oldMembers = new ArrayList<String>();

    //SLIDE UP//
    private SlideUp slideUp;
    private View dim;
    private View slideView;
    private FloatingActionButton fab;
    ///////////////////////
    // ADD USER //
    TextInputLayout email;
    CharSequence emailAddress;
    Button addUserEmail;
    private String fullName;
    private String emailAd;

    //////////////////////
    //Recycler View
    private RecyclerView showUsers;
    private FirestoreRecyclerOptions<SettingsFragmentModel> options;
    SettingsFragmentHolder adapter;
    /////////////////////
    private ProgressDialog progress;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        progress=new ProgressDialog(getContext());
        progress.setMessage("Loading User Fragment...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.show();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        projectName = getArguments().getString("projectName");
        Query query  = fStore.collection("users")
                .document(String.valueOf(mAuth.getCurrentUser().getEmail()))
                .collection("projects").document(projectName).collection("project_user");

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        showUsers = (RecyclerView)view.findViewById(R.id.showUsers);
        showUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<SettingsFragmentModel> options = new FirestoreRecyclerOptions.Builder<SettingsFragmentModel>()
                                                                .setQuery(query,SettingsFragmentModel.class)
                                                                .build();


        adapter = new SettingsFragmentHolder(options);
        showUsers.setAdapter(adapter);
        view = addUserFirst(view);
        progress.dismiss();
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


    private View addUserFirst(View view) {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        projectName = getArguments().getString("projectName");
        project_Name = (TextView) view.findViewById(R.id.projectName);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////




        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DocumentReference docRef = fStore.collection("users").document(String.valueOf(mAuth.getCurrentUser().getEmail())).
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
                    project_Name.setText(pn);

                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });


        ////////////////////////////////////////////////////////////////////////////

        slideView = view.findViewById(R.id.slideView);
        dim = view.findViewById(R.id.dim);

        slideUp = new SlideUp(slideView);
        slideUp.hideImmediately();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.animateIn();
                fab.hide();
            }
        });

        slideUp.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim.setAlpha(1 - (v / 100));
            }

            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    fab.show();
                }

            }
        });

        /////////////////////////////////////////////////////////////////////////
        ImageView img = (ImageView) view.findViewById(R.id.logoutImage);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ///////////////////////////////////////////////////////////////////////

        addUserEmail = (Button) view.findViewById(R.id.add_user_go);
        addUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(v);
            }
        });

        return view;

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

    private void addUser(View view){
        email =(TextInputLayout) getActivity().findViewById(R.id.addEmail);
        emailAddress = email.getEditText().getText().toString().trim();
        if(!validateEmail() )
        {
            return;
        }
        else
        {
            DocumentReference docRef  = fStore.collection("users").document((String) emailAddress);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("SettingsFramgment : ", "Exception while checking user exists", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        addUserInDatabase(emailAddress);
                    } else {
                        Toast.makeText(getActivity(),"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }
     }


    private void addUserInDatabase(final CharSequence emailAddress) {
        email =(TextInputLayout) getActivity().findViewById(R.id.addEmail);
        final String currentUser = mAuth.getCurrentUser().getEmail().toString();
        fStore.getInstance().collection("users").document(currentUser).collection("projects").document(projectName).update("members", FieldValue.arrayUnion(emailAddress));
        /////////////////////////////////////////////////////////
        DocumentReference doc = fStore.collection("users").document(currentUser).
                collection("projects").document(projectName);

        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    projectX =  snapshot.getData().get("project_name").toString().trim();
                    projectDesc = snapshot.getData().get("project_desc").toString().trim();
                    oldMembers = (ArrayList<String>)  snapshot.getData().get("members");
                    taskId = snapshot.getData().get("task_id").toString();
                    incomleteTask = (long)snapshot.getData().get("task_incomplete");
                    completedTask = (long)snapshot.getData().get("task_completed");
                    totalTask = (long)snapshot.getData().get("total_task");

                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });

        ////////////////////////////////
        DocumentReference documentReference = fStore.collection("users").document((String) emailAddress).collection("projects").document(projectName);
        Map<String,Object> project = new HashMap<>();
        Object pro = projectX;
        project.put("project_name",null);
        project.put("project_desc",null);
        project.put("created_at", FieldValue.serverTimestamp());
        project.put("members",members);
        project.put("task_incomplete",null);
        project.put("task_completed",null);
        project.put("total_task",null);


        documentReference.set(project)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("project_name", projectX);
                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("project_desc", projectDesc);
                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("task_id", taskId);
                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("task_completed", completedTask);
                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("task_incomplete",incomleteTask );
                        fStore.collection("users").document(String.valueOf(emailAddress)).
                                collection("projects").document(projectName).update("total_task",totalTask );
                        for (String temp : oldMembers) {
                            for(String temp2 : oldMembers){
                                fStore.collection("users").document(temp).
                                        collection("projects").document(projectName).update("members", FieldValue.arrayUnion(temp2));

                            }
                        }
                        putUserData(currentUser);
//                        email.getEditText().clearFocus();
                        email.getEditText().setText("");
                        email.clearFocus();

//                        Toast.makeText(getActivity(),"User Added Successfully 2",Toast.LENGTH_LONG).show();

                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Something went wrong at last",Toast.LENGTH_SHORT).show();
                        Log.e("Project", "Adding Failed");
                    }
                });


        }

    private void putUserData(String currentUser) {
        DocumentReference doc = fStore.collection("users").document(currentUser).
                collection("projects").document(projectName);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    oldMembers = (ArrayList<String>)  snapshot.getData().get("members");

                    for(final String tempx : oldMembers){
                        for(final String tempy : oldMembers){

                            DocumentReference docForUser = fStore.collection("users").document(tempy);
                            docForUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w("Bas u hi", "Listen failed.", e);
                                        return;
                                    }

                                    if (snapshot != null && snapshot.exists()) {



                                        fullName =  snapshot.getData().get("full_name").toString().trim();
                                        emailAd  = snapshot.getData().get("email").toString().trim();
                                        DocumentReference doc = fStore.collection("users").document(tempx)
                                                .collection("projects").document(projectName).
                                                        collection("project_user").document(tempy);

                                        Map<String,Object> project_user = new HashMap<>();

                                        project_user.put("full_name",null);
                                        project_user.put("email",null);
                                        project_user.put("tasks_completed",0);
                                        project_user.put("tasks_pending",0);
                                        project_user.put("total_tasks",0);

                                        doc.set(project_user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.e("Added User","Successful");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(),"Failed Failed Failed",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                        Log.e("First Name-Email 2",fullName+" "+emailAd);
                                        fStore.collection("users").document(tempx)
                                                .collection("projects")
                                                .document(projectName)
                                                .collection("project_user")
                                                .document(tempy)
                                                .update("full_name",fullName);

                                        fStore.collection("users")
                                                .document(tempx)
                                                .collection("projects")
                                                .document(projectName)
                                                .collection("project_user")
                                                .document(tempy)
                                                .update("email",emailAd);

                                    } else {
                                        Log.d("Bas u hi", "Current data: null");
                                    }
                                }
                            });
                        }
                    }
                    Toast.makeText(getActivity(),"User Added Successfully",Toast.LENGTH_LONG).show();

                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });

    }

} //Class