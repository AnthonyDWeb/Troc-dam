package com.dam.troc.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.dam.troc.R;

public class SearchActivity extends Fragment {

    RadioButton radioButtonProffesion, radioButtonUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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