package com.dam.troc;

import static com.dam.troc.R.id.editTextProfilVille;
import static com.dam.troc.R.id.edittextProfilCpostal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilActivity extends Fragment {

    Button btnSubmit;
    ImageView profilimg;
    Uri imgUrl;
    TextView plPseudo, plMel, plTel, plAdress, plVille, plCpostal, plComp1, plComp2,
            plComp3, plDescription;
    FirebaseAuth mAuth;
    public static final int PICK_IMAGE = 1;

    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
      ViewGroup itemView = (ViewGroup)inflater.inflate(R.layout.activity_profil,container,false);

        profilimg = itemView.findViewById(R.id.iv_profile_photo);
        plPseudo = itemView.findViewById(R.id.tv_user_profile_name);
        //plNom = findViewById(R.id.editTextTextProfilNom);
        //plPrenom = findViewById(R.id.EditTxtProfilPrenom);
        plMel = itemView.findViewById(R.id.editTextProfilEmail);
        plTel = itemView.findViewById(R.id.editTextTextProfilPhone);
        plAdress = itemView.findViewById(R.id.editTextProfilAdresse);
        plVille = itemView.findViewById(editTextProfilVille);
        plCpostal = itemView.findViewById(edittextProfilCpostal);
        plComp1 = itemView.findViewById(R.id.etprofilcomp1);
        plComp2 = itemView.findViewById(R.id.etprofilcomp2);
        plComp3 = itemView.findViewById(R.id.etprofilcomp3);
        plDescription = itemView.findViewById(R.id.et_profil_descriptionText);



        loadUserInformation();
        return itemView;

    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profilimg);
            }
        else {

              ImageView imageView = (ImageView) getActivity().findViewById(R.id.iv_profile_photo);
                imageView.setImageBitmap(BitmapFactory.decodeFile("R.drawable.unknown.jpg"));
            }




        }

    }

}

