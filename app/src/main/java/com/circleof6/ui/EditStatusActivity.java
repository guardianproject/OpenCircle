package com.circleof6.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.adapter.TagsRecyclerViewAdapter;
import com.circleof6.model.Contact;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.view.ContactAvatarView;
import com.circleof6.dialog.QuickStatusDialog;

import java.util.Arrays;
import java.util.List;

public class EditStatusActivity extends AppCompatActivity implements QuickStatusDialog.QuickStatusDialogListener, TagsRecyclerViewAdapter.TagsRecyclerViewAdapterListener {

    public static final String ARG_CONTACT_ID = "contact_id";
    private RecyclerView recyclerViewTags;
    private TagsRecyclerViewAdapter recyclerViewTagsAdapter;
    private TextView tvStatusHint;
    private List<String> listStatusTags;
    private List<String> listActionTags;
    private EditText editStatus;

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
}
