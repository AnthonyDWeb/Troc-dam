package com.dam.troc.auth;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.troc.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotten_Pass extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText editEmail;
    private FirebaseAuth mFirebaseAuth;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_pass);


        mFirebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.fp_email);
        submit= findViewById(R.id.fp_submit);

        findViewById(R.id.fp_email).setOnClickListener(this);
        findViewById(R.id.fp_submit).setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fp_submit:

                submitForm();

                break;

        }
    }


    private void submitForm() {

        String email = editEmail.getText().toString().trim();


        if (email.isEmpty()){
            editEmail.setError("Email obligatoire!");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Adresse email non conforme!!");
            editEmail.requestFocus();
            return;

        }

        mFirebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(Forgotten_Pass.this, "An email has been sent to you.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Forgotten_Pass.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



