package com.circleof6.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.dialog.utils.MethodsDialog;

/**
 * Created by Edgar Salvador Maurilio on 22/11/2015.
 */
public class OverlayDialog extends DialogFragment {


    public static final String CENTER_TEXT_VIEW = "center_text_view";
    public static final String SHOW_CHECK       = "SHOW_CHECK";
    public static final String ALERT_TYPE = "ALERT_TYPE";

    public enum Type
    {
        OK,
        Cancel,
        Progress
    }

    private PressOkSendSmsListener pressOkSendSmsListener;

    public interface PressOkSendSmsListener
    {
        void onPressOkSendSms();
    }

    public void setPressOkSendSmsListener(PressOkSendSmsListener pressOkSendSmsListener)
    {
        this.pressOkSendSmsListener = pressOkSendSmsListener;
    }


    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if(pressOkSendSmsListener != null) pressOkSendSmsListener.onPressOkSendSms();
    }

    public static OverlayDialog createDialog(String centerText)
    {
        return createDialog(centerText, Type.OK);
    }

    public static OverlayDialog createDialog(String centerText, Type type)
    {
        return createDialog(centerText, type, type == Type.OK);
    }

    public static OverlayDialog createDialog(String centerText, Type type, boolean showCheck)
    {
        Bundle args = new Bundle();
        args.putString(CENTER_TEXT_VIEW, centerText);
        args.putSerializable(ALERT_TYPE, type);
        args.putBoolean(SHOW_CHECK, showCheck);
        OverlayDialog od = new OverlayDialog();
        od.setArguments(args);
        return od;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_overlay, container);
        ((TextView)view.findViewById(R.id.center_text)).setText(getArguments().getString(
                CENTER_TEXT_VIEW));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupOkayButton(view);
    }

    private void setupOkayButton(View view) {
        final View progress = view.findViewById(R.id.location_progress);
        final View check = view.findViewById(R.id.image_alert_check);
        final View okButton = view.findViewById(R.id.button_ok);
        final View cancelButton = view.findViewById(R.id.button_cancel);
        final boolean showCheck = getArguments().getBoolean(SHOW_CHECK);
        final Type type = (Type)
                getArguments().getSerializable(ALERT_TYPE);
        check.setVisibility(showCheck ? View.VISIBLE : View.GONE);

        okButton.setVisibility(type == Type.OK ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(type == Type.Cancel ? View.VISIBLE : View.GONE);
        progress.setVisibility(type == Type.Progress ? View.VISIBLE : View.GONE);

        View buttonView = null;

        if(type == Type.OK)
            buttonView = okButton;
        else if(type == Type.Cancel)
            buttonView = cancelButton;

        if(buttonView != null)
        {
            buttonView.setVisibility(View.VISIBLE);
            buttonView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(pressOkSendSmsListener != null) pressOkSendSmsListener.onPressOkSendSms();
                    dismiss();
                }
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return MethodsDialog.createTransparentDialog(getActivity());
    }
}
