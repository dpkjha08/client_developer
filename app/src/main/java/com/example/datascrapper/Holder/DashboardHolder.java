package com.example.datascrapper.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datascrapper.R;

public class DashboardHolder extends RecyclerView.ViewHolder {
    public TextView project_name,project_desc;
    public DashboardHolder(@NonNull View itemView){
        super(itemView);
        project_name = itemView.findViewById(R.id.project_name);
        project_desc = itemView.findViewById(R.id.project_desc);
    }
}




