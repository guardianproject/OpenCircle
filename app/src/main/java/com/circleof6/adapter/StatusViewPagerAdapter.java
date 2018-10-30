package com.circleof6.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circleof6.R;
import com.circleof6.model.StatusUpdate;
import com.circleof6.view.ContactAvatarView;

import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class StatusViewPagerAdapter extends PagerAdapter {

    private List<StatusUpdate> statusUpdates;

    public List<StatusUpdate> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(List<StatusUpdate> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    @Override
    public int getCount() {
        if (this.statusUpdates == null) {
            return 0;
        }
        return this.statusUpdates.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        StatusUpdate statusUpdate = statusUpdates.get(position);

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.status_page, container, false);

        ContactAvatarView avatarView = view.findViewById(R.id.avatarView);
        avatarView.setContact(statusUpdate.getContact());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (View)object == view;
    }
}
