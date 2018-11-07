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
import com.circleof6.model.StatusUpdateReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class UpdateRemoveDialog {

    public interface UpdateRemoveDialogListener {
        void onRemoveSelected();
        void onUpdateSelected();
    }

    private static class Entry {
        int color;
        int resIdTitle;
        int resIdIcon;

        public Entry(int color, int resIdTitle, int resIdIcon) {
            this.resIdTitle = resIdTitle;
            this.resIdIcon = resIdIcon;
        }
    }

    public static void showFromAnchor(final View anchor, final UpdateRemoveDialogListener listener) {
        try {
            if (anchor == null)
                return;

            final Context context = anchor.getContext();
            final List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0xffffffff, R.string.remove, R.drawable.ic_delete_black_24dp));
            entries.add(new Entry(0xffffffff, R.string.update, R.drawable.ic_refresh_black_24dp));

            final ArrayAdapter<Entry> adapter = new ArrayAdapter<Entry>(context, 0, 0,
                    entries.toArray(new Entry[0])) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.reply_popup_item, parent, false);
                    }
                    final Entry entry = entries.get(position);

                    TextView title = view.findViewById(R.id.title);
                    ImageView image = view.findViewById(R.id.image);
                    View roundFrame = view.findViewById(R.id.roundFrame);
                    roundFrame.setBackgroundColor(entry.color);
                    title.setText(entry.resIdTitle);
                    image.setImageResource(entry.resIdIcon);
                    return view;
                }
            };

            PopupDialog.showPopupFromAnchor(anchor, adapter, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        if (position == 0) {
                            listener.onRemoveSelected();
                        } else if (position == 1) {
                            listener.onUpdateSelected();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
