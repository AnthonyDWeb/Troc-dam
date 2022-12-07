package com.dam.troc.search;

public class UserSearchModel {

    private String id, userImage, name, skill;

    public UserSearchModel() {}

    public UserSearchModel(String id, String userImage, String name, String skill) {
        this.id = id;
        this.userImage = userImage;
        this.name = name;
        this.skill = skill;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserImage() { return userImage; }
    public void setUserImage(String userImage) { this.userImage = userImage; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }



}
