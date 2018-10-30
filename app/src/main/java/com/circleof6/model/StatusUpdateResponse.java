package com.circleof6.model;

import java.util.Date;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class StatusUpdateResponse {

    public enum ResponseType {
        Call,
        Message,
        Emoji;

        @Override
        public String toString() {
            switch (this) {
                default:
                    return super.toString();
            }
        }
    }

    private Date date;
    private ResponseType type;
    private int emoji;
    private String message;
    private Contact contact;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
