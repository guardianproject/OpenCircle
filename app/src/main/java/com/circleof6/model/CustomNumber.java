package com.circleof6.model;

/**
 * Created by nickhargreaves on 7/5/16.
 */
public class CustomNumber {
    private String name;
    private String number;
    private String remote_id;

    public CustomNumber(String name, String remote_id, String number) {
        this.name = name;
        this.number = number;
        this.remote_id = remote_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String getRemote_id() {
        return remote_id;
    }

    public void setRemote_id(String remote_id) {
        this.remote_id = remote_id;
    }
}
