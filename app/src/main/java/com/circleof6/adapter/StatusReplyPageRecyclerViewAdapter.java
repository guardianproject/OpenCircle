package com.circleof6.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.StatusUpdateReply;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.ContactAvatarView;

import java.util.List;

/**
 * Created by N-Pex on 2018-11-05.
 */
public class StatusReplyPageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<StatusUpdateReply> replies;

    public StatusReplyPageRecyclerViewAdapter(Context context, List<StatusUpdateReply> replies) {
        super();
        setHasStableIds(true);
        this.context = context;
        this.replies = replies;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    @Override
    public long getItemId(int position) {
        return replies.get(position).hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_reply_page_item, parent, false);
        return new StatusReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        StatusReplyViewHolder viewHolder = (StatusReplyViewHolder) holder;
        StatusUpdateReply reply = replies.get(position);
        viewHolder.bindModel(reply);
    }

    private class StatusReplyViewHolder extends RecyclerView.ViewHolder {
        private final ContactAvatarView avatarView;
        private final TextView name;
        private final TextView date;

        StatusReplyViewHolder(View view) {
            super(view);
            avatarView = view.findViewById(R.id.avatarView);
            name = view.findViewById(R.id.tvContactName);
            date = view.findViewById(R.id.tvDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }

        void bindModel(final StatusUpdateReply reply) {
            avatarView.setContact(reply.getContact());
            name.setText(reply.getContact().getName());
            date.setText(MethodsUtils.dateDiffDisplayString(reply.getDate(), getContext(), R.string.status_updated_ago_never, R.string.status_updated_ago_recently, R.string.status_updated_ago_minutes, R.string.status_updated_ago_minute, R.string.status_updated_ago_hours, R.string.status_updated_ago_hour, R.string.status_updated_ago_days, R.string.status_updated_ago_day));
        }
    }
}
