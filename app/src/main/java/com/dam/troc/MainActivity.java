package com.dam.troc;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView emailUser, tvPass;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.newUser).setOnClickListener(this);
        findViewById(R.id.forgotPass).setOnClickListener(this);
        findViewById(R.id.TvEmail).setOnClickListener(this);
        findViewById(R.id.tvpassWord).setOnClickListener(this);
        findViewById(R.id.main_btn_register).setOnClickListener(this);

        emailUser = findViewById(R.id.TvEmail);
        tvPass = findViewById(R.id.tvpassWord);


    }



    private void userLogin(){

        String email = emailUser.getText().toString().trim();
        String Pass = tvPass.getText().toString().trim();


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

        mAuth.signInWithEmailAndPassword(email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(),"Login réussi.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,ProfilActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });


    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.newUser:

                startActivity( new Intent(this, SignUpActivity.class));
                break;

            case R.id.main_btn_register:

                userLogin();
                break;

            case R.id.forgotPass:

                startActivity( new Intent(this, Forgotten_Pass.class));
                break;

        }

    }
}