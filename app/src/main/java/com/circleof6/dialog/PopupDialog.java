package com.circleof6.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.circleof6.R;
import com.circleof6.util.MethodsUtils;

/**
 * Created by N-Pex on 2018-11-07.
 */
public class PopupDialog {
    public static void showPopupFromAnchor(final View anchor, ArrayAdapter adapter, final AdapterView.OnItemClickListener listener) {
        try {
            if (anchor == null || adapter == null || adapter.getCount() == 0) {
                return;
            }

            final Context context = anchor.getContext();

            View rootView = anchor.getRootView();

            Rect rectAnchor = new Rect();
            int[] location = new int[2];
            anchor.getLocationInWindow(location);
            rectAnchor.set(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
            Rect rectRoot = new Rect();
            rootView.getLocationInWindow(location);
            rectRoot.set(location[0], location[1], location[0] + rootView.getWidth(), location[1] + rootView.getHeight());

            final Dialog dialog = new Dialog(context,
                    android.R.style.Theme_Translucent_NoTitleBar);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_menu, (ViewGroup)anchor.getRootView(), false);
            View buttonCloseTop = dialogView.findViewById(R.id.btnCloseTop);
            View buttonCloseBottom = dialogView.findViewById(R.id.btnCloseBottom);
            ListView lv = dialogView.findViewById(R.id.lvItems);
            lv.setBackgroundColor(Color.TRANSPARENT);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onItemClick(parent, view, position, id);
                        dialog.dismiss();
                    }
                }
            });
            lv.setAdapter(adapter);


            // Top or bottom?
            boolean isDropDown = true;
            if ((rectAnchor.top - rectRoot.top) > (rectRoot.bottom - rectAnchor.bottom)) {
                isDropDown = false;
                buttonCloseTop.setVisibility(View.GONE);
                dialogView.measure(
                        View.MeasureSpec.makeMeasureSpec(MethodsUtils.dpToPx(200, context), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec((rectAnchor.top - rectRoot.top), View.MeasureSpec.AT_MOST));
            } else {
                buttonCloseBottom.setVisibility(View.GONE);
                dialogView.measure(
                        View.MeasureSpec.makeMeasureSpec(MethodsUtils.dpToPx(200, context), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec((rectRoot.bottom - rectAnchor.bottom), View.MeasureSpec.AT_MOST));
            }


            dialog.setTitle(null);
            dialog.setContentView(dialogView);
            dialog.setCancelable(true);

            // Setting dialogview
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.x = rectAnchor.right - dialogView.getMeasuredWidth();
            if (isDropDown) {
                wlp.y = rectAnchor.top;
            } else {
                wlp.y = rectAnchor.bottom - dialogView.getMeasuredHeight();
            }
            wlp.width = dialogView.getMeasuredWidth();
            wlp.height = dialogView.getMeasuredHeight();
            wlp.gravity = Gravity.TOP | Gravity.START;
            wlp.dimAmount = 0.6f;
            wlp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            dialog.setCanceledOnTouchOutside(true);

            anchor.setVisibility(View.INVISIBLE);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    anchor.setVisibility(View.VISIBLE);
                }
            });

            (isDropDown ? buttonCloseTop : buttonCloseBottom).setOnClickListener(new View.OnClickListener() {
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
