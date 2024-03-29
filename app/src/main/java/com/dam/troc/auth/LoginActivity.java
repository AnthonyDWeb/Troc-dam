package com.dam.troc.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailUser, tvPass;
    FirebaseAuth mAuth;

    private boolean checkForm(String email, String Pass) {
        if (email.isEmpty()) { emailUser.setError("Email obligatoire!"); emailUser.requestFocus(); return false; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailUser.setError("Adresse email non conforme!!"); emailUser.requestFocus(); return false; }
        if (Pass.isEmpty()) { tvPass.setError("Password obligatoire"); tvPass.requestFocus(); return false; }
        if (Pass.length() < 6) { tvPass.setError("Minimum 6 chars!"); tvPass.requestFocus(); return false; }
        return true;
    }

    private void userLogin() {
        String email = emailUser.getText().toString().trim();
        String Pass = tvPass.getText().toString().trim();
        boolean formChecked = checkForm(email, Pass);
        if (formChecked) {
            mAuth.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                startActivity(new Intent(this, Forgotten_Pass.class));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.tv_login_createUser).setOnClickListener(this);
        findViewById(R.id.tv_login_forgotPassword).setOnClickListener(this);
        findViewById(R.id.et_login_email).setOnClickListener(this);
        findViewById(R.id.et_login_password).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        emailUser = findViewById(R.id.et_login_email);
        tvPass = findViewById(R.id.et_login_password);
        // To del avant la MEP
        emailUser.setText("a@mail.to");
        tvPass.setText("123456");

    }
}