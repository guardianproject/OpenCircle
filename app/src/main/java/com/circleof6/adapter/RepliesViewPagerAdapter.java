package com.circleof6.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circleof6.R;
import com.circleof6.model.ContactStatusReply;

import java.util.List;
import java.util.Map;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class RepliesViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Map<Object, List<ContactStatusReply>> categorizedReplies;
    private Object[] categorizedReplyKeys;

    public RepliesViewPagerAdapter(Context context, Map<Object,List<ContactStatusReply>> categorizedReplies, Object[] categorizedReplyKeys) {
        this.context = context;
        updateData(categorizedReplies, categorizedReplyKeys);
    }

    public void updateData(Map<Object,List<ContactStatusReply>> categorizedReplies, Object[] categorizedReplyKeys) {
        this.categorizedReplies = categorizedReplies;
        this.categorizedReplyKeys = categorizedReplyKeys;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_reply_page, viewGroup, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ReplyViewHolder) {
            Object key = categorizedReplyKeys[i];

            ReplyViewHolder vh = (ReplyViewHolder)viewHolder;
            vh.recyclerView.setAdapter(new StatusReplyPageRecyclerViewAdapter(context, categorizedReplies.get(key)));
        }
    }

    @Override
    public int getItemCount() {
        return this.categorizedReplies.size();
    }

    private class ReplyViewHolder extends RecyclerView.ViewHolder {
        public final RecyclerView recyclerView;

        ReplyViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.rvReplies);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        @Override
        public String toString() {
            return "ReplyViewHolder";
        }
    }
}
