package com.dam.troc.profile;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.troc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends Fragment {

    FirebaseAuth mAuth;
    private FirebaseFirestore db;

    View view;
    String name;

    Button btnSubmit;
    ImageView userProfileImage;
    Uri imgUri;
    TextView username, email, tel, address, city, postalCode, skill1, skill2, skill3, description;
    public static final int PICK_IMAGE = 1;


    private void InitUI() {
        userProfileImage = view.findViewById(R.id.iv_profile_photo);
        username = view.findViewById(R.id.tv_profile_username);
        email = view.findViewById(R.id.tv_profile_email);
        tel = view.findViewById(R.id.tv_profile_tel);
        address = view.findViewById(R.id.tv_profile_address);
        city = view.findViewById(R.id.tv_profile_city);
        postalCode = view.findViewById(R.id.tv_profile_postalCode);
        skill1 = view.findViewById(R.id.tv_profile_skill1);
        skill2 = view.findViewById(R.id.tv_profile_skill2);
        skill3 = view.findViewById(R.id.tv_profile_skill3);
        description = view.findViewById(R.id.tv_profile_description);
    }

    private void loadUserInformation() {
        db = FIRESTORE_INSTANCE;
        FirebaseUser user = CURRENT_USER;
        db.collection(USERS).document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            name = documentSnapshot.getString("name");
                            username.setText(name);
                            email.setText(documentSnapshot.getString("email"));
                            tel.setText(documentSnapshot.getString("tel"));

                            if (skill1.getText() == "") skill1.setVisibility(View.GONE);
                            if (skill2.getText() == "") skill2.setVisibility(View.GONE);
                            if (skill3.getText() == "") skill3.setVisibility(View.GONE);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {}
                });
    }

    private void getDataToEdit(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(NAME, username.getText().toString());
        bundle.putString(EMAIL, email.getText().toString());

        Intent intent = new Intent(getContext(), EditProfile.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profil, container, false);
        InitUI();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserInformation();
        view.findViewById(R.id.btn_profil_editProfil).setOnClickListener(this::getDataToEdit);
    }
}

