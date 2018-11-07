package com.circleof6.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.TagsRecyclerViewAdapter;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.view.ContactAvatarView;
import com.circleof6.dialog.QuickStatusDialog;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditStatusActivity extends AppCompatActivity implements QuickStatusDialog.QuickStatusDialogListener, TagsRecyclerViewAdapter.TagsRecyclerViewAdapterListener {

    public static final String ARG_CONTACT_ID = "contact_id";
    private RecyclerView recyclerViewTags;
    private TagsRecyclerViewAdapter recyclerViewTagsAdapter;
    private TextView tvStatusHint;
    private List<String> listStatusTags;
    private List<String> listActionTags;
    private EditText editStatus;
    private Contact contact;
    private int selectedEmoji;
    private CheckBox cbUrgent;
    private TextView avatarViewEmoji;
    private View avatarViewEmojiLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(EditStatusActivity.ARG_CONTACT_ID, 0);
        contact = CircleOf6Application.getInstance().getContactWithId(id);

        setTitle(contact.getName());

        ContactAvatarView avatarView = findViewById(R.id.avatarView);
        avatarView.setContact(contact);
        avatarViewEmoji = findViewById(R.id.avatarViewEmoji);
        avatarViewEmojiLayout = findViewById(R.id.avatarViewEmojiLayout);
        avatarViewEmojiLayout.setVisibility(View.GONE);
        TextView tvName = findViewById(R.id.tvContactName);
        tvName.setText(contact.getName());

        tvStatusHint = findViewById(R.id.tvStatus);

        editStatus = findViewById(R.id.editStatus);
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIBasedOnStatusText();
            }
        });
        editStatus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateUIBasedOnStatusText();
            }
        });

        listActionTags = Arrays.asList(getResources().getStringArray(R.array.edit_status_action_tags));
        listStatusTags = Arrays.asList(getResources().getStringArray(R.array.edit_status_tags));

        cbUrgent = findViewById(R.id.cbUrgent);

        final View quickStatusButton = findViewById(R.id.btnQuickStatus);
        quickStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickStatusDialog.showFromAnchor(quickStatusButton, EditStatusActivity.this);
            }
        });

        recyclerViewTags = findViewById(R.id.rvTags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTagsAdapter = new TagsRecyclerViewAdapter(this);
        recyclerViewTagsAdapter.setListener(this);
        recyclerViewTags.setAdapter(recyclerViewTagsAdapter);
        updateUIBasedOnStatusText();
    }

    @Override
    public void onQuickStatusSelected(int emoji) {
        selectedEmoji = emoji;
        if (selectedEmoji != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(selectedEmoji));
            avatarViewEmoji.setText(sb);
            avatarViewEmojiLayout.setVisibility(View.VISIBLE);
        } else {
            avatarViewEmojiLayout.setVisibility(View.GONE);
        }
    }

    private void updateUIBasedOnStatusText() {
        if (editStatus.getText().length() == 0) {
            tvStatusHint.setVisibility(editStatus.hasFocus() ? View.GONE : View.VISIBLE);
            recyclerViewTagsAdapter.setTags(listStatusTags);
        } else {
            tvStatusHint.setVisibility(View.GONE);
            recyclerViewTagsAdapter.setTags(listActionTags);
        }
    }

    @Override
    public void onTagClicked(String tag) {
        editStatus.append(tag);
        updateUIBasedOnStatusText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_status, menu);
        //menu.findItem(R.id.action_save).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_save:
                StatusUpdate statusUpdate = new StatusUpdate();
                statusUpdate.setContact(contact);
                statusUpdate.setSeen(true);
                statusUpdate.setMessage(editStatus.getText().toString());
                statusUpdate.setDate(new Date());
                statusUpdate.setEmoji(selectedEmoji);
                statusUpdate.setUrgent(cbUrgent.isChecked());
                CircleOf6Application.getInstance().setContactStatus(contact, statusUpdate);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
