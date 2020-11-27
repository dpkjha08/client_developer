package com.example.datascrapper.Holder;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datascrapper.Activities.TaskInformationFragment;
import com.example.datascrapper.Model.TaskFragmentModel;
import com.example.datascrapper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class TaskFragmentHolder extends FirestoreRecyclerAdapter<TaskFragmentModel,TaskFragmentHolder.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    final ArrayList<String> projectNameAndDesc = new ArrayList<>();
    final ArrayList<String> taskNameAndDesc = new ArrayList<>();
    final ArrayList<String> onlyProjectName = new ArrayList<>();
    public TaskFragmentHolder(@NonNull FirestoreRecyclerOptions<TaskFragmentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final TaskFragmentModel model) {
        holder.task_name.setText(model.getTask_name());
        holder.end_date.setText(model.getEnd_time());
        holder.assignedToName.setText(model.getAssigned_to_name());
        holder.status.setText(model.getStatus());
        switch(model.getStatus()){
            case "IN-PROGRESS":
                holder.chip.setBackgroundColor(Color.parseColor("#f5bdfc"));
                holder.status.setTextColor(Color.parseColor("#db05f7"));
                break;
            case "RESOLVED":
                holder.chip.setBackgroundColor(Color.parseColor("#e5fcbd"));
                holder.status.setTextColor(Color.parseColor("#73b304"));
                break;
            case "REOPENED":
                holder.chip.setBackgroundColor(Color.parseColor("#bef7f1"));
                holder.status.setTextColor(Color.parseColor("#04bfac"));
                break;
            case "OPEN":
                holder.chip.setBackgroundColor(Color.parseColor("#7ea3f2"));
                holder.status.setTextColor(Color.parseColor("#10388f"));
                break;
            case "CLOSED":
                holder.chip.setBackgroundColor(Color.parseColor("#c1f2b8"));
                holder.status.setTextColor(Color.parseColor("#21b007"));
                break;
            default:
                holder.chip.setBackgroundColor(Color.parseColor("#000000"));
                holder.status.setTextColor(Color.parseColor("#fcfcf7"));
                break;

        }
        taskNameAndDesc.add(model.getTask_name()+" "+model.getTask_description());
        projectNameAndDesc.add(model.getTask_id());
        onlyProjectName.add(model.getProject_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pro = holder.getAdapterPosition();
                Log.e("Position", String.valueOf(pro));
                TaskInformationFragment taskInformation = new TaskInformationFragment();
                Bundle args = new Bundle();
                args.putString("taskNameAndDesc",taskNameAndDesc.get(pro));
                args.putString("projectNameAndDesc",projectNameAndDesc.get(pro));
                args.putString("onlyProjectName",onlyProjectName.get(pro));
                taskInformation.setArguments(args);
                FragmentTransaction fr =((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame,taskInformation);
                fr.commit();
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task,parent,false);
        return new myviewholder(view);

    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView task_name,end_date,assignedToName,status;
        MaterialCardView chip;
        public myviewholder(@NonNull View itemView){
            super(itemView);
            task_name = itemView.findViewById(R.id.task_name);
            end_date = itemView.findViewById(R.id.end_date);
            assignedToName = itemView.findViewById(R.id.assignedToName);
            status = itemView.findViewById(R.id.status);
            chip = itemView.findViewById(R.id.chip);

        }
    }

}
