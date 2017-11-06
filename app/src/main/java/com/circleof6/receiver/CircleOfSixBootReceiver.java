package com.circleof6.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.Constants;

import java.util.Calendar;

/**
 * Created by Edgar Salvador Maurilio on 04/11/2015.
 */
public class CircleOfSixBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (!AppPreferences.getInstance(context).isShowNotificationShare()) {
                Calendar calendar = getCalendarSave(context);
                ShareNotificationReceiver.createAlarmNotificationShare(context, calendar.getTimeInMillis());
            } else {
                ShareNotificationReceiver.disableCircleOfSixBootReceiver(context);
            }

        }
    }

    private Calendar getCalendarSave(Context context) {
        Calendar calendar = Calendar.getInstance();
        AppPreferences appPreferences = AppPreferences.getInstance(context);

        calendar.set(Calendar.DAY_OF_MONTH, appPreferences.getDayNotificationShare());
        calendar.set(Calendar.MONTH, appPreferences.getMonthNotificationShare());
        calendar.set(Calendar.YEAR, appPreferences.getYearNotificationShare());

        calendar.set(Calendar.HOUR_OF_DAY, Constants.HOUR_NOTIFICATION_SHARE);
        calendar.set(Calendar.MINUTE, Constants.MINUTE_NOTIFICATION_SHARE);

        return calendar;
    }

}
