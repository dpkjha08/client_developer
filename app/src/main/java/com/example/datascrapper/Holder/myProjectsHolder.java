package com.example.datascrapper.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datascrapper.R;

public class myProjectsHolder extends RecyclerView.ViewHolder {

    public TextView project_name,project_desc;
    public myProjectsHolder(@NonNull View itemView){
        super(itemView);
        project_name = itemView.findViewById(R.id.project_name);
        project_desc = itemView.findViewById(R.id.project_desc);
        itemView.setTag(project_name);
    }

}
