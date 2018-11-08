package com.circleof6.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.RepliesViewPagerAdapter;
import com.circleof6.dialog.QuickReplyDialog;
import com.circleof6.dialog.UpdateRemoveDialog;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatus;
import com.circleof6.model.ContactStatusReply;
import com.circleof6.util.MethodsUtils;
import com.circleof6.dialog.ReplyDialog;
import com.circleof6.view.StatusViewHolder;

import java.util.Date;

public class ContactStatusActivity extends AppCompatActivity implements StatusViewHolder.OnReplyListener {

    public static final String ARG_CONTACT_ID = "contact_id";
    private ViewPager repliesPager;
    private RepliesViewPagerAdapter repliesPagerAdapter;
    private Contact contact;
    private TabLayout repliesTitleStrip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(ContactStatusActivity.ARG_CONTACT_ID, 0);
        contact = CircleOf6Application.getInstance().getContactWithId(id);

        setTitle(contact.getName());

        StatusViewHolder holder = new StatusViewHolder(findViewById(R.id.statusRoot));
        holder.populateWithContact(contact);
        holder.setOnReplyListener(this);

        FloatingActionButton fabReply = findViewById(R.id.fabReply);
        if (contact.isYou()) {
            //holder.tvName.setVisibility(View.GONE); // No need to show name for ourselves
            //holder.layoutQuickReply.setVisibility(View.GONE);
            fabReply.setImageResource(R.drawable.ic_edit_white_32dp);
            fabReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateRemoveDialog.showFromAnchor(v, new UpdateRemoveDialog.UpdateRemoveDialogListener() {
                        @Override
                        public void onRemoveSelected() {
                            contact.setStatus(null);
                            CircleOf6Application.getInstance().statusUpdated(contact);
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
                        public void onReplySelected(ContactStatusReply.ReplyType replyType) {
                            //TODO
                        }
                    });
                }
            });
        }

        repliesTitleStrip = findViewById(R.id.repliesTitleStrip);

        if (!contact.isYou()) {
            CircleOf6Application.getInstance().setStatusSeen(contact);
        }
        repliesPagerAdapter = new RepliesViewPagerAdapter(this, repliesTitleStrip, contact.getStatus().getReplyList());
        repliesPager = findViewById(R.id.repliesPager);
        repliesPager.setAdapter(repliesPagerAdapter);
        MethodsUtils.connectTabLayoutAndViewPager(repliesPager, repliesTitleStrip);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(statusUpdateReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(statusUpdateReceiver, new IntentFilter(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED));
    }

    private BroadcastReceiver statusUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int contactId = intent.getIntExtra(Broadcasts.EXTRAS_CONTACT_ID, -1);
            if (contact != null && contactId == contact.getId()) {
                repliesPagerAdapter = new RepliesViewPagerAdapter(ContactStatusActivity.this, repliesTitleStrip, contact.getStatus().getReplyList());
                repliesPager.setAdapter(repliesPagerAdapter);
            }
        }
    };

    @Override
    public void onReply(Contact contact, View anchorButton) {
        QuickReplyDialog.showFromAnchor(repliesPager, anchorButton, new QuickReplyDialog.QuickReplyDialogListener() {
            @Override
            public void onQuickReplySelected(int emoji) {
                replyWithEmoji(emoji);
            }
        });
    }

    private void replyWithEmoji(int emoji) {
        ContactStatusReply reply = new ContactStatusReply();
        reply.setContact(CircleOf6Application.getInstance().getYouContact());
        reply.setDate(new Date());
        reply.setType(ContactStatusReply.ReplyType.Emoji);
        reply.setEmoji(emoji);
        CircleOf6Application.getInstance().sendReply(contact, reply);
    }
}
