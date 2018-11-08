package com.circleof6.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.Contact;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class ContactView extends FrameLayout {

    private Contact contact;
    private TextView tvName;
    private ContactAvatarView avatarView;

    public ContactView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public ContactView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ContactView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContactView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.contact_view, this, true);
        tvName = findViewById(R.id.name);
        avatarView = findViewById(R.id.avatarView);
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        tvName.setText(contact.getName());
        avatarView.setContact(contact);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        avatarView.setOnClickListener(l);
    }
}
