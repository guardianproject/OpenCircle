package com.circleof6.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.circleof6.service.ShareNotificationService;

/**
 * Created by Edgar Salvador Maurilio on 04/11/2015.
 */
public class ShareNotificationReceiver extends WakefulBroadcastReceiver {

    public static final String LOG_TAG = ShareNotificationReceiver.class.getSimpleName();

    public static final String ACTION_LAUNCH_NOTIFICATION = "circleofsix.williamsuniversity.action.launch_notification";
    public static final String ACTION_DELETE_NOTIFICATION = "circleofsix.williamsuniversity.action.delete_notifitation";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_LAUNCH_NOTIFICATION)) {
            Log.d(LOG_TAG, ACTION_LAUNCH_NOTIFICATION);

            startWakefulService(context, new Intent(context, ShareNotificationService.class));
        } else if (intent.getAction().equals(ACTION_DELETE_NOTIFICATION)) {
            Log.d(LOG_TAG, ACTION_DELETE_NOTIFICATION);
            ShareNotificationReceiver.disableCircleOfSixBootReceiver(context);
        }
        Log.d(LOG_TAG, "finish Receiver");

    }

    public static void createAlarmNotificationShare(Context context, long timeInMillis) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, ShareNotificationReceiver.class);
        alarmIntent.setAction(ACTION_LAUNCH_NOTIFICATION);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, timeInMillis, alarmPendingIntent);

    }


    public static void enableCircleOfSixBootReceiver(Context context) {

        ComponentName receiver = new ComponentName(context, CircleOfSixBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public static void disableCircleOfSixBootReceiver(Context context) {

        ComponentName receiver = new ComponentName(context, CircleOfSixBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

}
