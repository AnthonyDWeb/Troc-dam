package com.dam.troc.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.troc.MainActivity;
import com.dam.troc.ProfilActivity;
import com.dam.troc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    TextView emailUser, tvPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);



        emailUser = findViewById(R.id.sign_email);
        tvPass = findViewById(R.id.sign_pass);



        findViewById(R.id.sign_old_user).setOnClickListener(this);
        findViewById(R.id.sign_email).setOnClickListener(this);
        findViewById(R.id.sign_pass).setOnClickListener(this);
        findViewById(R.id.sign_register).setOnClickListener(this);

    }

    private void registerUser(){

        String email = emailUser.getText().toString().trim();
        String Pass = tvPass.getText().toString().trim();
        TextView accueil = findViewById(R.id.sign_old_user);

        if (email.isEmpty()){
            emailUser.setError("Email obligatoire!");
            emailUser.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("Adresse email non conforme!!");
            emailUser.requestFocus();
            return;

        }

        if (Pass.isEmpty()) {
            tvPass.setError("Password obligatoire");
            tvPass.requestFocus();
            return;

        }

        if (Pass.length() <6) {
            tvPass.setError("Minimum 6 chars!");
            tvPass.requestFocus();
            return;

        }

        mAuth.createUserWithEmailAndPassword(email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Inscription réussie", Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(SignUpActivity.this, ProfilActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Email déjà enregistrée", Toast.LENGTH_LONG).show();
                        accueil.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        accueil.setVisibility(View.VISIBLE);
                    }
                }

            }

        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_register:
                registerUser();
                break;
            case R.id.sign_old_user:
                startActivity(new Intent(this, MainActivity.class));
                break;


        }

    }
}