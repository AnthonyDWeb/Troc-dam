package com.dam.troc;

public class Message {

    private String userMel;
    private String UserId;
    private String message;
    private String dateTime;
    private String professionId;


    public Message() {
    }

    public Message(String userMel, String userId, String professionId, String message, String dateTime) {
        this.userMel = userMel;
        this.UserId = userId;
        this.professionId = professionId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }


    public String getUserMel() {
        return userMel;
    }

    public void setUserMel(String userMel) {
        this.userMel = userMel;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
