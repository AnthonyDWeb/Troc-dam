package com.dam.troc.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dam.troc.Profil;
import com.dam.troc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchActivity extends Fragment {

    private static final String TAG = "SearchActivity";
    private FirebaseFirestore db;
    private RadioButton radioButtonProffesion, radioButtonUsername;
//    private ProfilAdapter adapter;
    private List<Profil> profilsList;

    private void initFirebaseTools() { db = FirebaseFirestore.getInstance(); }

    public void searchReadDataFromFirestore(){
        db.collection("Profils").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        profilsList.clear();
                        for(DocumentSnapshot documentSnapshot: task.getResult()){
                            Profil noteModel = new Profil();
                            profilsList.add(noteModel);
                            Log.i(TAG, "onComplete: " + documentSnapshot.getId());
                        }
//                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error loading" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFirebaseTools();
//        searchReadDataFromFirestore();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_search, container, false);
        radioButtonProffesion = rootView.findViewById(R.id.radioButtonProffesion);
        radioButtonProffesion.setOnClickListener(this::onClick);
        radioButtonUsername = rootView.findViewById(R.id.radioButtonUsername);
        radioButtonUsername.setOnClickListener(this::onClick);
        return rootView;
    }

    public void onClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButtonProffesion:
                if (checked)
                    radioButtonUsername.setChecked(false);
                break;
            case R.id.radioButtonUsername:
                if (checked)
                    radioButtonProffesion.setChecked(false);
                break;
        }
    }
}