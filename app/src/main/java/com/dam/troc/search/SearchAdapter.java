package com.dam.troc.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dam.troc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

public class SearchAdapter extends FirestoreRecyclerAdapter<UserSearchModel,SearchAdapter.SearchViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SearchAdapter(@NonNull FirestoreRecyclerOptions<UserSearchModel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull UserSearchModel model) {
        String id = model.getId();
        String userImage = model.getUserImage();
        String username = model.getName();
        String skill = model.getSkill();

        holder.tv_card_username.setText(username);
        holder.tv_card_skill.setText("skill");

        Log.i("TAG", "onBindViewHolder id: " + id);

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher_round);
        Context context = holder.iv_card_userImage.getContext();
        Glide.with(context).load(userImage).apply(options).fitCenter().override(150,150).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_card_userImage);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new SearchViewHolder(view);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_card_userImage;
        private TextView tv_card_username,tv_card_skill;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_card_userImage = itemView.findViewById(R.id.iv_card_userProfilPhoto);
            tv_card_username = itemView.findViewById(R.id.tv_card_username);
            tv_card_skill = itemView.findViewById(R.id.tv_card_userSkill);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        DocumentSnapshot userDocument = getSnapshots().getSnapshot(pos);
                        Log.i("TAG", "onClick: " + userDocument);
                    }
                }
            });

        }
    }

}
