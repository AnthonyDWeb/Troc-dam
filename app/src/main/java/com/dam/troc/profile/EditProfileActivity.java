package com.dam.troc.profile;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.troc.MainActivity;
import com.dam.troc.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    ImageButton et_btnSubmit;
    ImageView et_userProfileImage;
    Uri et_imgUri;
    String skillnameSelected;
    EditSkillAdapter adapter;
    RecyclerView rc_dialog_skill;
    TextView job1, job2, job3;
    EditText et_username, et_email, et_tel, et_address, et_city, et_postalCode, et_description;
    Button btn_skill1, btn_skill2, btn_skill3;
    SearchView searchView;
    LinearLayout linear_profil_hearder, emailField, telField, adressField, descriptionField;
    public static final int PICK_IMAGE = 1;

    private void InitUI() {
        et_userProfileImage = findViewById(R.id.iv_profile_photo);
        et_username = findViewById(R.id.et_profile_username);
        et_email = findViewById(R.id.et_profile_email);
        et_tel = findViewById(R.id.et_profile_tel);
        et_address = findViewById(R.id.et_profile_address);
        et_city = findViewById(R.id.et_profile_city);
        et_postalCode = findViewById(R.id.et_profile_postalCode);
        et_description = findViewById(R.id.et_profile_description);
        btn_skill1 = findViewById(R.id.btn_profile_skill1);
        btn_skill2 = findViewById(R.id.btn_profile_skill2);
        btn_skill3 = findViewById(R.id.btn_profile_skill3);
        et_btnSubmit = findViewById(R.id.btn_editProfil_editProfil);
        linear_profil_hearder = findViewById(R.id.linear_profil_hearder);
        emailField = findViewById(R.id.emailField);
        telField = findViewById(R.id.telField);
        adressField = findViewById(R.id.adressField);
        descriptionField = findViewById(R.id.descriptionField);
    }

    private void getDataFromProfile() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            et_username.setText(bundle.getString(NAME));
            et_email.setText(bundle.getString(EMAIL));
            et_tel.setText(bundle.getString(TEL));
            et_city.setText(bundle.getString(CITY));
            et_postalCode.setText(bundle.getString(POSTAL_CODE));
            et_address.setText(bundle.getString(ADDRESS));
            et_description.setText(bundle.getString(DESCRIPTION));
        }
    }

    public void addValue(View view) {
        TextView itemSelection = view.findViewById(R.id.tv_card_skill);
        skillnameSelected = itemSelection.getText().toString();
        if(job1.getText().equals("aucun")){
            job1.setText(skillnameSelected);
        } else if (!job1.getText().equals("aucun") && job2.getText().equals("aucun")) {
            job2.setText(skillnameSelected);
        } else if (!job1.getText().equals("aucun") && !job2.getText().equals("aucun")){
            job3.setText(skillnameSelected);
        }
    }

    private void hideAllExceptSkill(boolean hide) {
        linear_profil_hearder.setVisibility(View.GONE);
        emailField.setVisibility(View.GONE);
        telField.setVisibility(View.GONE);
        adressField.setVisibility(View.GONE);
        descriptionField.setVisibility(View.GONE);
    }

    public void addSkill(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialog_add_skill, null);
        rc_dialog_skill = (RecyclerView) subView.findViewById(R.id.rc_dialog_skill);
        searchView = (SearchView) subView.findViewById(R.id.dialog_skill_searchview);
        job1 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job1_selection);
        job2 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job2_selection);
        job3 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job3_selection);
        final LinearLayout job1_section = (LinearLayout) subView.findViewById(R.id.dialog_job1_section);
        final LinearLayout job2_section = (LinearLayout) subView.findViewById(R.id.dialog_job2_section);
        final LinearLayout job3_section = (LinearLayout) subView.findViewById(R.id.dialog_job3_section);
        final Button addAnotherSkill = (Button) subView.findViewById(R.id.btn_dialog_skill_add_another);

        rc_dialog_skill.setLayoutManager(new LinearLayoutManager(this));

        Query query = FIRESTORE_INSTANCE_JOBS;
        FirestoreRecyclerOptions<EditSkillModel> jobs = new FirestoreRecyclerOptions.Builder<EditSkillModel>()
                .setQuery(query, EditSkillModel.class).build();
        adapter = new EditSkillAdapter(jobs);
        rc_dialog_skill.setAdapter(adapter);
        adapter.startListening();

        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        /** Gestion du bouton réponse valider **/
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn_skill1.setText(job1.getText());
                btn_skill2.setText(job2.getText());
                btn_skill3.setText(job3.getText());
            }
        });
        /** Gestion du bouton réponse négative **/
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditProfileActivity.this, "eddit annulé", Toast.LENGTH_LONG).show();
            }
        });
        builder.setView(subView);
        builder.create();
        builder.show();

        if(!btn_skill1.getText().equals("ajouter une compétence")) { job1.setText(btn_skill1.getText()); }
        if(!btn_skill2.getText().equals("ajouter une compétence")) { job2.setText(btn_skill2.getText()); }
        if(!btn_skill3.getText().equals("ajouter une compétence")) { job3.setText(btn_skill3.getText()); }

        if (job1.getText().equals("aucun")) {
            job2_section.setVisibility(View.GONE);
            job3_section.setVisibility(View.GONE);
        }

        TextView itemSelection = view.findViewById(R.id.tv_card_skill);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataSearchFromFirestore(newText.toString());
                return false;
            }
        });
        addAnotherSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!job1.getText().equals("aucun") && job2.getText().equals("aucun")) {
                    job2_section.setVisibility(View.VISIBLE);
                } else if (!job1.getText().equals("aucun") && !job2.getText().equals("aucun")) {
                    job3_section.setVisibility(View.VISIBLE);
                    addAnotherSkill.setVisibility(View.GONE);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Choisissez un métier", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDataSearchFromFirestore(String searchValue) {
        Query query = FIRESTORE_INSTANCE_JOBS.orderBy(SKILLNAME).startAt(searchValue).endAt(searchValue+"\uf8ff");
        FirestoreRecyclerOptions<EditSkillModel> jobs = new FirestoreRecyclerOptions.Builder<EditSkillModel>().setQuery(query, EditSkillModel.class).build();
        adapter = new EditSkillAdapter(jobs);
        rc_dialog_skill.setAdapter(adapter);
        adapter.startListening();
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
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserInformation(View view) {
        String uId = CURRENT_USER.getUid();
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        String city = et_city.getText().toString();
        String postalCode = et_postalCode.getText().toString();
        String address = et_address.getText().toString();
        String description = et_description.getText().toString();
        if (uId != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ID, uId);
            map.put(NAME, username);
            map.put(EMAIL, email);
            if (username.isEmpty()) {
                et_username.setError(NEEDED);
                et_username.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                et_email.setError(NEEDED);
                et_email.requestFocus();
                return;
            }
            if (!tel.isEmpty()) {
                map.put(TEL, tel);
            }
            if (!city.isEmpty()) {
                map.put(CITY, city);
            }
            if (!postalCode.isEmpty()) {
                map.put(POSTAL_CODE, postalCode);
            }
            if (!address.isEmpty()) {
                map.put(ADDRESS, address);
            }
            if (!description.isEmpty()) {
                map.put(DESCRIPTION, description);
            }


            FIRESTORE_INSTANCE.collection(USERS).document(uId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditProfileActivity.this, "Profil sauvegardé !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Une erreur s'est produite " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditProfileActivity.this, "Something is wrong ! No UID ", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        InitUI();
        getDataFromProfile();
        uploadImageToFirebaseStorage();
        et_userProfileImage.setOnClickListener(this::choixImage);
        et_btnSubmit.setOnClickListener(this::saveUserInformation);
//        et_skill1.setOnClickListener(this::addSkill);
//        et_skill2.setOnClickListener(this::addSkill);
//        et_skill3.setOnClickListener(this::addSkill);
    }

}