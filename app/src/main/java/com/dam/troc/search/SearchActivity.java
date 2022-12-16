package com.dam.troc.search;

import static com.dam.troc.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dam.troc.MainActivity;
import com.dam.troc.profile.ProfileModel;
import com.dam.troc.R;
import com.dam.troc.utils.Gol;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchActivity extends Fragment {

    private static final String TAG = "SearchActivity";

    private View baseView;
    private RecyclerView rv_search_result;
    private SearchView sv_search_userSearch;
    private Context context;
    private SearchAdapter adapter;
    private RadioButton radioButtonProffesion, radioButtonUsername;
    private String Filter;

    private void initUI() {
        context = getContext();
        baseView = baseView.findViewById(R.id.mainLayout_Search);
        rv_search_result = baseView.findViewById(R.id.rv_search_result);
        sv_search_userSearch = baseView.findViewById(R.id.sv_search_userSearch);
        radioButtonProffesion = baseView.findViewById(R.id.radioButtonProffesion);
        radioButtonUsername = baseView.findViewById(R.id.radioButtonUsername);
        rv_search_result.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void getDataFromFirestore() {
        FirebaseUser user = CURRENT_USER;
        Query query = FIRESTORE_INSTANCE_USERS.whereNotEqualTo(ID,user.getUid());
        FirestoreRecyclerOptions<ProfileModel> users = new FirestoreRecyclerOptions.Builder<ProfileModel>().setQuery(query, ProfileModel.class).build();
        adapter = new SearchAdapter(users); rv_search_result.setAdapter(adapter); adapter.startListening();
    }


    private void getDataSearchFromFirestore(String searchValue) {
        Query query = FIRESTORE_INSTANCE_USERS;
        if (Filter.equals(NAME)) query = query.orderBy(NAME).startAt(searchValue).endAt(searchValue+"\uf8ff");
        if (Filter.equals(SKILLS)) query = query.whereArrayContains(SKILLS, searchValue);
        FirestoreRecyclerOptions<ProfileModel> users = new FirestoreRecyclerOptions.Builder<ProfileModel>().setQuery(query, ProfileModel.class).build();
        adapter = new SearchAdapter(users);
        rv_search_result.setAdapter(adapter);
        adapter.startListening();
    }


    private void onSearchTextChanged(View view, boolean b) {
        sv_search_userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataSearchFromFirestore(newText.toString());
                return false;
            }
        });
    }

    public void onClickRadioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        boolean jobSelection = view.getId() == R.id.radioButtonProffesion && checked;
        // Check which radio button was clicked
        if (jobSelection) { radioButtonUsername.setChecked(false); Filter = SKILLS; }
        else { radioButtonProffesion.setChecked(false); Filter = NAME; }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.activity_search, container, false);
        initUI();
        getDataFromFirestore();
        if (Filter == null) Filter = NAME;
        radioButtonProffesion.setOnClickListener(this::onClickRadioButton);
        radioButtonUsername.setOnClickListener(this::onClickRadioButton);
        sv_search_userSearch.setOnQueryTextFocusChangeListener(this::onSearchTextChanged);

        return baseView;
    }

}