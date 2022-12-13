package com.dam.troc.auth;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.troc.MainActivity;
import com.dam.troc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailUser, passwordUser;
    FirebaseAuth FIREBASE_AUTH_INITIALISATION;

    private boolean checkForm(String email, String Pass) {
        if (email.isEmpty()) {
            emailUser.setError(NEED_EMAIL);
            emailUser.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailUser.setError(INCORRECT_EMAIL);
            emailUser.requestFocus();
            return false;
        }
        if (Pass.isEmpty()) {
            passwordUser.setError(NEED_PASSWORD);
            passwordUser.requestFocus();
            return false;
        }
        if (Pass.length() < 6) {
            passwordUser.setError(ERROR_LENGTH_PASSWORD);
            passwordUser.requestFocus();
            return false;
        }
        return true;
    }

    private void userLogin() {
        String email = emailUser.getText().toString().trim();
        String Pass = passwordUser.getText().toString().trim();
        boolean formChecked = checkForm(email, Pass);
        if (formChecked) {
            FIREBASE_AUTH_INITIALISATION.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login réussi.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_createUser:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.tv_login_forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.tv_login_createUser).setOnClickListener(this);
        findViewById(R.id.tv_login_forgotPassword).setOnClickListener(this);
        findViewById(R.id.et_login_email).setOnClickListener(this);
        findViewById(R.id.et_login_password).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        emailUser = findViewById(R.id.et_login_email);
        passwordUser = findViewById(R.id.et_login_password);
        // To del avant la MEP
        emailUser.setText("e@mail.to");
        passwordUser.setText("123456");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FIREBASE_AUTH_INITIALISATION = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = CURRENT_USER;
        if (firebaseUser != null) {
            FirebaseAuth.getInstance();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}