package com.dam.troc.profile;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.dam.troc.search.SearchAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends Fragment {

    View view;
    Button btnSubmit;
    ImageView userProfileImage;
    String imgUri;
    TextView username, email, tel, address, city, postalCode, description;
    private Context context;
    RecyclerView skillsList;
    public static final int PICK_IMAGE = 1;
    private ProfileAdapter adapter;


    private void InitUI() {
        userProfileImage = view.findViewById(R.id.iv_profile_photo);
        username = view.findViewById(R.id.tv_profile_username);
        email = view.findViewById(R.id.tv_profile_email);
        tel = view.findViewById(R.id.tv_profile_tel);
        city = view.findViewById(R.id.tv_profile_city);
        postalCode = view.findViewById(R.id.tv_profile_postalCode);
        address = view.findViewById(R.id.tv_profile_address);
        description = view.findViewById(R.id.tv_profile_description);
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
                            ArrayList<String> skillsArrayList = new ArrayList<>();
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                String toto = documentSnapshot.get(SKILLS).toString();
//                                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                                    skillsArrayList.add(entry.getValue().toString());
//                                }
                                //Log.i("TAG", "onComplete: " + toto);
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
        // loadUserInformation();
        view.findViewById(R.id.btn_profil_editProfil).setOnClickListener(this::getDataToEdit);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profil, container, false);
        InitUI();
        loadUserInformation();
        getDataSkillsFromFirestore();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

