package com.example.datascrapper.Holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datascrapper.Model.SettingsFragmentModel;
import com.example.datascrapper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SettingsFragmentHolder extends FirestoreRecyclerAdapter<SettingsFragmentModel,SettingsFragmentHolder.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SettingsFragmentHolder(@NonNull FirestoreRecyclerOptions<SettingsFragmentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull SettingsFragmentModel model) {
        holder.full_name_card.setText(model.getFull_name());
        holder.email_card.setText(model.getEmail());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user,parent,false);
        return new myviewholder(view);

    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView full_name_card,email_card;

        public myviewholder(@NonNull View itemView){
            super(itemView);
            full_name_card = itemView.findViewById(R.id.full_name_card);
            email_card = itemView.findViewById(R.id.email_card);

        }
    }

}
