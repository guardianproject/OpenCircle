package com.circleof6.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.ContactAvatarView;

import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class StatusViewPagerAdapter extends PagerAdapter {

    public interface OnReplyListener {
        void onReply(StatusUpdate statusUpdate);
    }

    private OnReplyListener onReplyListener;
    private List<Contact> contacts;

    public OnReplyListener getOnReplyListener() {
        return onReplyListener;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        if (this.contacts == null) {
            return 0;
        }
        return this.contacts.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final Contact contact = contacts.get(position);
        final StatusUpdate statusUpdate = CircleOf6Application.getInstance().getContactStatus(contact);

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.status_page, container, false);

        ContactAvatarView avatarView = view.findViewById(R.id.avatarView);
        avatarView.setContact(contact);

        View layoutEmoji = view.findViewById(R.id.avatarViewEmojiLayout);
        TextView tvEmoji = view.findViewById(R.id.avatarViewEmoji);
        TextView tvName = view.findViewById(R.id.tvContactName);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        View layoutLocation = view.findViewById(R.id.locationLayout);
        TextView tvLocation = view.findViewById(R.id.tvLocation);
        View layoutAddResponse = view.findViewById(R.id.layoutAddResponse);
        FloatingActionButton fabReply = view.findViewById(R.id.fabReply);

        if (statusUpdate != null && statusUpdate.getEmoji() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(statusUpdate.getEmoji()));
            tvEmoji.setText(sb);
        } else {
            layoutEmoji.setVisibility(View.GONE);
        }

        tvName.setText(contact.getName());

        if (statusUpdate != null) {
            tvDate.setText(MethodsUtils.dateDiffDisplayString(statusUpdate.getDate(), container.getContext(), R.string.status_updated_ago_never, R.string.status_updated_ago_recently, R.string.status_updated_ago_minutes, R.string.status_updated_ago_minute, R.string.status_updated_ago_hours, R.string.status_updated_ago_hour, R.string.status_updated_ago_days, R.string.status_updated_ago_day));
            tvStatus.setText(statusUpdate.getMessage());
            if (TextUtils.isEmpty(statusUpdate.getLocation())) {
                // No location given
                layoutLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setText(statusUpdate.getLocation());
            }
            layoutAddResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnReplyListener() != null) {
                        getOnReplyListener().onReply(statusUpdate);
                    }
                }
            });
            fabReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnReplyListener() != null) {
                        getOnReplyListener().onReply(statusUpdate);
                    }
                }
            });
        } else {
            tvDate.setText(R.string.status_updated_ago_never);
            tvStatus.setVisibility(View.GONE);
            layoutLocation.setVisibility(View.GONE);
            layoutAddResponse.setVisibility(View.GONE);
            fabReply.setVisibility(View.GONE);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (View) object == view;
    }
}
