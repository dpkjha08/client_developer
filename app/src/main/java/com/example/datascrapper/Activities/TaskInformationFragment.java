package com.example.datascrapper.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datascrapper.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskInformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String taskNameAndDesc,projectNameAndDesc,onlyProjectName,memberIdFinalForDb,membersNameFinalForDb;
    TextView final_status,final_date,final_assigned,projectName,task_name,task_desc;
    LinearLayout status_layout,assigned_to_layout,end_date_layout;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    ArrayList<String> membersId,membersName;
    Button updateTask,resetTask,deleteTask;
    private String taskIdForRecyclerView,projectDescription,projectNameFull;
    public Bundle bundle;
    public String previousStatus;
    public TaskInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskInformationFragment newInstance(String param1, String param2) {
        TaskInformationFragment fragment = new TaskInformationFragment();
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
        View view =  inflater.inflate(R.layout.fragment_task_information, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        taskNameAndDesc = getArguments().getString("taskNameAndDesc");
        projectNameAndDesc = getArguments().getString("projectNameAndDesc");
        onlyProjectName = getArguments().getString("onlyProjectName");
        projectName = (TextView)view.findViewById(R.id.projectName);
        final_status = (TextView)view.findViewById(R.id.final_status);
        final_date = (TextView)view.findViewById(R.id.final_date);
        final_assigned = (TextView)view.findViewById(R.id.final_assigned);
        task_name = (TextView)view.findViewById(R.id.task_name);
        task_desc = (TextView)view.findViewById(R.id.task_desc);
        status_layout = (LinearLayout)view.findViewById(R.id.status_layout);
        assigned_to_layout = (LinearLayout)view.findViewById(R.id.assigned_to_layout);
        end_date_layout = (LinearLayout)view.findViewById(R.id.end_date_layout);
        updateTask = (Button)view.findViewById(R.id.updateTask);
        resetTask = (Button)view.findViewById(R.id.resetTask);
        deleteTask = (Button)view.findViewById(R.id.deleteTask);



        projectName.setText(onlyProjectName);
        DocumentReference doc = fStore.collection("tasks")
                                        .document(projectNameAndDesc)
                                        .collection("all_task")
                                        .document(taskNameAndDesc);

        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TASK_INFORMATION","Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String assigned_to_email = (String) snapshot.getData().get("assigned_to_email");
                    String assigned_to_name = (String) snapshot.getData().get("assigned_to_name");
                    String end_time = (String) snapshot.getData().get("end_time");
                    String status = (String) snapshot.getData().get("status");
                    previousStatus =  (String) snapshot.getData().get("status");
                    String task__name = (String) snapshot.getData().get("task_name");
                    String task__description = (String) snapshot.getData().get("task_description");
                    taskIdForRecyclerView = (String) snapshot.getData().get("task_id");
                    projectDescription =(String) snapshot.getData().get("project_desc");
                    projectNameFull =(String) snapshot.getData().get("project_name");
                    task_name.setText(task__name);
                    task_desc.setText(task__description);
                    final_assigned.setText(assigned_to_name+" ("+assigned_to_email+")");
                    memberIdFinalForDb = assigned_to_email;
                    membersNameFinalForDb = assigned_to_name;
                    final_date.setText(end_time);
                    final_status.setText(status);

                } else {
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });

        /* ----------------------- Set Date --------------------------*/

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String setDate =  dayOfMonth+"/"+month+"/"+year;
                final_date.setText(setDate);
                final_date.setTextColor(Color.parseColor("#f70505"));
            }
        };
        end_date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDate();
            }
        });
        /* ----------------------- Set Date End  --------------------------*/

        /* ----------------------- Assign User --------------------------*/

        assigned_to_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserFromDialog();
            }
        });
        /* ----------------------- Assign User End --------------------------*/


        /* ----------------------- Assign STATUS --------------------------*/

        status_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStatusFromDialog();
            }
        });
        /* ----------------------- Assign STATUS End --------------------------*/


        DocumentReference docRef  = fStore.collection("users")
                .document(String.valueOf(mAuth.getCurrentUser().getEmail()))
                .collection("projects")
                .document(onlyProjectName);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("Bas u hi", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    membersId = (ArrayList<String>) snapshot.getData().get("members");
                    membersName = new ArrayList<>();
                    for(String mem:membersId) {
                        DocumentReference documentReference = fStore.collection("users")
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
                                    membersName.add(fullName);
                                }
                                else{
                                    Log.d("Bas u hi", "Current data: null");
                                }
                            }
                        });
                    }
                }
                else{
                    Log.d("Bas u hi", "Current data: null");
                }
            }
        });



        updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = fStore.collection("tasks")
                        .document(projectNameAndDesc)
                        .collection("all_task")
                        .document(taskNameAndDesc);
                ref.update("end_time",final_date.getText().toString());
                ref.update("status",final_status.getText().toString());
                ref.update("assigned_to_email",memberIdFinalForDb);
                ref.update("assigned_to_name",membersNameFinalForDb);
                switch(final_status.getText().toString()){
                    case "RESOLVED":
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("pending_task",FieldValue.increment(-1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("complete_task",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("RESOLVED",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update(previousStatus,FieldValue.increment(-1));

                        break;
                    case "REOPENED":
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("pending_task",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("complete_task",FieldValue.increment(-1));

                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("REOPENED",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update(previousStatus,FieldValue.increment(-1));

                        break;
                    case "CLOSED":
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("pending_task",FieldValue.increment(-1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("complete_task",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("CLOSED",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update(previousStatus,FieldValue.increment(-1));
                        break;
                    case "IN-PROGRESS":
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update("IN-PROGRESS",FieldValue.increment(1));
                        fStore.collection("tasks")
                                .document(projectNameAndDesc)
                                .update(previousStatus,FieldValue.increment(-1));

                    default:
                        break;

                }

                Toast.makeText(getContext(),"Task Updated",Toast.LENGTH_LONG).show();
                TaskFragment taskFragment = new TaskFragment();
                bundle = new Bundle();
                bundle.putString("projectName",projectNameFull);
                bundle.putString("taskIdForRecyclerView",taskIdForRecyclerView);
                bundle.putString("projectDescription",projectDescription);
                taskFragment.setArguments(bundle);
                FragmentTransaction fr =((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame,taskFragment);
                fr.commit();

            }
        });

       resetTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                       TaskInformationFragment taskFragment = new TaskInformationFragment();
                       bundle = new Bundle();
                       bundle.putString("taskNameAndDesc",taskNameAndDesc);
                       bundle.putString("projectNameAndDesc",projectNameAndDesc);
                       bundle.putString("onlyProjectName",onlyProjectName);
                       taskFragment.setArguments(bundle);
                       FragmentTransaction fr =  getActivity().getSupportFragmentManager().beginTransaction();
                       fr.replace(R.id.main_frame,taskFragment);
                       fr.commit();

               /*--------------------------------------------------------------------------*/

               /*--------------------------------------------------------------------------*/
           }
       });

       deleteTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               fStore.collection("tasks")
                       .document(projectNameAndDesc).update("pending_task", FieldValue.increment(-1));
               fStore.collection("tasks")
                       .document(projectNameAndDesc).update("total_task", FieldValue.increment(-1));
               fStore.collection("tasks").document(projectNameAndDesc)
                       .collection("all_task")
                       .document(taskNameAndDesc).delete();
               Toast.makeText(getContext(),"Task Deleted Successfully",Toast.LENGTH_LONG).show();
               TaskFragment taskFragment = new TaskFragment();
               bundle = new Bundle();
               bundle.putString("projectName",projectNameFull);
               bundle.putString("taskIdForRecyclerView",taskIdForRecyclerView);
               bundle.putString("projectDescription",projectDescription);
               taskFragment.setArguments(bundle);
               FragmentTransaction fr =((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
               fr.replace(R.id.main_frame,taskFragment);
               fr.commit();


           }
       });
        return view;
    }

    private void selectStatusFromDialog() {

        final String[] selectStatus = {"IN-PROGRESS","RESOLVED","REOPENED","CLOSED"};
        final String[] items = new String[selectStatus.length];
        for(int i=0;i<selectStatus.length;i++){
            items[i] = selectStatus[i];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setTitle("Assign task to - ");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(String a:items){
                    if(a.equals(items[i])){
                        final_status.setText(a);
                        final_status.setTextColor(Color.parseColor("#f70505"));
                    }
                }
            }

        });
        builder.show();

    }

    private void selectUserFromDialog() {
        Log.e("membersName outter",String.valueOf(membersName));
        Log.e("membersName outter",String.valueOf(membersId));
        final String[] membersIdFinal = membersId.toArray(new String[membersId.size()]);
        final String[] membersFullName = membersName.toArray(new String[membersName.size()]);
        final String[] items = new String[membersId.size()];
        for(int i=0;i<membersId.size();i++){
            items[i] = membersFullName[i]+" "+"("+membersIdFinal[i]+")";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setTitle("Assign task to - ");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(String a:items){
                    if(a.equals(items[i])){
                        final_assigned.setText(a);
                        final_assigned.setTextColor(Color.parseColor("#f70505"));
                        memberIdFinalForDb = membersIdFinal[i];
                        membersNameFinalForDb = membersFullName[i];
                    }
                }
            }

        });
        builder.show();
        /*-----------------------------------------------------*/

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


}