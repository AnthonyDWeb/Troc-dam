package com.dam.troc;

import static com.dam.troc.R.id.editTextProfilVille;
import static com.dam.troc.R.id.edittextProfilCpostal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class EditProfil extends AppCompatActivity {
    Button btnSubmit;
    ImageView profilimg;
    Uri imgUrl;
    EditText plPseudo, plMel, plTel, plAdress, plVille, plCpostal, plComp1, plComp2,
    plComp3, plDescription;
    FirebaseAuth mAuth;
    public static final int PICK_IMAGE = 1;

    private FirebaseFirestore


     db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        mAuth = FirebaseAuth.getInstance();


        profilimg = findViewById(R.id.iv_profile_photo);
        plPseudo = findViewById(R.id.tv_user_profile_name);
        //plNom = findViewById(R.id.editTextTextProfilNom);
        //plPrenom = findViewById(R.id.EditTxtProfilPrenom);
        plMel = findViewById(R.id.editTextProfilEmail);
        plTel = findViewById(R.id.editTextTextProfilPhone);
        plAdress = findViewById(R.id.editTextProfilAdresse);
        plVille = findViewById(editTextProfilVille);
        plCpostal = findViewById(edittextProfilCpostal);
        plComp1 = findViewById(R.id.etprofilcomp1);
        plComp2 = findViewById(R.id.etprofilcomp2);
        plComp3 = findViewById(R.id.etprofilcomp3);
        plDescription = findViewById(R.id.et_profil_descriptionText);

        profilimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choixImage ();
            }
        });

        uploadImageToFirebaseStorage();
        findViewById(R.id.Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }



    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + mAuth.getUid() + ".jpg");

        if (imgUrl != null) {

            profileImageRef.putFile(imgUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgUrl = Uri.parse(taskSnapshot.getUploadSessionUri().toString());
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

    private void saveUserInformation() {

        String id = mAuth.getUid();
        String displayPseudo = plPseudo.getText().toString();
        //String displayNom = plNom.getText().toString();
        //String displayPrenom = plPrenom.getText().toString();
        String displayEmail = plMel.getText().toString();
        String displayTelphone= plTel.getText().toString();
        String displayAdresse = plAdress.getText().toString();
        String displayVille = plVille.getText().toString();
        String displayCpostal= plCpostal.getText().toString();
        String displayComp1 = plComp1.getText().toString();
        String displayComp2= plComp2.getText().toString();
        String displayComp3= plComp3.getText().toString();
        String displayDesc = plDescription.getText().toString();
        if (id != null) {
        if (displayPseudo.isEmpty()) {
          plPseudo.setError("Champs obligatoire ");
          plPseudo.requestFocus();
            return;
        }

        /*if (displayNom.isEmpty()) {
            plNom.setError("Champs obligatoire");
            plNom.requestFocus();
            return;
        }

        if (displayPrenom.isEmpty()) {
            plPrenom.setError("Champs obligatoire");
            plPrenom.requestFocus();
            return;
        }*/
        if (displayEmail.isEmpty()) {
         plMel.setError("Champs obligatoire") ;
         plMel.requestFocus();
            return;
        }

        if (displayTelphone.isEmpty()) {
            plTel.setError("Champs obligatoire") ;
            plTel.requestFocus();
            return;
        }

        if (displayAdresse.isEmpty()) {
            plAdress.setError("Champs obligatoire") ;
            plAdress.requestFocus();
            return;
        }


        if (displayCpostal.isEmpty()) {
            plCpostal.setError("Champs obligatoire");
            plCpostal.requestFocus();
            return;
        }
        if (displayVille.isEmpty()) {
            plVille.setError("Champs obligatoire");
            plVille.requestFocus();
            return;
        }
        if (displayComp1.isEmpty()) {
            plComp1.setText("");
            return;
        }
        if (displayComp2.isEmpty()) {
            plComp2.setText("");
            return;

        }
        if (displayComp3.isEmpty()) {
            plComp3.setText("");
            return;
        }
        if (displayDesc.isEmpty()) {
            plDescription.setText("");
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("id ", id);
        map.put("Pseudo ", plPseudo);
        //map.put("Nom ", plNom);
        //map.put("Prenom ", plPrenom);
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

             }

            else {


        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUrl = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),  imgUrl);
                profilimg.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


   private void choixImage (){

       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);


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


}