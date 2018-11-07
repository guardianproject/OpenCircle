package com.circleof6.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class StatusUpdate {
    private Date date;
    private Contact contact;
    private int emoji;
    private String message;
    private boolean urgent;
    private String location;
    private List<StatusUpdateReply> replyList;

    // Has the user "seen" this update?
    private boolean seen;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public List<StatusUpdateReply> getReplyList() {
        if (replyList == null) {
            replyList = new ArrayList<>();
        }
        return replyList;
    }

    public void setReplyList(List<StatusUpdateReply> replyList) {
        this.replyList = replyList;
        if (this.replyList != null) {
            // Order by date
            Collections.sort(this.replyList, new Comparator<StatusUpdateReply>() {
                @Override
                public int compare(StatusUpdateReply o1, StatusUpdateReply o2) {
                    return (int)(o1.getDate().getTime() - o2.getDate().getTime());
                }
            });
        }
    }
}
