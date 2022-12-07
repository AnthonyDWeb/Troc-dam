package com.dam.troc.profile;

import android.net.Uri;
import java.util.Arrays;

public class ProfileModel {

    private Uri imgUri;
    private String id, username, email, tel, address, city, postalCode, description;
    private Arrays skills;

    //default constructor!!! Needed by firebase
    public ProfileModel(){};

    // Full constructor
    public ProfileModel(Uri imgUri, String id, String username, String email, String tel, String address, String city, String postalCode, String description, Arrays skills) {
        this.imgUri = imgUri;
        this.id = id;
        this.username = username;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.description = description;
        this.skills = skills;
    }

    //getters
    public Uri getImgUri() { return imgUri; }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getTel() { return tel; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }
    public String getDescription() { return description; }
    public Arrays getSkills() { return skills; }

    //Setters!!!
    public void setImgUri(Uri imgUri) { this.imgUri = imgUri; }
    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setTel(String tel) { this.tel = tel; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setDescription(String description) { this.description = description; }
    public void setSkills(Arrays skills) { this.skills = skills; }

}
