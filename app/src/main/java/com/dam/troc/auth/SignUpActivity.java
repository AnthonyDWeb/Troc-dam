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


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SignupActivity";
    TextInputEditText emailUser, username, password,confirmPassword;

    private FirebaseAuth mAuth;
    // Ajout de la variable FirebaseUser
    private FirebaseUser firebaseUser;
    // Ajout de la variable de liaison avec les collections du Cloud FireStore
    private CollectionReference collectionReference; // Le reste est dans la méthode initFireStore
    // Ajout de la variable de liaison avec FirebaseStorage
    private StorageReference fileStorage;
    // Variables des Uri du fichier image de l'avatar utilisateur
    private Uri localFileUri; // L'Uri du fichier sur le terminal
    private Uri serverFileUri; // L'UrL du fichier stocké dans le storage (on parle bien ici d'un U R L (ELLE))
    private String urlStorageAvatar; // Le String de l'url stocké dans le storage pour l'ajouter dans la base Users
    // Variable pour la localisation de l'ImageView
    private String userID;



    // Méthode initFirebase pour initialiser les composants de Firebase
    private void initFirebase() {
        /** 6.2 Insertion dans Firestore **/
        collectionReference = FIRESTORE_INSTANCE.collection(USERS); // Instance définie dans la classe des constantes

        /** 7.2 Initialisation du bucket pour le stockage des avatars utilisateurs **/
        fileStorage = STORAGE_INSTANCE.getReference();
    }

    private void registerUser(){

        String email = emailUser.getText().toString().trim();
        String userName = username.getText().toString().trim();
        String Pass = password.getText().toString().trim();
        String ConfirPass = confirmPassword.getText().toString().trim();
        TextView accueil = findViewById(R.id.btn_signup);

        if (email.isEmpty()) {
            emailUser.setError("Email obligatoire!");
            emailUser.requestFocus();
            return;
        } else if (userName.isEmpty()) {
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("Adresse email non conforme!!");
            emailUser.requestFocus();
            return;
        } else if (Pass.isEmpty()) {
            password.setError("mot de passe obligatoire");
            password.requestFocus();
            return;
        } else if (!Pass.equals(ConfirPass)) {
            confirmPassword.setError("mot de passe non identique");
            confirmPassword.requestFocus();
            return;
        } else if (Pass.length() <6) {
            password.setError("Minimum 6 charactere!");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    userID = firebaseUser.getUid();
                    Log.i(TAG, "User creation " + userID);
                    updateUsername();
                    Toast.makeText(getApplicationContext(),"Inscription réussie", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Cette email est déjè pris", Toast.LENGTH_LONG).show();
                        emailUser.setError("Cette email est déjè pris");
                        emailUser.requestFocus();
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

    private void updateUsername() {
        // Utilisation de la méthode UserProfileChangeRequest pour charger le nom de l'utilisateur qui s'est enregistré
        // Gestion de remplissage d'Authenticator
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString().trim())
                .build();

        // Gestion du remplissage de la base de données
        firebaseUser.updateProfile(request)
                // Ajout d'un listener qui affiche un Toast si tout c'est bien déroulé
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Création du HashMap pour la gestion des données
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(NAME, username.getText().toString().trim());
                            hashMap.put(ID, userID);
                            hashMap.put(EMAIL, emailUser.getText().toString().trim());
                            hashMap.put(ONLINE, "true"); // User set ONLINE, true, car il est dans on profile
                            hashMap.put(AVATAR, ""); // Vide pour le moment
                            Log.i(TAG, "Name only " + userID);
                            collectionReference.document(userID).set(hashMap)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                            // Lancement de l'activité suivante
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        }
                                    });

                        } else {}
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        initFirebase();
        setContentView(R.layout.activity_sign_up);

        emailUser = findViewById(R.id.et_signup_email);
        emailUser.setText("@mail.to");
        username = findViewById(R.id.et_signup_username);
        password = findViewById(R.id.et_signup_password);
        password.setText("123456");
        confirmPassword = findViewById(R.id.et_signup_password_verification);
        confirmPassword.setText("123456");

        findViewById(R.id.btn_signup).setOnClickListener(this);

    }
}