package com.circleof6.model;

import android.text.TextUtils;

public class Contact {

    private int id;
    private String name;
    private String phoneNumber;
    private String photo;
    private boolean you;

    public Contact(int id, String name, String phoneNumber, String photo) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public  boolean isEmpty () {
        return TextUtils.isEmpty(phoneNumber);
    }

    public boolean isYou() {
        return you;
    }

    public void setYou(boolean you) {
        this.you = you;
    }
}
