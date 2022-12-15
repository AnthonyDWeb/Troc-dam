package com.dam.troc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Message> list;

    public RecyclerAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }


    public void addMessage(Message msg) {

        list.add(0, msg);
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUserMel());
        holder.message.setText(list.get(position).getMessage());
        holder.dateTime.setText(list.get(position).getDateTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, dateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_email);
            message = itemView.findViewById(R.id.user_message);
            dateTime = itemView.findViewById(R.id.user_message_time);
        }
    }
}
