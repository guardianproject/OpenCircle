package com.circleof6.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.ui.NoUnderlineSpan;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.StatusViewHolder;

import java.util.List;

/**
 * Created by N-Pex on 2018-11-08.
 */
public class StatusUpdatesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private Contact contact;
    private StatusViewHolder.OnReplyListener onReplyListener;
    private boolean usingSeparateLayoutForFirstItem = true;
    private boolean showingQuickReplyButton = true;
    private boolean showAll = false;

    public StatusUpdatesRecyclerViewAdapter(Context context, Contact contact) {
        super();
        setHasStableIds(true);
        this.context = context;
        this.contact = contact;
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
        if (contact.getStatus().getUpdates() == null || contact.getStatus().getUpdates().size() == 0) {
            return 1; // Show the "no update" view
        }
        int num = showAll ? contact.getStatus().getUpdates().size() : 1;
        if (contact.getStatus().getUpdates().size() > 1) {
            // Add a view for "show more/show less"
            num += 1;
        }
        return num;
    }

    @Override
    public int getItemViewType(int position) {
        if (isUsingSeparateLayoutForFirstItem() && position == 0) {
            return 1;
        }
        if (contact.getStatus().getUpdates().size() > 1 && ((showAll && position == contact.getStatus().getUpdates().size()) || (!showAll && position == 1) )) {
            return 2; // The show more/show less view type
        }
        return 0;
    }

    //@Override
    //public long getItemId(int position) {
    //    return replies.get(position).hashCode();
    //}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_page_item_show_more, parent, false);
            return new ShowMoreViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate((viewType == 1) ? R.layout.status_page_item : R.layout.status_page_item_n, parent, false);
        return new StatusUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShowMoreViewHolder) {
            ShowMoreViewHolder vh = (ShowMoreViewHolder) holder;
            vh.tv.setText(showAll ? R.string.show_less : R.string.show_more);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAll = !showAll;
                    notifyDataSetChanged();
                }
            });
            return;
        }

        StatusUpdateViewHolder viewHolder = (StatusUpdateViewHolder) holder;
        ContactStatusUpdate update = null;
        if (contact.getStatus().getUpdates() != null && contact.getStatus().getUpdates().size() > 0) {
            update = contact.getStatus().getUpdates().get(position);
        }
        viewHolder.bindModel(contact, update, isShowingQuickReplyButton() && (contact.getStatus().getUpdates() != null && position == 0));
    }

    private class ShowMoreViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ShowMoreViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.text);
        }
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
            tvStatus.setAutoLinkMask(Linkify.ALL);
            tvStatus.setLinkTextColor(ContextCompat.getColor(view.getContext(), R.color.link_color));
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

                // Remove underline in links
                if (tvStatus.getText() instanceof Spannable) {
                    Spannable s = (Spannable)tvStatus.getText();
                    for (Object span : s.getSpans(0, s.length(), NoUnderlineSpan.class)) {
                        s.removeSpan(span);
                    }
                    for (Object span : s.getSpans(0, s.length(), ClickableSpan.class)) {
                        s.setSpan(new NoUnderlineSpan(), s.getSpanStart(span), s.getSpanEnd(span), s.getSpanFlags(span));
                    }
                }

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
                                    onReplyListener.onQuickReply(contact, layoutQuickReply);
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
