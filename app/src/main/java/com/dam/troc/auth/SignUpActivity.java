package com.dam.troc.auth;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.troc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    TextInputEditText emailUser, username, password, confirmPassword;

    // Ajout de la variable FirebaseUser
    private FirebaseUser FIREBASE_USER;
    // Ajout de la variable de liaison avec les collections du Cloud FireStore
    private CollectionReference COLLECTION_REFERENCE_USER; // Le reste est dans la méthode initFireStore
    // Ajout de la variable de liaison avec FirebaseStorage
    private StorageReference FILE_STORAGE;
    private String userID, userEmail, userName, userPassword, ConfirmUserPassword;

    // Méthode initFirebase pour initialiser les composants de Firebase
    private void initFirebase() {
        /** Insertion dans Firestore **/
        COLLECTION_REFERENCE_USER = FIRESTORE_INSTANCE.collection(USERS);
        /** Initialisation du bucket pour le stockage des avatars utilisateurs **/
        FILE_STORAGE = STORAGE_INSTANCE.getReference();
    }

    private void registerUser(View view) {
        userEmail = emailUser.getText().toString().trim();
        userName = username.getText().toString().trim();
        userPassword = password.getText().toString().trim();
        ConfirmUserPassword = confirmPassword.getText().toString().trim();

        if (userEmail.isEmpty()) {
            emailUser.setError(NEED_EMAIL);
            emailUser.requestFocus();
            return;
        } else if (userName.isEmpty()) {
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            emailUser.setError(INCORRECT_EMAIL);
            emailUser.requestFocus();
            return;
        } else if (userPassword.isEmpty()) {
            password.setError(NEED_PASSWORD);
            password.requestFocus();
            return;
        } else if (!userPassword.equals(ConfirmUserPassword)) {
            confirmPassword.setError(NO_MATCH_PASSWORD);
            confirmPassword.requestFocus();
            return;
        } else if (userPassword.length() < 6) {
            password.setError(ERROR_LENGTH_PASSWORD);
            password.requestFocus();
            return;
        }
        FIREBASE_AUTH.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = CURRENT_USER.getUid();
                    updateUsername();
                    Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), USED_EMAIL, Toast.LENGTH_LONG).show();
                        emailUser.setError(USED_EMAIL);
                        emailUser.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void updateUsername() {
        // Utilisation de la méthode UserProfileChangeRequest pour charger le nom de l'utilisateur qui s'est enregistré
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
        // Gestion du remplissage de la base de données
        FIREBASE_USER.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Création du HashMap pour la gestion des données
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ID, userID);
                    hashMap.put(ONLINE, "true"); // User set ONLINE, true, car il est dans on profile
                    hashMap.put(NAME, userName);
                    hashMap.put(EMAIL, userEmail);
                    hashMap.put(AVATAR, "");
                    COLLECTION_REFERENCE_USER.document(userID).set(hashMap).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                    });
                } else { }
            }
        });
    }

    public void backToLogin(View view) { Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); startActivity(intent); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initFirebase();
        emailUser = findViewById(R.id.et_signup_email);
        username = findViewById(R.id.et_signup_username);
        password = findViewById(R.id.et_signup_password);
        confirmPassword = findViewById(R.id.et_signup_password_verification);
        findViewById(R.id.btn_signup).setOnClickListener(this::registerUser);
        emailUser.setText("@mail.to");
        password.setText("123456");
        confirmPassword.setText("123456");
    }
}