package com.dam.troc.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.troc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EditSkillAdapter extends FirestoreRecyclerAdapter<EditSkillModel, EditSkillAdapter.EditSkillHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EditSkillAdapter(@NonNull FirestoreRecyclerOptions<EditSkillModel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull EditSkillHolder holder, int position, @NonNull EditSkillModel model) {
        String skillname = model.getSkillname();
        holder.tv_skillname.setText(skillname);
    }

    @NonNull
    @Override
    public EditSkillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_card_skill, parent, false);
        return new EditSkillHolder(view);
    }

    public class EditSkillHolder extends RecyclerView.ViewHolder {
        TextView tv_skillname;
        public EditSkillHolder(@NonNull View itemView) {
            super(itemView);
            tv_skillname = itemView.findViewById(R.id.tv_card_skill);
        }
    }
}
