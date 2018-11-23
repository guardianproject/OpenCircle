package com.circleof6.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.view.StatusViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class StatusViewPagerAdapter extends PagerAdapter {

    private StatusViewHolder.OnReplyListener onReplyListener;
    private List<Contact> contacts;

    /**
     * Stores a mapping of contact->StatusViewHolder, so we can refresh particular pages when a contact changes status.
     */
    private Map<Contact, StatusViewHolder> viewHolderMap;

    public StatusViewPagerAdapter() {
        super();
        viewHolderMap = new HashMap<>();
    }

    public StatusViewHolder.OnReplyListener getOnReplyListener() {
        return onReplyListener;
    }

    public void setOnReplyListener(StatusViewHolder.OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        StatusViewHolder holder = (StatusViewHolder)object;
        return contacts.indexOf(holder.contact);
    }

    @Override
    public int getCount() {
        if (this.contacts == null) {
            return 0;
        }
        return this.contacts.size();
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Contact contact = contacts.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.status_page, container, false);
        StatusViewHolder holder = new StatusViewHolder(view);
        holder.populateWithContact(contact);
        holder.setOnReplyListener(getOnReplyListener());
        container.addView(view);
        viewHolderMap.put(contact, holder);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        StatusViewHolder holder = (StatusViewHolder)object;
        container.removeView(holder.itemView);
        viewHolderMap.remove(holder.contact);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((StatusViewHolder) object).itemView == view;
    }

    public void onContactStatusUpdated(Contact contact) {
        StatusViewHolder holder = viewHolderMap.get(contact);
        if (holder != null) {
            // Refresh
            holder.refresh();
        }
    }
}
