package com.dam.troc.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.troc.MainActivity;
import com.dam.troc.ProfilActivity;
import com.dam.troc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    TextInputEditText emailUser, password,confirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);



        emailUser = findViewById(R.id.et_signup_email);
        password = findViewById(R.id.et_signup_password);
        confirmPassword = findViewById(R.id.et_signup_password_verification);



        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.et_signup_email).setOnClickListener(this);
        findViewById(R.id.et_signup_password).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);

    }

    private void registerUser(){

        String email = emailUser.getText().toString().trim();
        String Pass = password.getText().toString().trim();
        TextView accueil = findViewById(R.id.btn_signup);

        if (email.isEmpty()){
            emailUser.setError("Email obligatoire!");
            emailUser.requestFocus();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("Adresse email non conforme!!");
            emailUser.requestFocus();
            return;
        } else if (Pass.isEmpty()) {
            password.setError("mot de passe obligatoire");
            password.requestFocus();
            return;
        } else if (password != confirmPassword) {
            confirmPassword.setError("mot de passe non identique");
            confirmPassword.requestFocus();
            return;
        } else if (Pass.length() <6) {
            password.setError("Minimum 6 charactere!");
            password.requestFocus();
            return;
        } else {
            startActivity(new Intent(this, MainActivity.class));
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
            case R.id.btn_signup:
                registerUser();
                break;
        }

    }

    public void backToLogin(View view){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}