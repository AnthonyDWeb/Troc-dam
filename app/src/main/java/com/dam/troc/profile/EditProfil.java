package com.dam.troc.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.troc.R;
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

public class EditProfil extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] metiers ={"Agriculture","Architecture","Art","Artisanat","Batiment","Bois","Babysitter","Construction","Jardinage",
            "Culture","Hôtellerie","Restauration","Tourisme","Industries","Developpement web","Transport","Santé","Banque","Assurances",
            "Immobilier","Commerce","Vente","Marketting",
            "Transport","Informatique","Internet","Service à la personne","Logistique","Social"};



    FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapter;
    ImageButton et_btnSubmit;
    ImageView et_userProfileImage;
    Uri et_imgUri;
    EditText et_username, et_email, et_tel, et_address, et_city, et_postalCode, et_description;
    Spinner spinner01, spinner02, spinner03;
    public static final int PICK_IMAGE = 1;

    public String et_skill1 = "";
    public String et_skill2 = "";
    public String et_skill3 = "";



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








    public void InitUI(){
        et_userProfileImage = findViewById(R.id.iv_profile_photo);
        et_username = findViewById(R.id.et_profile_username);
        et_email = findViewById(R.id.et_profile_email);
        et_tel = findViewById(R.id.et_profile_tel);
        et_address = findViewById(R.id.et_profile_address);
        et_city = findViewById(R.id.et_profile_city);
        et_postalCode = findViewById(R.id.et_profile_postalCode);
        spinner01 = findViewById(R.id.et_profile_skill1);
        spinner02 = findViewById(R.id.et_profile_skill2);
        spinner03 = findViewById(R.id.et_profile_skill3);
        et_description = findViewById(R.id.et_profile_description);
        et_btnSubmit = findViewById(R.id.btn_editProfil_editProfil);



        spinner01.setOnItemSelectedListener(this);
        spinner02.setOnItemSelectedListener(this);
        spinner03.setOnItemSelectedListener(this);


        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,metiers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spinner01.setAdapter(adapter);
        spinner02.setAdapter(adapter);
        spinner03.setAdapter(adapter);



    }

    private void getDataFromProfile(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            et_username.setText(bundle.getString("name"));
            et_email.setText(bundle.getString("email"));
          /*  et_skill1.setText (bundle.getString("skill1"));
            et_skill2.setText(bundle.getString("skill2"));
            et_skill3.setText(bundle.getString("skill3"));*/
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

    public void saveUserInformation(View view) {

        String uId = mAuth.getUid();


        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String tel= et_tel.getText().toString();
        String address = et_address.getText().toString();
        String city = et_city.getText().toString();
        String postalCode= et_postalCode.getText().toString();
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
                et_tel.setText("");
            }

            if (address.isEmpty()) {
                et_address.setText("");
            }


            if (postalCode.isEmpty()) {
                et_postalCode.setText("");
            }
            if (city.isEmpty()) {
                et_city.setText("");
            }
            if (et_skill1.isEmpty()) {
                et_skill1 =("");
            }
            if (et_skill2.isEmpty()) {
                et_skill2 = ("");

            }
            if (et_skill3.isEmpty()) {
                et_skill3 = ("");
            }
            if (description.isEmpty()) {
                et_description.setText("");
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("id ", uId);
            map.put("name ", username);
            map.put("email ", email);
            map.put("tel ", tel);
            map.put("address ", address);
            map.put("city ", city);
            map.put("postalCode ", postalCode);
            map.put("skill1 ", et_skill1);
            map.put("skill2 ", et_skill2);
            map.put("skill3 ", et_skill3);
            map.put("Description ", description);




            db.collection("Users").document(uId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("TAG", "inside 2");
                            Toast.makeText(EditProfil.this, "Profil sauvegardé !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("TAG", "inside 3");
                            Toast.makeText(EditProfil.this, "Une erreur s'est produite " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Log.i("TAG", "inside 4");
            Toast.makeText(EditProfil.this, "Something is wrong ! No UID ", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.et_profile_skill1:
                Toast.makeText(getApplicationContext(), metiers[position], Toast.LENGTH_SHORT).show();
                et_skill1 = spinner01.getSelectedItem().toString();
                spinner01.setSelection(position);
                break;

            case R.id.et_profile_skill2:
                Toast.makeText(getApplicationContext(), metiers[position], Toast.LENGTH_SHORT).show();
                et_skill2 = spinner02.getSelectedItem().toString();
                spinner02.setSelection(position);
                break;

            case R.id.et_profile_skill3:

                Toast.makeText(getApplicationContext(), metiers[position], Toast.LENGTH_SHORT).show();
                et_skill3 = spinner03.getSelectedItem().toString();
                spinner03.setSelection(position);

                break;


            default:

                break;

        }}

    @Override
    public void onNothingSelected (AdapterView < ? > parent){

    }


}