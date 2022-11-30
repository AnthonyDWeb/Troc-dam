package com.dam.troc;

import android.net.Uri;

public class Profil {
    private Uri imgUrl;
    private String pseudo;
    private String nom, prenom;
    private String email, telephone;
    private String adresse, ville, cpostal;
    private String comp1, comp2, comp3, desc;

    //default constructor!!! Needed by firebase
    public Profil (){};


    // Full constructor
    public Profil(Uri imgUrl,String pseudo, String nom, String prenom, String email, String telephone, String adresse,
                  String ville, String cpostal, String comp1, String comp2, String comp3, String desc) {
        this.imgUrl = imgUrl;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.ville = ville;
        this.cpostal = cpostal;
        this.comp1 = comp1;
        this.comp2 = comp2;
        this.comp3 = comp3;
        this.desc = desc;
    }



    //getters

    public String getPseudo() {
        return pseudo;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getCpostal() {
        return cpostal;
    }

    public String getComp1() {
        return comp1;
    }

    public String getComp2() {
        return comp2;
    }

    public String getComp3() {
        return comp3;
    }

    public String getDesc() {
        return desc;
    }



    //Setters!!!

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCpostal(String cpostal) {
        this.cpostal = cpostal;
    }

    public void setComp1(String comp1) {
        this.comp1 = comp1;
    }

    public void setComp2(String comp2) {
        this.comp2 = comp2;
    }

    public void setComp3(String comp3) {
        this.comp3 = comp3;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
