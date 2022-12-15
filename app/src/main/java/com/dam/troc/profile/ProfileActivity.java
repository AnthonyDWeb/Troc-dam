package com.dam.troc.profile;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.troc.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends Fragment {

    View view;
    Button btnSubmit, btn_addskill;
    ImageView userProfileImage;
    String imgUri;
    TextView username, email, tel, address, city, postalCode, description;
    TextView tv_arrSkill1,tv_arrSkill2,tv_arrSkill3;
    private Context context;
    RecyclerView skillsList;
    public static final int PICK_IMAGE = 1;
    private ProfileAdapter adapter;
    com.dam.troc.MainActivity mainActivity;

    private void InitUI() {
        userProfileImage = view.findViewById(R.id.iv_profile_photo);
        username = view.findViewById(R.id.tv_profile_username);
        email = view.findViewById(R.id.tv_profile_email);
        tel = view.findViewById(R.id.tv_profile_tel);
        city = view.findViewById(R.id.tv_profile_city);
        postalCode = view.findViewById(R.id.tv_profile_postalCode);
        address = view.findViewById(R.id.tv_profile_address);
        description = view.findViewById(R.id.tv_profile_description);
        btn_addskill = view.findViewById(R.id.btn_profile_addskill);
        tv_arrSkill1 = view.findViewById(R.id.tv_profile_arrSkill1);
        tv_arrSkill2 = view.findViewById(R.id.tv_profile_arrSkill2);
        tv_arrSkill3 = view.findViewById(R.id.tv_profile_arrSkill3);
        skillsList = view.findViewById(R.id.rv_profile_skills);
        skillsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void loadUserInformation() {
        FIRESTORE_INSTANCE_USERS.document(CURRENT_USER.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            username.setText(documentSnapshot.getString(NAME));
                            email.setText(documentSnapshot.getString(EMAIL));
                            tel.setText(documentSnapshot.getString(TEL));
                            city.setText(documentSnapshot.getString(CITY));
                            postalCode.setText(documentSnapshot.getString(POSTAL_CODE));
                            address.setText(documentSnapshot.getString(ADDRESS));
                            description.setText(documentSnapshot.getString(DESCRIPTION));
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists() && documentSnapshot.get(SKILLS) != null) {
                                ArrayList<String> skillsArrayList  = (ArrayList<String>) documentSnapshot.get(SKILLS);
                                for (int i = 0; i < skillsArrayList.size(); i++){
                                    if (i == 0) tv_arrSkill1.setText(skillsArrayList.get(0));
                                    if (i == 1) tv_arrSkill2.setText(skillsArrayList.get(1));
                                    if (i == 2) tv_arrSkill3.setText(skillsArrayList.get(2));
                                    Log.i("TAG", "onComplete: " + "INSIDE");
                                }
                                if (tv_arrSkill1.getText().length() == 0) tv_arrSkill1.setVisibility(View.GONE);
                                if (tv_arrSkill2.getText().length() == 0) tv_arrSkill2.setVisibility(View.GONE);
                                if (tv_arrSkill3.getText().length() == 0) tv_arrSkill3.setVisibility(View.GONE);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void getDataSkillsFromFirestore() {
        Query query = FIRESTORE_INSTANCE_USERS.whereEqualTo(ID, CURRENT_USER.getUid()).whereEqualTo(SKILLS, true);
        FirestoreRecyclerOptions<ProfileModel> skills = new FirestoreRecyclerOptions.Builder<ProfileModel>().setQuery(query, ProfileModel.class).build();
        adapter = new ProfileAdapter(skills);
        skillsList.setAdapter(adapter);
        adapter.startListening();

        FIRESTORE_INSTANCE_USERS.whereEqualTo(ID, CURRENT_USER.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void getDataToEdit(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(NAME, username.getText().toString());
        bundle.putString(EMAIL, email.getText().toString());
        bundle.putString(TEL, tel.getText().toString());
        bundle.putString(CITY, city.getText().toString());
        bundle.putString(POSTAL_CODE, postalCode.getText().toString());
        bundle.putString(ADDRESS, address.getText().toString());
        bundle.putString(DESCRIPTION, description.getText().toString());

        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (com.dam.troc.MainActivity) getActivity();
        view.findViewById(R.id.btn_profil_editProfil).setOnClickListener(this::getDataToEdit);
        btn_addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addSkill(view);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profil, container, false);
        InitUI();
        loadUserInformation();
        getDataSkillsFromFirestore();
        Log.i("TAG", "onCreateView: " + (tv_arrSkill1.getText().length() == 0));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

