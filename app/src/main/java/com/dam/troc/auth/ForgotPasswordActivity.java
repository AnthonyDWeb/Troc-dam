package com.dam.troc.auth;


import static com.dam.troc.commons.Constants.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.troc.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button submit;
    private EditText editEmail;

    private void submitForm(View view) {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()){
            editEmail.setError(NEED_EMAIL);
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError(INCORRECT_EMAIL);
            editEmail.requestFocus();
            return;

        }
        FIREBASE_AUTH.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Un email vous a été envoyé.", Toast.LENGTH_SHORT).show();
                        //finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void backToLogin(View view){ Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class); startActivity(intent); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_pass);
        editEmail = findViewById(R.id.tiet_forgotPassword_email);
        submit = findViewById(R.id.btn_forgottenPassword_submit);
        submit.setOnClickListener(this::submitForm);
    }
}



