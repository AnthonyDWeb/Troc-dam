package com.dam.troc.commons;


import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface Constants {

    //************ CONSTANTES FIREBASE ****************/
    // Auth
    FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    // User
    FirebaseUser CURRENT_USER = FIREBASE_AUTH.getCurrentUser();
    // Firestore
    @SuppressLint("StaticFieldLeak")
    FirebaseFirestore FIRESTORE_INSTANCE = FirebaseFirestore.getInstance();
    // Storage
    FirebaseStorage STORAGE_INSTANCE = FirebaseStorage.getInstance();

    //************ CONSTANTS POUR LE DOSSIER DE STORAGE ****************/
    // Lien vers le dossier de stockage des avatars
    String AVATARS_FOLDER = "avatars_user";

    //************ CONSTANTS AUTH ****************/
    String NEED_EMAIL = "E-mail obligatoire";
    String USED_EMAIL = "E-mail déjà utiliser";
    String INCORRECT_EMAIL = "E-mail non conforme";
    String NEED_PASSWORD = "Mot de passe obligatoire";
    String NO_MATCH_PASSWORD = "Mot de passe non identique";
    String ERROR_LENGTH_PASSWORD = "Minimum 6 charactere!";

    //************ CONSTANTS DES COLLECTIONS ET DE LEURS CHAMPS ****************/
    //-------------- Collection Users
    String ONLINE = "online";
    String AVATAR = "avatar";
    String ID = "id";
    String USERS = "Users";
    String NAME = "name";
    String EMAIL = "email";
    String DESCRIPTION = "description";
    String CITY = "city";
    String POSTAL_CODE = "postalCode";
    String ADDRESS = "address";
    String SKILLS = "skills";
    //-------------- end Users

    //-------------- Collection Friend request
    String FRIEND_REQUESTS = "FriendRequests";
    String REQUEST_TYPE = "request_type";
    String REQUEST_STATUS_SENT = "sent";
    String REQUEST_STATUS_RECEIVED = "received";
    StorageReference FILE_STORAGE = STORAGE_INSTANCE.getReference();
    //-------------- end Friend Request
}
