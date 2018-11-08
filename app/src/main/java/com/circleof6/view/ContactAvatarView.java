package com.circleof6.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatus;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.ui.Broadcasts;
import com.circleof6.view.util.ConstantsView;
import com.circleof6.view.util.DrawUtils;


/**
 * Created by N-Pex on 2018-10-30.
 */
public class ContactAvatarView extends RoundFrameLayout {

    private Contact contact;

    private LinearGradient shaderNormal;
    private LinearGradient shaderUnread;
    private LinearGradient shaderUnreadUrgent;
    private boolean ignoringSeenStatus;

    private ImageView imageView;

    public ContactAvatarView(Context context) {
        super(context);
        init(null);
    }

    public ContactAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ContactAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

    }

    public boolean isIgnoringSeenStatus() {
        return ignoringSeenStatus;
    }

    public void setIgnoringSeenStatus(boolean ignoringSeenStatus) {
        this.ignoringSeenStatus = ignoringSeenStatus;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = findViewById(R.id.imageView);
        if (imageView == null) {
            imageView = new ImageView(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
            addView(imageView);
        }

        if (isInEditMode()) {
            imageView.setImageResource(R.drawable.ic_add);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusUpdateReceiver, new IntentFilter(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED));
    }

    @Override
    protected void onDetachedFromWindow() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(statusUpdateReceiver);
        super.onDetachedFromWindow();
    }

    private BroadcastReceiver statusUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int contactId = intent.getIntExtra(Broadcasts.EXTRAS_CONTACT_ID, -1);
            if (contact != null && contact.getId() == contactId) {
                invalidate();
            }
        }
    };

    public void setContact(Contact contact) {
        this.contact = contact;
        if (this.contact == null || (!this.contact.isYou() && TextUtils.isEmpty(this.contact.getPhoneNumber()))) {
            // Show the add!
            imageView.setImageResource(R.drawable.ic_add);
        } else if (contact.getPhoto() == null || contact.getPhoto().equals(ConstantsView.DEFAULT_PHOTO)) {
            // Show default image
            imageView.setImageResource(R.drawable.contact_icon);
        } else {
            imageView.setImageBitmap(DrawUtils.getBitmapFromPath(contact.getPhoto()));
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shaderNormal = buildShader(ContextCompat.getColor(getContext(), R.color.avatarReadStart), ContextCompat.getColor(getContext(), R.color.avatarReadEnd), w, h);
        shaderUnreadUrgent = buildShader(ContextCompat.getColor(getContext(), R.color.avatarUnreadStart), ContextCompat.getColor(getContext(), R.color.avatarUnreadEnd), w, h);
        shaderUnread = buildShader(ContextCompat.getColor(getContext(), R.color.avatarUnreadUrgentStart), ContextCompat.getColor(getContext(), R.color.avatarUnreadUrgentEnd), w, h);
    }

    private LinearGradient buildShader(int colStart, int colEnd, int width, int height) {
        return new LinearGradient(0,0,width, 0,new int[] { colStart, colEnd }, new float[] { 0, 1}, Shader.TileMode.CLAMP);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.contact != null && !TextUtils.isEmpty(contact.getPhoneNumber())) {
            ContactStatus status = this.contact.getStatus();
            if (status.canReply()) {
                if (status.hasUnseenUpdates() || isIgnoringSeenStatus()) {
                    ContactStatusUpdate latestUpdate = status.getLatestUpdate(true);
                    if (latestUpdate != null && latestUpdate.isUrgent()) {
                        setBorderShader(shaderUnreadUrgent);
                    } else {
                        setBorderShader(shaderUnread);
                    }
                } else {
                    setBorderShader(shaderNormal);
                }
            } else {
                setBorderColor(Color.TRANSPARENT);
            }
        } else if (isInEditMode()) {
            setBorderShader(shaderNormal);
        }
        super.onDraw(canvas);
    }
}
