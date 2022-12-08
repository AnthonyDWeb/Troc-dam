package com.dam.troc.search;

import java.util.List;

public class UserSearchModel {

    private String id, userImage, name;
    private List<String> skills;

    public UserSearchModel() {}
    public UserSearchModel(String id, String userImage, String name, List<String> skills) {
        this.id = id;
        this.userImage = userImage;
        this.name = name;
        this.skills = skills;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserImage() { return userImage; }
    public void setUserImage(String userImage) { this.userImage = userImage; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }


}
