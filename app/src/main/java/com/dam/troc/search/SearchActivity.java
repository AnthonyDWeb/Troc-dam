package com.dam.troc.search;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dam.troc.profile.ProfileModel;
import com.dam.troc.R;
import com.dam.troc.utils.Gol;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchActivity extends Fragment {

    private static final String TAG = "SearchActivity";
    private FirebaseFirestore db;

    private View baseView;
    private RecyclerView rv_search_result;
    private Context context;
    private SearchAdapter adapter;
    private RadioButton radioButtonProffesion, radioButtonUsername;


    private void initFirebaseTools() { db = FIRESTORE_INSTANCE; }
    private void initUI(){
        context = getContext();
        baseView = baseView.findViewById(R.id.mainLayout_Search);
        radioButtonProffesion = baseView.findViewById(R.id.radioButtonProffesion);
        radioButtonUsername = baseView.findViewById(R.id.radioButtonUsername);
        rv_search_result = baseView.findViewById(R.id.rv_search_result);
        rv_search_result.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void getDataFromFirestore(){
        Query query = db.collection(USERS).orderBy(NAME);

        FirestoreRecyclerOptions<UserSearchModel> users = new FirestoreRecyclerOptions.Builder<UserSearchModel>().setQuery(query,UserSearchModel.class).build();
        Log.i("TAG", "getDataFromFirestore: " + users);
        adapter = new SearchAdapter(users);
        rv_search_result.setAdapter(adapter);
        adapter.startListening();
    }

    public void onClickRadioButton(View view) {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView =  inflater.inflate(R.layout.activity_search, container, false);
        initFirebaseTools();
        initUI();
//        Gol.showSnackbar(baseView, "Hello " + CURRENT_USER);
        radioButtonProffesion.setOnClickListener(this::onClickRadioButton);
        radioButtonUsername.setOnClickListener(this::onClickRadioButton);
        getDataFromFirestore();
        return baseView;
    }

}