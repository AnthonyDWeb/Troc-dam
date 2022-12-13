package com.dam.troc.profile;

import android.net.Uri;
import java.util.Arrays;
import java.util.List;

public class ProfileModel {
    private String imgUri;
    private String id, name, email, tel, address, city, postalCode, description;
    private List<String> skills;

    //default constructor!!! Needed by firebase
    public ProfileModel(){};
    public ProfileModel(String imgUri, String id, String name, String email, String tel, String address, String city, String postalCode, String description, List<String> skills) {
        this.imgUri = imgUri;
        this.id = id;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.description = description;
        this.skills = skills;
    }

    //getters
    public String getImgUri() { return imgUri; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTel() { return tel; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }
    public String getDescription() { return description; }
    public List<String> getSkills() { return skills; }

    //Setters!!!
    public void setImgUri(String imgUri) { this.imgUri = imgUri; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setTel(String tel) { this.tel = tel; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setDescription(String description) { this.description = description; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
