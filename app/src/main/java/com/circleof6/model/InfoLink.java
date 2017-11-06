package com.circleof6.model;

/**
 * Created by nickhargreaves on 7/5/16.
 */
public class InfoLink {
    private String name;
    private String link;
    private String remote_id;

    public InfoLink(String name, String remote_id, String link) {
        this.name = name;
        this.link = link;
        this.remote_id = remote_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemote_id() {
        return remote_id;
    }

    public void setRemote_id(String remote_id) {
        this.remote_id = remote_id;
    }
}
