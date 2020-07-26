package com.example.datascrapper.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datascrapper.R;

public class myUserHolder extends RecyclerView.ViewHolder {

    public TextView list_email,list_full_name;
    public myUserHolder(@NonNull View itemView) {
        super(itemView);
        list_email = itemView.findViewById(R.id.list_email);
        list_full_name = itemView.findViewById(R.id.list_full_name);

    }
}

