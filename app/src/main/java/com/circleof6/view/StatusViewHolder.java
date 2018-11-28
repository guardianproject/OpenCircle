package com.circleof6.view;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class StatusViewHolder {

    public interface OnReplyListener {
        void onReply(Contact contact, View anchorButton);
        void onQuickReply(Contact contact, View anchorButton);
        void onReply(Contact contact, int emoji);
        void onUnreply(Contact contact, int emoji);
    }

    private OnReplyListener onReplyListener;

    public Contact contact; // Currently bound contact

    public final View itemView;
    private final ContactAvatarView avatarView;
    private final View layoutEmoji;
    private final TextView tvEmoji;
    private final RecyclerView rvStatusUpdates;
    public final FloatingActionButton fabReply;
    private final LinearLayout emojiReplyList;
    private final TabLayout repliesTitleStrip;
    private RepliesViewPagerAdapter repliesPagerAdapter;
    private final RecyclerView repliesPager;

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
        emojiReplyList = view.findViewById(R.id.emojiReplyList);
        repliesTitleStrip = view.findViewById(R.id.repliesTitleStrip);
        repliesPager = view.findViewById(R.id.repliesPager);
        repliesPager.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(repliesPager);
        MethodsUtils.connectTabLayoutAndRecyclerView(repliesPager, repliesTitleStrip);
    }

    public void populateWithContact(final Contact contact) {
        this.contact = contact;
        StatusUpdatesRecyclerViewAdapter adapter = new StatusUpdatesRecyclerViewAdapter(itemView.getContext(), contact);
        rvStatusUpdates.setAdapter(adapter);
        emojiReplyList.setVisibility(contact.isYou() ? View.GONE : View.VISIBLE);
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

        // Categorize replies
        Map<Object,List<ContactStatusReply>> categorizedReplies = new HashMap<>();
        for (ContactStatusReply reply : contact.getStatus().getReplyList()) {
            Object key = null;
            switch (reply.getType()) {
                case Call:
                case Message:
                case WhatsApp:
                    key = reply.getType();
                    break;
                case Emoji:
                    key = reply.getEmoji();
                    break;
            }
            if (key != null) {
                if (!categorizedReplies.containsKey(key)) {
                    categorizedReplies.put(key, new ArrayList<ContactStatusReply>());
                }
                categorizedReplies.get(key).add(reply);
            }
        }

        Object[] categorizedReplyKeys = categorizedReplies.keySet().toArray(new Object[0]);
        // Sort
        Arrays.sort(categorizedReplyKeys, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if ((o1 instanceof ContactStatusReply.ReplyType) && (o2 instanceof ContactStatusReply.ReplyType)) {
                    return ((ContactStatusReply.ReplyType)o1).ordinal() - ((ContactStatusReply.ReplyType)o2).ordinal();
                } else if (o1 instanceof ContactStatusReply.ReplyType) {
                    return -1;
                } else if (o2 instanceof ContactStatusReply.ReplyType) {
                    return 1;
                }
                return (Integer)o1 - (Integer)o2;
            }
        });

        int currentTab = repliesTitleStrip.getSelectedTabPosition();

        // Populate quick emoji reply list
        populateQuickEmojiReplyList(categorizedReplies, categorizedReplyKeys);

        // Populate replies title strip
        populateRepliesTitleStrip(categorizedReplies, categorizedReplyKeys);

        if (repliesPagerAdapter == null) {
            repliesPagerAdapter = new RepliesViewPagerAdapter(itemView.getContext(), categorizedReplies, categorizedReplyKeys);
            repliesPager.setAdapter(repliesPagerAdapter);
        } else {
            repliesPagerAdapter.updateData(categorizedReplies, categorizedReplyKeys);
        }
        if (currentTab >= 0 && currentTab < repliesTitleStrip.getTabCount()) {
            repliesTitleStrip.getTabAt(currentTab).select();
        }
    }

    public OnReplyListener getOnReplyListener() {
        return onReplyListener;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    private void populateRepliesTitleStrip(Map<Object,List<ContactStatusReply>> categorizedReplies, Object[] categorizedReplyKeys) {
        repliesTitleStrip.removeAllTabs();

        for (int i = 0; i < categorizedReplyKeys.length; i++) {
            Object key = categorizedReplyKeys[i];
            TabLayout.Tab tab = repliesTitleStrip.newTab();
            tab.setCustomView(R.layout.status_reply_tab_item);
            tab.setText(String.format("%d", categorizedReplies.get(key).size()));
            View icon = tab.getCustomView().findViewById(android.R.id.icon);
            TextView textualIcon = tab.getCustomView().findViewById(R.id.textual_icon);
            if (key == ContactStatusReply.ReplyType.Call) {
                tab.setIcon(R.drawable.ic_reply_call);
                textualIcon.setVisibility(View.GONE);
            } else if (key == ContactStatusReply.ReplyType.Message) {
                tab.setIcon(R.drawable.ic_reply_message);
                textualIcon.setVisibility(View.GONE);
            } else if (key == ContactStatusReply.ReplyType.WhatsApp) {
                tab.setIcon(R.drawable.ic_reply_whatsapp);
                textualIcon.setVisibility(View.GONE);
            } else {
                icon.setVisibility(View.GONE);
                StringBuffer sb = new StringBuffer();
                sb.append(Character.toChars((Integer)key));
                textualIcon.setText(sb);
                textualIcon.setVisibility(View.VISIBLE);
            }
            repliesTitleStrip.addTab(tab);
        }
    }

    private void populateQuickEmojiReplyList(Map<Object,List<ContactStatusReply>> categorizedReplies, Object[] categorizedReplyKeys) {

        // Clear all but the "add new emoji reply" button
        View quickReply = emojiReplyList.findViewById(R.id.layoutQuickReply);
        emojiReplyList.removeAllViews();
        emojiReplyList.addView(quickReply);
        quickReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnReplyListener() != null) {
                    getOnReplyListener().onQuickReply(contact, v);
                }
            }
        });
        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        for (int i = 0; i < categorizedReplyKeys.length; i++) {
            Object key = categorizedReplyKeys[i];
            if (!(key instanceof Integer)) {
                continue;
            }
            final Integer emoji = (Integer)key;

            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(emoji));

            // Create an entry in the quick reply emoji list
            View view = inflater.inflate(R.layout.status_reply_quick_item, emojiReplyList, false);
            TextView textualIcon = view.findViewById(R.id.textual_icon);
            textualIcon.setText(sb);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(String.format("%d", categorizedReplies.get(key).size()));

            // Has the user responded with this?!?
            boolean responded = false;
            for (ContactStatusReply reply : categorizedReplies.get(key)) {
                if (reply.getContact().isYou()) {
                    responded = true;
                    break;
                }
            }
            if (responded) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getOnReplyListener() != null) {
                            getOnReplyListener().onUnreply(contact, emoji);
                        }
                    }
                });
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getOnReplyListener() != null) {
                            getOnReplyListener().onReply(contact, emoji);
                        }
                    }
                });
            }
            view.setBackgroundResource(responded ? R.drawable.status_quick_reply_item_background_selected : R.drawable.status_quick_reply_item_background);
            emojiReplyList.addView(view, 0);
        }
    }


}
