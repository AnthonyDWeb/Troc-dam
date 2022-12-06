package com.dam.troc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class EditProfil extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageButton et_btnSubmit;
    ImageView et_userProfileImage;
    Uri et_imgUri;
    EditText et_username, et_email, et_tel, et_address, et_city, et_postalCode, et_skill1, et_skill2, et_skill3, et_description;
    public static final int PICK_IMAGE = 1;


    private void InitUI(){
        et_userProfileImage = findViewById(R.id.iv_profile_photo);
        et_username = findViewById(R.id.et_profile_username);
        et_email = findViewById(R.id.et_profile_email);
        et_tel = findViewById(R.id.et_profile_tel);
        et_address = findViewById(R.id.et_profile_address);
        et_city = findViewById(R.id.et_profile_city);
        et_postalCode = findViewById(R.id.et_profile_postalCode);
        et_skill1 = findViewById(R.id.et_profile_skill1);
        et_skill2 = findViewById(R.id.et_profile_skill2);
        et_skill3 = findViewById(R.id.et_profile_skill3);
        et_description = findViewById(R.id.et_profile_description);
        et_btnSubmit = findViewById(R.id.btn_editProfil_editProfil);
    }

    private void getDataFromProfile(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            et_username.setText(bundle.getString("name"));
            et_email.setText(bundle.getString("email"));
            et_skill1.setText(bundle.getString("skill1"));
            et_skill2.setText(bundle.getString("skill2"));
            et_skill3.setText(bundle.getString("skill3"));
        }
    }


    private void choixImage (View view){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            et_imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), et_imgUri);
                et_userProfileImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + mAuth.getUid() + ".jpg");

        if (et_imgUri != null) {

            profileImageRef.putFile(et_imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            et_imgUri = Uri.parse(taskSnapshot.getUploadSessionUri().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserInformation(View view) {

        String uId = mAuth.getUid();
        Log.i("TAG", "saveUserInformation uid: " + uId);

        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String tel= et_tel.getText().toString();
        String address = et_address.getText().toString();
        String city = et_city.getText().toString();
        String postalCode= et_postalCode.getText().toString();
        String skill1 = et_skill1.getText().toString();
        String skill2= et_skill2.getText().toString();
        String skill3= et_skill3.getText().toString();
        String description = et_description.getText().toString();
        if (uId != null) {
            Log.i("TAG", "inside");
            if (username.isEmpty()) {
                et_username.setError("Champs obligatoire ");
                et_username.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                et_email.setError("Champs obligatoire") ;
                et_email.requestFocus();
                return;
            }

            if (tel.isEmpty()) {
                tel = "";
            }

            if (address.isEmpty()) {
                address = "";
            }


            if (postalCode.isEmpty()) {
                postalCode = "";
            }
            if (city.isEmpty()) {
                city = "";
            }
            if (skill1.isEmpty()) {
                skill1 = "";
            }
            if (skill2.isEmpty()) {
                skill2 = "";

            }
            if (skill3.isEmpty()) {
                skill3 = "";
            }
            if (description.isEmpty()) {
                description = "";
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("id ", uId);
            map.put("name ", username);
            map.put("email ", email);
            map.put("tel ", tel);
            map.put("address ", address);
            map.put("city ", city);
            map.put("postalCode ", postalCode);
            map.put("skill1 ", skill1);
            map.put("skill2 ", skill2);
            map.put("skill3 ", skill3);
            map.put("description ", description);

            Log.i("TAG", String.valueOf(map));


            db.collection("Users").document(uId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditProfil.this, "Profil sauvegardé !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfil.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfil.this, "Une erreur s'est produite " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(EditProfil.this, "Something is wrong ! No UID ", Toast.LENGTH_SHORT).show();
        }
    }


    /* private void upLoadToDbase (String id ,String plPseudo,String plNom,String plPrenom,String plMel,String plTel,String plAdress,
        String plVille,String plCpostal,String plComp1,String plComp2,String plComp3,String plDescription){
        if(!plPseudo.isEmpty() && !plNom.isEmpty()){
            // Création d'un tableau qui contoient les data à envoyer sur Firestore
            HashMap<String, Object> map = new HashMap<>();
            map.put("id ", id);
            map.put("Pseudo ", plPseudo);
            map.put("Nom ", plNom);
            map.put("Prenom ", plPrenom);
            map.put("Email ", plMel);
            map.put("Telephone ", plTel);
            map.put("Adresse ", plAdress );
            map.put("Ville ", plVille );
            map.put("Code Postal ", plCpostal);
            map.put("Competence 1 ", plComp1 );
            map.put("Competence 2 ", plComp2);
            map.put("Competence 3 ", plComp3);
            map.put("Description ", plDescription );


            db.collection("Profiles").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditProfil.this, "Profil sauvegardé !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfil.this, "Une erreur s'est produite " + e, Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "Empty fields are not allowed ", Toast.LENGTH_SHORT).show();
        }
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        InitUI();
        getDataFromProfile();

        et_userProfileImage.setOnClickListener(this::choixImage);
        uploadImageToFirebaseStorage();
        et_btnSubmit.setOnClickListener(this::saveUserInformation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}