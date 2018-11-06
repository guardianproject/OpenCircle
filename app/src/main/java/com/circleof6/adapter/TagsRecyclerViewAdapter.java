package com.circleof6.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;

import java.util.List;

/**
 * Created by N-Pex on 2018-11-06.
 */
public class TagsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface TagsRecyclerViewAdapterListener {
        void onTagClicked(String tag);
    }

    private final Context context;
    private List<String> tags;
    private TagsRecyclerViewAdapterListener listener;

    public TagsRecyclerViewAdapter(Context context) {
        super();
        setHasStableIds(true);
        this.context = context;
    }

    public void setListener(TagsRecyclerViewAdapterListener listener) {
        this.listener = listener;
    }

    public void setTags(List<String> tags) {
        if (this.tags != tags) {
            this.tags = tags;
            notifyDataSetChanged();
        }
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        if (tags == null) {
            return 0;
        }
        return tags.size();
    }

    @Override
    public long getItemId(int position) {
        return tags.get(position).hashCode();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_tag_item, parent, false);
        return new StatusTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StatusTagViewHolder viewHolder = (StatusTagViewHolder) holder;
        String tag = tags.get(position);
        viewHolder.bindModel(tag);
    }

    private class StatusTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tagView;
        private String tag;

        StatusTagViewHolder(View view) {
            super(view);
            tagView = (TextView)view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tag + "'";
        }

        void bindModel(final String tag) {
            itemView.setOnClickListener(this);
            this.tag = tag;
            tagView.setText(tag);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onTagClicked(this.tag);
            }
        }
    }
}
