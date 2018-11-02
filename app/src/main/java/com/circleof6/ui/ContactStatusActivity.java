package com.circleof6.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.view.ReplyDialog;
import com.circleof6.view.StatusViewHolder;

public class ContactStatusActivity extends AppCompatActivity implements StatusViewHolder.OnReplyListener {

    public static final String ARG_CONTACT_ID = "contact_id";

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
