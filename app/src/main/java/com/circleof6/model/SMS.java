package com.circleof6.model;

/**
 * Created by nickhargreaves on 7/5/16.
 */
public class SMS {

    private String label;
    private String text;

    public SMS(String label, String text){
        this.label = label;
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
