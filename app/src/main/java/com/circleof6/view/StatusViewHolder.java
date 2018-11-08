package com.circleof6.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.StatusUpdatesRecyclerViewAdapter;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatus;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.util.MethodsUtils;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class StatusViewHolder {

    public interface OnReplyListener {
        void onReply(Contact contact, View anchorButton);
    }

    private OnReplyListener onReplyListener;

    public Contact contact; // Currently bound contact

    public View itemView;
    private ContactAvatarView avatarView;
    private View layoutEmoji;
    private TextView tvEmoji;
    private RecyclerView rvStatusUpdates;

    public StatusViewHolder(View view) {
        itemView = view;
        avatarView = view.findViewById(R.id.avatarView);
        avatarView.setIgnoringSeenStatus(true);
        layoutEmoji = view.findViewById(R.id.avatarViewEmojiLayout);
        tvEmoji = view.findViewById(R.id.avatarViewEmoji);
        rvStatusUpdates = view.findViewById(R.id.rvStatusUpdates);
        rvStatusUpdates.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void populateWithContact(final Contact contact) {
        this.contact = contact;

        avatarView.setContact(contact);
        if (contact.getStatus().getEmoji() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(contact.getStatus().getEmoji()));
            tvEmoji.setText(sb);
        } else {
            layoutEmoji.setVisibility(View.GONE);
        }

        rvStatusUpdates.setAdapter(new StatusUpdatesRecyclerViewAdapter(itemView.getContext(), contact));
        ((StatusUpdatesRecyclerViewAdapter)rvStatusUpdates.getAdapter()).setOnReplyListener(onReplyListener);
    }

    public OnReplyListener getOnReplyListener() {
        return onReplyListener;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
        if (rvStatusUpdates != null && rvStatusUpdates.getAdapter() != null) {
            ((StatusUpdatesRecyclerViewAdapter) rvStatusUpdates.getAdapter()).setOnReplyListener(onReplyListener);
        }
    }
}
