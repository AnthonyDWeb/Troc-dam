package com.dam.troc.profile;

import static com.dam.troc.commons.Constants.*;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dam.troc.MainActivity;
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

public class EditProfile extends AppCompatActivity {
    ImageButton et_btnSubmit;
    ImageView et_userProfileImage;
    Uri et_imgUri;
    EditText et_username, et_email, et_tel, et_address, et_city, et_postalCode, et_skill1, et_skill2, et_skill3, et_description;
    public static final int PICK_IMAGE = 1;

    private void InitUI() {
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

    private void getDataFromProfile() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            et_username.setText(bundle.getString(NAME));
            et_email.setText(bundle.getString(EMAIL));
            et_skill1.setText(bundle.getString("skill1"));
            et_skill2.setText(bundle.getString("skill2"));
            et_skill3.setText(bundle.getString("skill3"));
        }
    }

    private void choixImage(View view) {
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
        StorageReference profileImageRef = STORAGE_INSTANCE.getReference("profilepics/" + FIREBASE_AUTH.getUid() + ".jpg");
        if (et_imgUri != null) { profileImageRef.putFile(et_imgUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { et_imgUri = Uri.parse(taskSnapshot.getUploadSessionUri().toString()); }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) { Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show(); }
            });
        }
    }

    private void saveUserInformation(View view) {
        String uId = FIREBASE_AUTH.getUid();
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        String address = et_address.getText().toString();
        String city = et_city.getText().toString();
        String postalCode = et_postalCode.getText().toString();
        String skill1 = et_skill1.getText().toString();
        String skill2 = et_skill2.getText().toString();
        String skill3 = et_skill3.getText().toString();
        String description = et_description.getText().toString();
        if (uId != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ID, uId);
            map.put(NAME, username);
            map.put(EMAIL, email);
            if (username.isEmpty()) { et_username.setError(NEEDED); et_username.requestFocus(); return; }
            if (email.isEmpty()) { et_email.setError(NEEDED); et_email.requestFocus(); return; }
            if (!tel.isEmpty()) { map.put(TEL, tel); }
            if (!city.isEmpty()) { map.put(CITY, city); }
            if (postalCode.isEmpty()) { map.put(POSTAL_CODE, postalCode); }
            if (!address.isEmpty()) { map.put(ADDRESS, address); }
            if (!description.isEmpty()) { map.put(DESCRIPTION, description); }
            if (!skill1.isEmpty()) { map.put("skill1 ", skill1); }
            if (!skill2.isEmpty()) { map.put("skill2 ", skill2); }
            if (!skill3.isEmpty()) {  map.put("skill3 ", skill3); }

            Log.i("TAG", String.valueOf(map));

            FIRESTORE_INSTANCE.collection(USERS).document(uId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditProfile.this, "Profil sauvegardé !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { Toast.makeText(EditProfile.this, "Une erreur s'est produite " + e, Toast.LENGTH_SHORT).show(); }
                    });
        } else { Toast.makeText(EditProfile.this, "Something is wrong ! No UID ", Toast.LENGTH_SHORT).show(); }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) { return super.onCreateView(name, context, attrs); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        InitUI();
        getDataFromProfile();
        uploadImageToFirebaseStorage();
        et_userProfileImage.setOnClickListener(this::choixImage);
        et_btnSubmit.setOnClickListener(this::saveUserInformation);
    }
}