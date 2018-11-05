package com.circleof6.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.RepliesViewPagerAdapter;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.ContactAvatarView;
import com.circleof6.view.QuickStatusDialog;
import com.circleof6.view.ReplyDialog;
import com.circleof6.view.StatusViewHolder;

public class EditStatusActivity extends AppCompatActivity implements QuickStatusDialog.QuickStatusDialogListener {

    public static final String ARG_CONTACT_ID = "contact_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Contact contact = getContactFromIntent();
        setTitle(contact.getName());

        ContactAvatarView avatarView = findViewById(R.id.avatarView);
        avatarView.setContact(contact);
        TextView tvName = findViewById(R.id.tvContactName);
        tvName.setText(contact.getName());

        final View quickStatusButton = findViewById(R.id.btnQuickStatus);
        quickStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickStatusDialog.showFromAnchor(quickStatusButton, EditStatusActivity.this);
            }
        });
    }

    private Contact getContactFromIntent() {
        int id = getIntent().getIntExtra(EditStatusActivity.ARG_CONTACT_ID, 0);
        if (id > 0) {
            String name = AppPreferences.getInstance(this).getNameContact(id);
            String phone = AppPreferences.getInstance(this).getPhoneContact(id);
            String photo = AppPreferences.getInstance(this).getPhotoContact(id);
            Contact contact = new Contact(id, name, phone, photo);
            return contact;
        }
        return null;
    }

    @Override
    public void onQuickStatusSelected() {

    }

    @Override
    public void onQuickStatusCanceled() {

    }
}
