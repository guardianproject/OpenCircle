package com.circleof6.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.StatusUpdateReply;
import com.circleof6.util.MethodsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class QuickStatusDialog {

    public interface QuickStatusDialogListener {
        void onQuickStatusSelected();
        void onQuickStatusCanceled();
    }

    public static void showFromAnchor(final View anchor, final QuickStatusDialogListener listener) {
        try {
            if (anchor == null)
                return;

            final Pair[] quickStatuses = new Pair[]{
                    new Pair<>(R.string.status_safe, 0x1f600),
                    new Pair<>(R.string.status_unsure, 0x1f606),
                    new Pair<>(R.string.status_scared, 0x1f607)
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

            // Get root and locations
            Rect rectGlobal = new Rect();
            anchor.getGlobalVisibleRect(rectGlobal);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.quick_status_popup, (ViewGroup)anchor.getRootView(), false);

            ListView lv = dialogView.findViewById(R.id.lvItems);
            lv.setBackgroundColor(Color.TRANSPARENT);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setAdapter(adapter);

            final Dialog dialog = new Dialog(context,
                    android.R.style.Theme_Translucent_NoTitleBar);

            // Setting dialogview
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.x = rectGlobal.right - MethodsUtils.dpToPx(200, context);
            wlp.y = rectGlobal.top;
            wlp.width = MethodsUtils.dpToPx(200, context);
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.gravity = Gravity.TOP | Gravity.START;
            wlp.dimAmount = 0.6f;
            wlp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            dialog.setTitle(null);
            dialog.setContentView(dialogView);
            dialog.setCancelable(true);
            //dialog.setCanceledOnTouchOutside(true);

            anchor.setVisibility(View.INVISIBLE);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    anchor.setVisibility(View.VISIBLE);
                }
            });

            View buttonClose = dialogView.findViewById(R.id.btnClose);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
