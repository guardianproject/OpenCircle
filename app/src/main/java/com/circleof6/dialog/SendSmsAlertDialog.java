package com.circleof6.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.dialog.utils.ConstantsDialog;
import com.circleof6.dialog.utils.MethodsDialog;
import com.circleof6.dialog.utils.TypeSendSmsListener;

/**
 * Created by Edgar Salvador Maurilio on 22/11/2015.
 */
public class SendSmsAlertDialog extends DialogFragment implements View.OnClickListener {

    private TypeSendSmsListener typeSendSmsListener;

    public static SendSmsAlertDialog newInstance (ConstantsDialog.TypeSmsAlertDialog typeSmsAlertDialog) {

        SendSmsAlertDialog sendSmsAlertDialog = new SendSmsAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantsDialog.ARG_TYPE_SMS_ALERT, typeSmsAlertDialog);
        sendSmsAlertDialog.setArguments(bundle);
        return sendSmsAlertDialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        typeSendSmsListener = (TypeSendSmsListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_send_sms_alert, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCenterMessage(view);
        setupButtons(view);

    }

    private void setupCenterMessage(View view) {

        TextView imageViewAlert = (TextView) view.findViewById(R.id.center_text);
        imageViewAlert.setText(getTypeTextAlert());

    }


    private int getTypeTextAlert()
    {
        switch (getTypeSmsAlert())
        {
            case COME_AND_GETME:
                return R.string.alert_sms_confirmation_location;
            case CALL_ME_NEED_INTERRUPTION:
                return R.string.alert_sms_confirmation_interruption;
            case NEED_TO_TALK:
                return R.string.alert_sms_confirmation_relationships;
            case I_AM_OK:
            default:
                return R.string.alert_got_help;
        }
    }


    private ConstantsDialog.TypeSmsAlertDialog getTypeSmsAlert() {
        return (ConstantsDialog.TypeSmsAlertDialog) getArguments().getSerializable(ConstantsDialog.ARG_TYPE_SMS_ALERT);
    }


    private void setupButtons(View view) {
        view.findViewById(R.id.sms_confirm).setOnClickListener(this);
        view.findViewById(R.id.sms_cancel).setOnClickListener(this);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return MethodsDialog.createTransparentDialog(getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sms_confirm:
                sendSms();
                break;
            case R.id.sms_cancel:
                break;
        }
        dismiss();
    }

    private void sendSms() {
        switch (getTypeSmsAlert())
        {
            case COME_AND_GETME:
                typeSendSmsListener.sendSmsComeAndGetMe();
                break;
            case CALL_ME_NEED_INTERRUPTION:
                typeSendSmsListener.sendSmsCallMeNeed();
                break;
            case NEED_TO_TALK:
                typeSendSmsListener.sendSmsNeedToTalk();
                break;
            case I_AM_OK:
                typeSendSmsListener.sendSmsIamOk();
                break;
        }
    }
}
