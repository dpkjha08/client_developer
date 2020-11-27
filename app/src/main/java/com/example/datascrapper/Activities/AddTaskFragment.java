package com.example.datascrapper.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ////////////////////////////////////////////////////

    TextInputLayout taskName,taskDesc;
    TextView date,assignedTo;
    ImageButton calendar,selectUser;
    Button task;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String projectName,projectDescription,memberIdFinalForDb,membersNameFinalForDb,task_Name,task_Description,fixedDate,fixedAssignedTo;
    ArrayList<String> members,membersName;
    TextView project_Name;
    public String task_id,documentTask;
    FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    public long incompleteTaskinUser;
    public long totalTaskInUser;
    public long completeTaskInUser;
    public String projectNameInUser;
    private String pn;
    public String projectDescInUser;
    public String taskIdInUser;
    ArrayList<String>membersInUsers ;
    Date createdAtInUser;

    ////////////////////////////////////////////////////

    public AddTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTaskFragment newInstance(String param1, String param2) {
        AddTaskFragment fragment = new AddTaskFragment();
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
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_add_task, container, false);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        projectName = getArguments().getString("projectName");
        projectDescription = getArguments().getString("projectDescription");
        members = getArguments().getStringArrayList("members");
        Log.e("MEMBERS ", String.valueOf(members));
        membersName = getArguments().getStringArrayList("membersName");
        task_id = getArguments().getString("task_id");

        project_Name = (TextView)view.findViewById(R.id.projectName);
        project_Name.setText(projectName);


        /* ----------------------- Set Date --------------------------*/
        calendar = (ImageButton)view.findViewById(R.id.calendarIcon);
        date = (TextView)view.findViewById(R.id.dates);
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String setDate =  dayOfMonth+"/"+month+"/"+year;
                date.setText(setDate);
            }
        };
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDate();
            }
        });
        /* ----------------------- Set Date End  --------------------------*/

        /* ----------------------- Assign User --------------------------*/
        selectUser = (ImageButton)view.findViewById(R.id.selectUser);
        assignedTo = (TextView)view.findViewById(R.id.assignedTo);
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserFromDialog();
            }
        });
        /* ----------------------- Assign User End --------------------------*/

        /* ---------------------- Add Task ---------------------------------*/
        taskName = (TextInputLayout)view.findViewById(R.id.taskName);
        taskDesc = (TextInputLayout)view.findViewById(R.id.taskDesc);
        task = (Button)view.findViewById(R.id.task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskFinal();
            }
        });

        /* ---------------------- Add Task End ---------------------------------*/


        return view;
    }

    private void selectUserFromDialog() {

        final String[] membersId = members.toArray(new String[members.size()]);
        final String[] membersFullName = membersName.toArray(new String[membersName.size()]);
        final String[] items = new String[members.size()];
        for(int i=0;i<members.size();i++){
            items[i] = membersFullName[i]+" "+"("+membersId[i]+")";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setTitle("Assign task to - ");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(String a:items){
                    if(a.equals(items[i])){
                        assignedTo.setText(a);
                        memberIdFinalForDb = membersId[i];
                        membersNameFinalForDb = membersFullName[i];
                    }
                }
            }

        });

        builder.show();
    }


    private void displayDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addTaskFinal(){
      task_Name = taskName.getEditText().getText().toString();
      task_Description = taskDesc.getEditText().getText().toString();
      fixedDate = date.getText().toString();
      fixedAssignedTo = assignedTo.getText().toString();
      boolean validation = task_Name.isEmpty() || task_Description.isEmpty() || fixedDate.equals("DD/MM/YYYY") || fixedAssignedTo.equals("Assign To");
      if(validation){
          Toast.makeText(getActivity(),"All Fields are required",Toast.LENGTH_LONG).show();
      }
      else{
          addTaskToFirebase();
      }
      return;

    }


    //Adding Task to the Firebase
    private void addTaskToFirebase() {
        documentTask = task_Name+" "+task_Description;
        Log.e("Document Task",documentTask);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("tasks").document(task_id)
                                              .collection("all_task")
                                              .document(documentTask);

        Map<String,Object> addTask = new HashMap<>();
        addTask.put("assigned_to_email",memberIdFinalForDb);
        addTask.put("assigned_to_name",membersNameFinalForDb);
        addTask.put("end_time",fixedDate);
        addTask.put("task_name",task_Name);
        addTask.put("task_description",task_Description);
        addTask.put("status","OPEN");
        addTask.put("task_id",task_id);
        addTask.put("project_name",projectName);
        addTask.put("project_desc",projectDescription);
        documentReference.set(addTask)
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 updateNumberOfTasks();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                         });



    }

    private void updateNumberOfTasks() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("tasks").document(task_id).update("pending_task",FieldValue.increment(1));
        fStore.collection("tasks").document(task_id).update("total_task",FieldValue.increment(1));
        fStore.collection("tasks").document(task_id).update("OPEN",FieldValue.increment(1));

        DocumentReference ref =  fStore.collection("users").document(mAuth.getCurrentUser().getEmail())
                                .collection("projects")
                                .document(projectName);

        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    membersInUsers = (ArrayList<String>)  snapshot.getData().get("members");

                    for(String mem:membersInUsers){
                        fStore.collection("users")
                                .document(mem)
                                .collection("projects")
                                .document(projectName)
                                .collection("project_user")
                                .document(memberIdFinalForDb).update("total_tasks",FieldValue.increment(1));
                        fStore.collection("users")
                                .document(mem)
                                .collection("projects")
                                .document(projectName)
                                .collection("project_user")
                                .document(memberIdFinalForDb).update("tasks_pending",FieldValue.increment(1));
                    }
                    Log.e("MEMBERS", String.valueOf(membersInUsers));
                    Log.e("MEMBER  2",memberIdFinalForDb);
                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });




    }


}