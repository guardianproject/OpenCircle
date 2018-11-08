package com.circleof6.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.ui.Emoji;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class QuickStatusDialog {

    public interface QuickStatusDialogListener {
        void onQuickStatusSelected(int emoji);
    }

    public static void showFromAnchor(final View anchor, final QuickStatusDialogListener listener) {
        try {
            if (anchor == null)
                return;

            final Pair[] quickStatuses = new Pair[]{
                    new Pair<>(R.string.status_safe, Emoji.Safe),
                    new Pair<>(R.string.status_unsure, Emoji.Unsure),
                    new Pair<>(R.string.status_scared, Emoji.Scared)
            };

            final Context context = anchor.getContext();
            final ArrayAdapter<Pair> adapter = new ArrayAdapter<Pair>(context, R.layout.quick_status_popup_item, 0,
                    quickStatuses) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.quick_status_popup_item, parent, false);
                    }
                    TextView statusText = view.findViewById(R.id.statusText);
                    TextView statusIcon = view.findViewById(R.id.statusIcon);
                    statusText.setText((Integer) quickStatuses[position].first);
                    StringBuffer sb = new StringBuffer();
                    sb.append(Character.toChars((Integer) quickStatuses[position].second));
                    statusIcon.setText(sb);
                    return view;
                }
            };

            PopupDialog.showPopupFromAnchor(anchor, adapter, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onQuickStatusSelected((Integer)quickStatuses[position].second);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
