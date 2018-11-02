package com.circleof6.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.RepliesViewPagerAdapter;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.view.ReplyDialog;
import com.circleof6.view.StatusViewHolder;

public class ContactStatusActivity extends AppCompatActivity implements StatusViewHolder.OnReplyListener {

    public static final String ARG_CONTACT_ID = "contact_id";
    private ViewPager repliesPager;
    private RepliesViewPagerAdapter repliesPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Contact contact = getContactFromIntent();
        setTitle(contact.getName());

        StatusViewHolder holder = new StatusViewHolder(findViewById(R.id.statusRoot));
        holder.populateWithContact(contact);
        holder.setOnReplyListener(this);

        FloatingActionButton fabReply = findViewById(R.id.fabReply);
        fabReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyDialog.showFromAnchor(v);
            }
        });

        StatusUpdate statusUpdate = CircleOf6Application.getInstance().getContactStatus(contact);
        if (statusUpdate != null) {
            repliesPagerAdapter = new RepliesViewPagerAdapter(this, statusUpdate.getReplyList());
        }
        repliesPager = findViewById(R.id.repliesPager);
        repliesPager.setAdapter(repliesPagerAdapter);
    }

    private Contact getContactFromIntent() {
        int id = getIntent().getIntExtra(ContactStatusActivity.ARG_CONTACT_ID, 0);
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
    public void onReply(Contact contact, View anchorButton) {
        ReplyDialog.showFromAnchor(anchorButton);
    }
}
