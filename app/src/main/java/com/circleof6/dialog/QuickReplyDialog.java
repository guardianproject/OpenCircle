package com.circleof6.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.ui.Emoji;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class QuickReplyDialog {

    public interface QuickReplyDialogListener {
        void onQuickReplySelected(int emoji);
    }

    public static void showFromAnchor(final View rootView, final View anchor, final QuickReplyDialogListener listener) {
        try {
            if (anchor == null)
                return;

            // Show emoji keyboard popup!

            // Insert temporary edit view, needed by emoji popup library!
            final EmojiEditText editText = new EmojiEditText(anchor.getContext());
            ((ViewGroup)anchor.getParent()).addView(editText, 0, 0);
            final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                    .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                        @Override
                        public void onEmojiPopupDismiss() {
                            final InputMethodManager inputMethodManager = (InputMethodManager) anchor.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS, null);
                            ((ViewGroup)anchor.getParent()).removeView(editText);
                        }
                    })
                    .build(editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        // Ok, done
                        emojiPopup.dismiss();
                        int emoji = Character.codePointAt(s, 0);
                        if (listener != null) {
                            listener.onQuickReplySelected(emoji);
                        }
                        s.clear();
                    }
                }
            });
            emojiPopup.toggle();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
