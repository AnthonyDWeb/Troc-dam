package com.dam.troc.commons;


import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface Constants {

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
    String TEL = "tel";
    String DESCRIPTION = "description";
    String CITY = "city";
    String POSTAL_CODE = "postalCode";
    String ADDRESS = "address";
    String SKILLS = "skills";
    //-------------- end Users
    String JOBS = "Jobs";
    String SKILLNAME = "skillname";
    String NEEDED = "Champ obligatoire";


    //************ CONSTANTES FIREBASE ****************/
    // Auth
    FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    // FirebaseAuth FIREBASE_AUTH = FIREBASE_AUTH_INITIALISATION;
    // User
    FirebaseUser CURRENT_USER = FIREBASE_AUTH.getCurrentUser();
    // Firestore
    @SuppressLint("StaticFieldLeak")
    FirebaseFirestore FIRESTORE_INSTANCE = FirebaseFirestore.getInstance();
    CollectionReference FIRESTORE_INSTANCE_USERS = FIRESTORE_INSTANCE.collection(USERS);
    CollectionReference FIRESTORE_INSTANCE_JOBS = FIRESTORE_INSTANCE.collection(JOBS);
    // Storage
    FirebaseStorage STORAGE_INSTANCE = FirebaseStorage.getInstance();

    //************ CONSTANTS POUR LE DOSSIER DE STORAGE ****************/
    // Lien vers le dossier de stockage des avatars
    String AVATARS_FOLDER = "avatars_user";

    //-------------- Collection Friend request
    String FRIEND_REQUESTS = "FriendRequests";
    String REQUEST_TYPE = "request_type";
    String REQUEST_STATUS_SENT = "sent";
    String REQUEST_STATUS_RECEIVED = "received";
    StorageReference FILE_STORAGE = STORAGE_INSTANCE.getReference();
    //-------------- end Friend Request
}
