package com.circleof6.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.circleof6.R;
import com.circleof6.util.Constants;

/**
 * Created by sati on 11/08/2015.
 */
public class UnsentSMSDialog extends DialogFragment
{

    public static UnsentSMSDialog newInstance(String name)
    {
        UnsentSMSDialog dialogUnsentSMS = new UnsentSMSDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_FRIEND_NAME_DIALOG, name);
        dialogUnsentSMS.setArguments(bundle);
        return dialogUnsentSMS;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String textAlert = getString(R.string.message_unsent_sms) + " " + getArguments()
                .getString(Constants.ARG_FRIEND_NAME_DIALOG);
        builder.setMessage(textAlert);
        builder.setNegativeButton(R.string.ok, null);

        return builder.create();
    }
}
