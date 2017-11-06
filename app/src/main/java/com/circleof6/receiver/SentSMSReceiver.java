package com.circleof6.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.circleof6.ui.MainActivity;
import com.circleof6.util.Constants;

/**
 * Created by sati on 11/08/2015.
 */
public class SentSMSReceiver extends BroadcastReceiver
{

    public static final String TAG = SentSMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        MainActivity mainActivity = (MainActivity) context;


        Log.d(TAG, intent.getAction());
        if (intent.getAction().contains(Constants.ACTION_SENT_SMS_CONTACT)) {
            if (getResultCode() == Activity.RESULT_OK) {
                ((MainActivity) context).displayNewContact();
            } else {
                mainActivity.showUnsentSMSDialog(splitName(intent.getAction()));
            }
        } else {
            if (getResultCode() == Activity.RESULT_OK) {
                ((MainActivity) context).showAlertSentMessageSuccess();
            } else {
                mainActivity.showUnsentSMSDialog(splitName(intent.getAction()));
            }
        }
    }

    public String splitName(String action)
    {
        String[] split = action.split("-");
        return split[split.length - 1];
    }

}
