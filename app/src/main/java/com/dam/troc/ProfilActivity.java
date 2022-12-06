package com.dam.troc;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilActivity extends Fragment {

    FirebaseAuth mAuth;
    private FirebaseFirestore db;

    View view;

    Button btnSubmit;
    ImageView userProfileImage;
    Uri imgUri;
    TextView username, email, tel, address, city, postalCode, skill1, skill2, skill3, description;
    public static final int PICK_IMAGE = 1;


    private void InitUI() {
        db = FirebaseFirestore.getInstance();
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
        FirebaseUser user = mAuth.getCurrentUser();

        db.collection("Users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            tel.setText(documentSnapshot.getString("tel"));
                            Log.i("TAG", "Retour de la base " + task.getResult());
                        }
                    }
                });

        if (user != null) {
            String uid = user.getUid();


            String userDisplayName = user.getDisplayName();

            String usermail = user.getEmail();
            email.setText(usermail);

            String userTel = user.getPhoneNumber();
            tel.setText(userTel);

            if (skill1.getText() == "") skill1.setVisibility(View.GONE);
            if (skill2.getText() == "") skill2.setVisibility(View.GONE);
            if (skill3.getText() == "") skill3.setVisibility(View.GONE);
        }
    }

    private void getDataToEdit(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username.getText().toString());
        bundle.putString("email", email.getText().toString());
        bundle.putString("skill1", skill1.getText().toString());
        bundle.putString("skill2", skill2.getText().toString());
        bundle.putString("skill3", skill3.getText().toString());

        Intent intent = new Intent(getContext(), EditProfil.class);
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitUI();
        loadUserInformation();
        view.findViewById(R.id.btn_profil_editProfil).setOnClickListener(this::getDataToEdit);
    }
}

