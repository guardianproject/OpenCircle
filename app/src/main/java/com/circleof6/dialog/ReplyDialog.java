package com.circleof6.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.ContactStatusReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class ReplyDialog {

    public interface ReplyDialogListener {
        void onReplySelected(ContactStatusReply.ReplyType replyType);
    }

    private static class ReplyEntry {
        public ContactStatusReply.ReplyType type;
        public int color;
        public int resIdTitle;
        public int resIdIcon;

        public ReplyEntry(ContactStatusReply.ReplyType type, int color, int resIdTitle, int resIdIcon) {
            this.type = type;
            this.color = color;
            this.resIdTitle = resIdTitle;
            this.resIdIcon = resIdIcon;
        }
    }

    public static void showFromAnchor(final View anchor, final ReplyDialogListener listener) {
        try {
            if (anchor == null)
                return;

            final Context context = anchor.getContext();
            final List<ReplyEntry> entries = new ArrayList<>();
            entries.add(new ReplyEntry(ContactStatusReply.ReplyType.Call, 0xfff8e71c, R.string.reply_call, R.drawable.ic_reply_call));
            entries.add(new ReplyEntry(ContactStatusReply.ReplyType.Message, 0xff3384ff, R.string.reply_message, R.drawable.ic_reply_message));
            entries.add(new ReplyEntry(ContactStatusReply.ReplyType.WhatsApp, 0xff23b180, R.string.reply_whatsapp, R.drawable.ic_reply_whatsapp));

            final ArrayAdapter<ReplyEntry> adapter = new ArrayAdapter<ReplyEntry>(context, R.layout.quick_status_popup_item, 0,
                    entries.toArray(new ReplyEntry[0])) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.reply_popup_item, parent, false);
                    }
                    ReplyEntry entry = entries.get(position);

                    TextView title = view.findViewById(R.id.title);
                    ImageView image = view.findViewById(R.id.image);
                    View roundFrame = view.findViewById(R.id.roundFrame);
                    title.setText(entry.resIdTitle);
                    roundFrame.setBackgroundColor(entry.color);
                    image.setImageResource(entry.resIdIcon);
                    return view;
                }
            };

            PopupDialog.showPopupFromAnchor(anchor, adapter, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onReplySelected(entries.get(position).type);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
