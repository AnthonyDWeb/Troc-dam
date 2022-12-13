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

import java.util.List;

public class ProfileAdapter  extends FirestoreRecyclerAdapter<ProfileModel, ProfileAdapter.ProfileViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * {@link FirestoreRecyclerOptions}
     * @param options
     */
    public ProfileAdapter(@NonNull FirestoreRecyclerOptions<ProfileModel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull ProfileModel model) {
        List<String> skills = model.getSkills();
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card_skill,parent,false);
        return new ProfileViewHolder(view);
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_profile_card_skill;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_profile_card_skill = itemView.findViewById(R.id.tv_profile_card_skill);
        }
    }
}
