package com.circleof6.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.RepliesViewPagerAdapter;
import com.circleof6.dialog.PopupDialog;
import com.circleof6.dialog.UpdateRemoveDialog;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.model.StatusUpdateReply;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.MethodsUtils;
import com.circleof6.dialog.ReplyDialog;
import com.circleof6.view.StatusViewHolder;

import java.util.ArrayList;
import java.util.List;

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

        int id = getIntent().getIntExtra(ContactStatusActivity.ARG_CONTACT_ID, 0);
        final Contact contact = CircleOf6Application.getInstance().getContactWithId(id);

        setTitle(contact.getName());

        StatusViewHolder holder = new StatusViewHolder(findViewById(R.id.statusRoot));
        holder.populateWithContact(contact);
        holder.setOnReplyListener(this);

        FloatingActionButton fabReply = findViewById(R.id.fabReply);
        if (contact.isYou()) {
            holder.tvName.setVisibility(View.GONE); // No need to show name for ourselves
            holder.layoutQuickReply.setVisibility(View.GONE);
            fabReply.setImageResource(R.drawable.ic_edit_white_32dp);
            fabReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateRemoveDialog.showFromAnchor(v, new UpdateRemoveDialog.UpdateRemoveDialogListener() {
                        @Override
                        public void onRemoveSelected() {
                            CircleOf6Application.getInstance().setContactStatus(contact, null);
                            Toast.makeText(ContactStatusActivity.this, R.string.status_removed, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onUpdateSelected() {
                            Intent intent = new Intent(ContactStatusActivity.this, EditStatusActivity.class);
                            intent.putExtra(EditStatusActivity.ARG_CONTACT_ID, contact.getId());
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        } else {
            fabReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReplyDialog.showFromAnchor(v, new ReplyDialog.ReplyDialogListener() {
                        @Override
                        public void onReplySelected(StatusUpdateReply.ReplyType replyType) {
                            //TODO
                        }
                    });
                }
            });
        }

        TabLayout repliesTitleStrip = findViewById(R.id.repliesTitleStrip);

        StatusUpdate statusUpdate = CircleOf6Application.getInstance().getContactStatus(contact);
        if (statusUpdate != null) {
            if (!contact.isYou()) {
                CircleOf6Application.getInstance().setStatusSeen(statusUpdate);
            }
            repliesPagerAdapter = new RepliesViewPagerAdapter(this, repliesTitleStrip, statusUpdate.getReplyList());
        }
        repliesPager = findViewById(R.id.repliesPager);
        repliesPager.setAdapter(repliesPagerAdapter);
        MethodsUtils.connectTabLayoutAndViewPager(repliesPager, repliesTitleStrip);
    }

    @Override
    public void onReply(Contact contact, View anchorButton) {
        ReplyDialog.showFromAnchor(anchorButton, new ReplyDialog.ReplyDialogListener() {
            @Override
            public void onReplySelected(StatusUpdateReply.ReplyType replyType) {
                //TODO
            }
        });
    }
}
