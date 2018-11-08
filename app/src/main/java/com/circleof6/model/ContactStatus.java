package com.circleof6.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class ContactStatus {
    private List<ContactStatusUpdate> updates;
    private List<ContactStatusReply> replyList;

    /**
     *  Return an emoji representing our latest emoji update, or 0 if none set.
     */
    public int getEmoji() {
        if (updates != null) {
            for (ContactStatusUpdate update : updates) {
                if (update.getEmoji() != 0) {
                    return update.getEmoji();
                }
            }
        }
        return 0;
    }

    public boolean canReply() {
        if (updates != null) {
            for (ContactStatusUpdate update : updates) {
                if (!update.isEmojiOnlyUpdate()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasUnseenUpdates() {
        if (updates != null) {
            for (ContactStatusUpdate update : updates) {
                if (!update.isEmojiOnlyUpdate() && !update.isSeen()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ContactStatusUpdate getLatestUpdate(boolean ignoreEmojiOnlyUpdates) {
        if (updates != null) {
            for (ContactStatusUpdate update : updates) {
                if (!ignoreEmojiOnlyUpdates || !update.isEmojiOnlyUpdate()) {
                    return update;
                }
            }
        }
        return null;
    }

    public List<ContactStatusUpdate> getUpdates() {
        if (updates == null) {
            updates = new ArrayList<>();
        }
        return updates;
    }

    public void setUpdates(List<ContactStatusUpdate> updates) {
        this.updates = updates;
        if (this.updates != null) {
            // Order by date
            Collections.sort(this.updates, new Comparator<ContactStatusUpdate>() {
                @Override
                public int compare(ContactStatusUpdate o1, ContactStatusUpdate o2) {
                    return (int)(o1.getDate().getTime() - o2.getDate().getTime());
                }
            });
        }
    }

    public List<ContactStatusReply> getReplyList() {
        if (replyList == null) {
            replyList = new ArrayList<>();
        }
        return replyList;
    }

    public void setReplyList(List<ContactStatusReply> replyList) {
        this.replyList = replyList;
        if (this.replyList != null) {
            // Order by date
            Collections.sort(this.replyList, new Comparator<ContactStatusReply>() {
                @Override
                public int compare(ContactStatusReply o1, ContactStatusReply o2) {
                    return (int)(o1.getDate().getTime() - o2.getDate().getTime());
                }
            });
        }
    }

    public void addUpdate(ContactStatusUpdate update) {
        getUpdates().add(0, update);
    }
}
