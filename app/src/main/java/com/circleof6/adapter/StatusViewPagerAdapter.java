package com.circleof6.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public interface OnAddResponseListener {
        void onAddResponse(StatusUpdate statusUpdate);
    }

    private OnAddResponseListener onAddResponseListener;
    private List<StatusUpdate> statusUpdates;

    public List<StatusUpdate> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(List<StatusUpdate> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    public OnAddResponseListener getOnAddResponseListener() {
        return onAddResponseListener;
    }

    public void setOnAddResponseListener(OnAddResponseListener onAddResponseListener) {
        this.onAddResponseListener = onAddResponseListener;
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

        final StatusUpdate statusUpdate = statusUpdates.get(position);
        Contact contact = statusUpdate.getContact();

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.status_page, container, false);


        ContactAvatarView avatarView = view.findViewById(R.id.avatarView);
        avatarView.setContact(contact);

        TextView tvName = view.findViewById(R.id.tvContactName);
        tvName.setText(contact.getName());

        TextView tvDate = view.findViewById(R.id.tvDate);
        tvDate.setText(MethodsUtils.dateDiffDisplayString(statusUpdate.getDate(), container.getContext(), R.string.status_updated_ago_never, R.string.status_updated_ago_recently, R.string.status_updated_ago_minutes, R.string.status_updated_ago_minute, R.string.status_updated_ago_hours, R.string.status_updated_ago_hour, R.string.status_updated_ago_days, R.string.status_updated_ago_day));

        TextView tvStatus = view.findViewById(R.id.tvStatus);
        tvStatus.setText(statusUpdate.getMessage());

        TextView tvLocation = view.findViewById(R.id.tvLocation);
        tvLocation.setText(statusUpdate.getLocation());

        View layoutAddResponse = view.findViewById(R.id.layoutAddResponse);
        layoutAddResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnAddResponseListener() != null) {
                    getOnAddResponseListener().onAddResponse(statusUpdate);
                }
            }
        });

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
