package com.circleof6.model;


public class CampusLang
{
    String campusName;
    String id;
    private String welcomeMessage;
    private String customNumberMessage;
    private String termsAndConditions;
    private String level;
    private String logo;
    private String parent;

    public CampusLang(String id, String name, String logo, String welcome_message, String custom_number_message, String terms_of_service, String level, String parent) {
        this.id = id;
        this.campusName = name;
        this.logo = logo;
        this.welcomeMessage = welcome_message;
        this.customNumberMessage = custom_number_message;
        this.termsAndConditions = terms_of_service;
        this.level = level;
        this.parent = parent;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public enum Lang
    {
        English,
        Hindi
    }

    public CampusLang(String campusName, String id)
    {
        this.campusName = campusName;
        this.id = id;
    }

    public CampusLang(String campusName, String id,
                          String welcomeMessage, String customNumberMessage, String termsAndConditions)
    {
        this.id = id;
        this.campusName = campusName;
        this.welcomeMessage = welcomeMessage;
        this.customNumberMessage = customNumberMessage;
        this.termsAndConditions = termsAndConditions;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public String getCampusName()
    {
        return campusName;
    }
}
