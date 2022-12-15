package com.dam.troc;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ChatActivity extends AppCompatActivity {
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Message> list;
    TextInputLayout message;
    FloatingActionButton send;
    DatabaseReference db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uId;
    String useremail;
    String dateTime;
    String professionId; //Transmise par activity recherche.

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        send = findViewById(R.id.fabsend);
        message = findViewById(R.id.mymessage);
        recyclerView = findViewById(R.id.recyclerview);
        db = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        uId = user.toString().valueOf(mAuth.getCurrentUser());
        list = new ArrayList<>();


        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                useremail = user.getEmail();
                String dateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateTime = new SimpleDateFormat("dd-MM-YY_HH:mm:ss").format(Calendar.getInstance().getTime());
                }
                String msg = message.getEditText().getText().toString();
                db.child("Messages").push().setValue(new Message(useremail, uId, professionId, msg, dateTime)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        message.getEditText().setText("");


                    }
                });
            }


        });

        adapter = new RecyclerAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recieveMessage();
    }

    private void recieveMessage() {

        db.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {

                    Message message = snap.getValue(Message.class);
                    //list.add(message);
                    //adapter.notifyDataSetChanged();
                    adapter.addMessage(message);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String e = error.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_LONG).show();

            }
        });

    }

}