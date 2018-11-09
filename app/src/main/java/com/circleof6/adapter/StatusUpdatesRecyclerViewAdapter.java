package com.circleof6.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.StatusViewHolder;

import java.util.List;

/**
 * Created by N-Pex on 2018-11-08.
 */
public class StatusUpdatesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private Contact contact;
    private List<ContactStatusUpdate> updates;
    private StatusViewHolder.OnReplyListener onReplyListener;
    private boolean usingSeparateLayoutForFirstItem = true;
    private boolean showingQuickReplyButton = true;

    public StatusUpdatesRecyclerViewAdapter(Context context, Contact contact) {
        super();
        setHasStableIds(true);
        this.context = context;
        this.contact = contact;
        this.updates = contact.getStatus().getUpdates();
    }

    public void setOnReplyListener(StatusViewHolder.OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public boolean isUsingSeparateLayoutForFirstItem() {
        return usingSeparateLayoutForFirstItem;
    }

    public void setUsingSeparateLayoutForFirstItem(boolean usingSeparateLayoutForFirstItem) {
        this.usingSeparateLayoutForFirstItem = usingSeparateLayoutForFirstItem;
    }

    public boolean isShowingQuickReplyButton() {
        return showingQuickReplyButton;
    }

    public void setShowingQuickReplyButton(boolean showingQuickReplyButton) {
        this.showingQuickReplyButton = showingQuickReplyButton;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        if (updates == null || updates.size() == 0) {
            return 1; // Show the "no update" view
        }
        return updates.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isUsingSeparateLayoutForFirstItem() && position == 0) {
            return 1;
        }
        return 0;
    }

    //@Override
    //public long getItemId(int position) {
    //    return replies.get(position).hashCode();
    //}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate((viewType == 1) ? R.layout.status_page_item : R.layout.status_page_item_n, parent, false);
        return new StatusUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        StatusUpdateViewHolder viewHolder = (StatusUpdateViewHolder) holder;
        ContactStatusUpdate update = null;
        if (updates != null && updates.size() > 0) {
            update = updates.get(position);
        }
        viewHolder.bindModel(contact, update, isShowingQuickReplyButton() && (updates != null && position == 0));
    }

    private class StatusUpdateViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmoji;
        private TextView tvDate;
        private TextView tvStatus;
        private View layoutLocation;
        private TextView tvLocation;
        public View layoutQuickReply;

        public StatusUpdateViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvContactName);
            tvEmoji = view.findViewById(R.id.emoji);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvStatus);
            layoutLocation = view.findViewById(R.id.locationLayout);
            tvLocation = view.findViewById(R.id.tvLocation);
            layoutQuickReply = view.findViewById(R.id.layoutQuickReply);
        }

        public void bindModel(final Contact contact, ContactStatusUpdate update, boolean showQuickReply) {
            if (tvName != null) {
                tvName.setText(contact.getName());
            }
            if (tvEmoji != null) {
                if (update != null && update.getEmoji() != 0) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(Character.toChars(update.getEmoji()));
                    tvEmoji.setText(sb);
                    tvEmoji.setVisibility(View.VISIBLE);
                } else {
                    tvEmoji.setVisibility(View.INVISIBLE);
                }
            }
            if (update != null) {
                tvDate.setText(MethodsUtils.dateDiffDisplayString(update.getDate(), tvDate.getContext(), R.string.status_updated_ago_never, R.string.status_updated_ago_recently, R.string.status_updated_ago_minutes, R.string.status_updated_ago_minute, R.string.status_updated_ago_hours, R.string.status_updated_ago_hour, R.string.status_updated_ago_days, R.string.status_updated_ago_day));
                tvStatus.setText(update.getMessage());
                if (TextUtils.isEmpty(update.getLocation())) {
                    // No location given
                    layoutLocation.setVisibility(View.GONE);
                } else {
                    tvLocation.setText(update.getLocation());
                }
                if (layoutQuickReply != null) {
                    if (showQuickReply) {
                        layoutQuickReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onReplyListener != null) {
                                    onReplyListener.onReply(contact, layoutQuickReply);
                                }
                            }
                        });
                    } else {
                        layoutQuickReply.setVisibility(View.GONE);
                    }
                }
            } else {
                tvDate.setText(R.string.status_updated_ago_never);
                tvStatus.setVisibility(View.GONE);
                layoutLocation.setVisibility(View.GONE);
                if (layoutQuickReply != null) {
                    layoutQuickReply.setVisibility(View.GONE);
                }
            }
        }
    }
}
