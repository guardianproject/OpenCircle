package com.circleof6.model;


import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class CollegeCountry implements ParentObject{

    protected String collegeName;
    private String id;
    private int imageId;
    private String logoUrl;
    private List<Object> mChildrenList;
    private String welcomeMessage;
    private String customNumberMessage;
    private String termsAndConditions;

    public CollegeCountry(String collegeName, String id, String logoURL)
    {
        this.id = id;
        this.collegeName = collegeName;
        this.logoUrl = logoURL;
    }

    public CollegeCountry(String collegeName, String id, String logoURL,
                          String welcomeMessage, String customNumberMessage, String termsAndConditions)
    {
        this.id = id;
        this.collegeName = collegeName;
        this.logoUrl = logoURL;
        this.welcomeMessage = welcomeMessage;
        this.customNumberMessage = customNumberMessage;
        this.termsAndConditions = termsAndConditions;
    }

    public CollegeCountry(String collegeName, String id, int imageId)
    {
        this.id = id;
        this.collegeName = collegeName;
        this.imageId = imageId;
    }

    public String getCollegeName()
    {
        return collegeName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        this.mChildrenList = list;
    }

    public boolean hasCampuses() {
        return mChildrenList != null && ! mChildrenList.isEmpty();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCustomNumberMessage() {
        return customNumberMessage;
    }

    public void setCustomNumberMessage(String customNumberMessage) {
        this.customNumberMessage = customNumberMessage;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }
}
