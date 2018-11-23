package com.circleof6.view;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.RepliesViewPagerAdapter;
import com.circleof6.adapter.StatusUpdatesRecyclerViewAdapter;
import com.circleof6.dialog.ReplyDialog;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatus;
import com.circleof6.model.ContactStatusReply;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.ui.ContactStatusActivity;
import com.circleof6.util.MethodsUtils;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class StatusViewHolder {

    public interface OnReplyListener {
        void onReply(Contact contact, View anchorButton);
        void onQuickReply(Contact contact, View anchorButton);
    }

    private OnReplyListener onReplyListener;

    public Contact contact; // Currently bound contact

    public final View itemView;
    private final ContactAvatarView avatarView;
    private final View layoutEmoji;
    private final TextView tvEmoji;
    private final RecyclerView rvStatusUpdates;
    public final FloatingActionButton fabReply;
    private final TabLayout repliesTitleStrip;
    private RepliesViewPagerAdapter repliesPagerAdapter;
    private final ViewPager repliesPager;

    public StatusViewHolder(View view) {
        itemView = view;
        avatarView = view.findViewById(R.id.avatarView);
        avatarView.setIgnoringSeenStatus(true);
        layoutEmoji = view.findViewById(R.id.avatarViewEmojiLayout);
        tvEmoji = view.findViewById(R.id.avatarViewEmoji);
        rvStatusUpdates = view.findViewById(R.id.rvStatusUpdates);
        rvStatusUpdates.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        fabReply = view.findViewById(R.id.fabReply);
        fabReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact != null && getOnReplyListener() != null) {
                    getOnReplyListener().onReply(contact, v);
                }
            }
        });
        repliesTitleStrip = view.findViewById(R.id.repliesTitleStrip);
        repliesPager = view.findViewById(R.id.repliesPager);
        MethodsUtils.connectTabLayoutAndViewPager(repliesPager, repliesTitleStrip);
    }

    public void populateWithContact(final Contact contact) {
        this.contact = contact;
        StatusUpdatesRecyclerViewAdapter adapter = new StatusUpdatesRecyclerViewAdapter(itemView.getContext(), contact);
        rvStatusUpdates.setAdapter(adapter);
        adapter.setShowingQuickReplyButton(!contact.isYou());
        adapter.setOnReplyListener(onReplyListener);
        refresh();
    }

    public void refresh() {
        avatarView.setContact(contact);
        if (contact.getStatus().getEmoji() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(contact.getStatus().getEmoji()));
            tvEmoji.setText(sb);
        } else {
            layoutEmoji.setVisibility(View.GONE);
        }
        rvStatusUpdates.getAdapter().notifyDataSetChanged();
        repliesPagerAdapter = new RepliesViewPagerAdapter(itemView.getContext(), repliesTitleStrip, contact.getStatus().getReplyList());
        repliesPager.setAdapter(repliesPagerAdapter);
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
