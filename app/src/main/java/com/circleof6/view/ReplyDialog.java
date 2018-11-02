package com.circleof6.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class ReplyDialog {

    private static class ReplyEntry {
        public StatusUpdateReply.ReplyType type;
        public int color;
        public int resIdTitle;
        public int resIdIcon;

        public ReplyEntry(StatusUpdateReply.ReplyType type, int color, int resIdTitle, int resIdIcon) {
            this.type = type;
            this.color = color;
            this.resIdTitle = resIdTitle;
            this.resIdIcon = resIdIcon;
        }
    }

    public static void showFromAnchor(final View anchor) {
        try {
            if (anchor == null)
                return;

            final Context context = anchor.getContext();
            final List<ReplyEntry> entries = new ArrayList<>();
            entries.add(new ReplyEntry(StatusUpdateReply.ReplyType.Call, 0xfff8e71c, R.string.reply_call, R.drawable.ic_reply_call));
            entries.add(new ReplyEntry(StatusUpdateReply.ReplyType.Message, 0xff3384ff, R.string.reply_message, R.drawable.ic_reply_message));
            entries.add(new ReplyEntry(StatusUpdateReply.ReplyType.WhatsApp, 0xff23b180, R.string.reply_whatsapp, R.drawable.ic_reply_whatsapp));

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

            Rect rectGlobal = new Rect();
            anchor.getGlobalVisibleRect(rectGlobal);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.reply_popup, (ViewGroup)anchor.getRootView(), false);

            dialogView.measure(
                    View.MeasureSpec.makeMeasureSpec(MethodsUtils.dpToPx(200, context), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(MethodsUtils.dpToPx(400, context), View.MeasureSpec.EXACTLY));

            ListView lv = dialogView.findViewById(R.id.lvItems);
            lv.setBackgroundColor(Color.TRANSPARENT);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setAdapter(adapter);

            final Dialog dialog = new Dialog(context,
                    android.R.style.Theme_Translucent_NoTitleBar);


            // Setting dialogview
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.x = rectGlobal.right - dialogView.getMeasuredWidth();
            wlp.y = rectGlobal.bottom - dialogView.getMeasuredHeight();
            wlp.width = dialogView.getMeasuredWidth();
            wlp.height = dialogView.getMeasuredHeight();
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
