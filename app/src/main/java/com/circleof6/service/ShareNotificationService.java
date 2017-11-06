package com.circleof6.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.circleof6.R;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.receiver.ShareNotificationReceiver;
import com.circleof6.ui.SharingAppActivity;
import com.circleof6.util.Constants;

/**
 * Created by Edgar Salvador Maurilio on 04/11/2015.
 */
public class ShareNotificationService extends IntentService {
    public ShareNotificationService() {
        super("ShareNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(this, SharingAppActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0,
                contentIntent, 0);

        Intent deleteIntent = new Intent(this, ShareNotificationReceiver.class);
        deleteIntent.setAction(ShareNotificationReceiver.ACTION_DELETE_NOTIFICATION);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(this, 0,
                deleteIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getText(R.string.messsage_notification_share)))
                        .setContentText(getText(R.string.messsage_notification_share))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(contentPendingIntent)
                        .setDeleteIntent(deletePendingIntent);

        mNotificationManager.notify(Constants.ID_SHARE_NOTIFICATION, mBuilder.build());

        AppPreferences.getInstance(this).setShowNotifcation(true);
        ShareNotificationReceiver.disableCircleOfSixBootReceiver(this);
        ShareNotificationReceiver.completeWakefulIntent(intent);
    }


}
