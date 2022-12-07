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
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText et_search_userSearch;
    private ImageView iv_search_iconSearch;
    private Context context;
    private SearchAdapter adapter;
    private RadioButton radioButtonProffesion, radioButtonUsername;
    private String RadioButtonSelection;


    private void initFirebaseTools() {
        db = FIRESTORE_INSTANCE;
    }

    private void initUI() {
        context = getContext();
        baseView = baseView.findViewById(R.id.mainLayout_Search);
        rv_search_result = baseView.findViewById(R.id.rv_search_result);
        et_search_userSearch = baseView.findViewById(R.id.et_search_userSearch);
        iv_search_iconSearch = baseView.findViewById(R.id.iv_search_iconSearch);
        radioButtonProffesion = baseView.findViewById(R.id.radioButtonProffesion);
        radioButtonUsername = baseView.findViewById(R.id.radioButtonUsername);
        rv_search_result.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void getDataFromFirestore() {
        Query query = db.collection(USERS).whereNotEqualTo(NAME, CURRENT_USER.getDisplayName()).orderBy(NAME);
        // TODO filter par name & id
//        .whereNotEqualTo(ID,CURRENT_USER.getUid());
        FirestoreRecyclerOptions<UserSearchModel> users = new FirestoreRecyclerOptions.Builder<UserSearchModel>().setQuery(query, UserSearchModel.class).build();
        adapter = new SearchAdapter(users);
        rv_search_result.setAdapter(adapter);
        adapter.startListening();
    }

    public void onClickRadioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        boolean jobSelection = view.getId() == R.id.radioButtonProffesion && checked;
        // Check which radio button was clicked
        if (jobSelection) {
            radioButtonUsername.setChecked(false);
            RadioButtonSelection = "métier";
        } else {
            radioButtonProffesion.setChecked(false);
            RadioButtonSelection = "nom d'utilisateur";
        }
        Log.i("TAG", "onClickRadioButton: " + RadioButtonSelection);
    }

    private void onSearchSubmit(String searchValue, String filter){
        Log.i("TAG", "onSearchSubmit searchValue: " + searchValue);
        Log.i("TAG", "onSearchSubmit filter: " + filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.activity_search, container, false);
        initFirebaseTools();
        initUI();
        radioButtonProffesion.setOnClickListener(this::onClickRadioButton);
        radioButtonUsername.setOnClickListener(this::onClickRadioButton);
        getDataFromFirestore();

        return baseView;
    }

}