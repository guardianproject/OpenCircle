package com.circleof6.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdateReply;
import com.circleof6.view.StatusViewHolder;

import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class RepliesViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<StatusUpdateReply> replies;

    public RepliesViewPagerAdapter(Context context, List<StatusUpdateReply> replies) {
        this.context = context;
        this.replies = replies;
    }

    @Override
    public int getCount() {
        if (this.replies == null) {
            return 0;
        }
        return this.replies.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = new View(container.getContext());
        view.setBackgroundColor(Color.BLUE);
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

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        int drawable = 0;
        switch (position) {
            case 0:
                drawable = R.drawable.ic_reply_call;
                title = "1";
                break;
            case 1:
                drawable = R.drawable.ic_reply_message;
                title = "2";
                break;
            default:
                drawable = R.drawable.ic_reply_whatsapp;
                title = "3";
                break;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
        try {
            Drawable d = ContextCompat.getDrawable(context, drawable);
            d.setBounds(5, 5, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sb;
    }
}
