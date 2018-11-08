package com.circleof6;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CollegeCountry;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.model.StatusUpdateReply;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.ui.Broadcasts;
import com.circleof6.ui.Emoji;
import com.circleof6.util.Constants;
import com.circleof6.util.LruBitmapCache;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.circleof6.util.MethodsUtils;
import com.circleof6.view.util.DrawUtils;

import static com.circleof6.util.MethodsUtils.getPhotoFileByContact;

/**
 * Created by corbett on 9/28/14.
 */
public class CircleOf6Application extends Application {
    public static final String LOG_TAG = "CircleOf6";

    private static CircleOf6Application context;

    // The contact that represents the current user, i.e. "You". Lazy loaded in getYouContact().
    private Contact youContact;
    private StatusUpdate youStatus;
    private Contact[] contactList;
    private StatusUpdate[] contactStatusList;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());

        context = this;
        updateLangLocale();

        contactStatusList = new StatusUpdate[Constants.MAX_NUMBER_OF_FRIENDS];
        initializeMockupContacts();
        initializeMockupStatuses();
    }

    public static void updateLangLocale()
    {
            Locale locale;
    }


    public static void defaultLabelTrackingEvent(Context context, String action)
    {
       // tracker.send(createMapTracker(ConstantsAnalytics.CATEGOTY_APP_USAGE, action,
         //                             AppPreferences.getInstance(context).getCollegeLocation()));
    }


    public static void trackingEvent(String action, String label)
    {
        //tracker.send(createMapTracker(ConstantsAnalytics.CATEGOTY_APP_USAGE, action, label));
    }


    public static boolean isUniversalFlavor()
    {
        return BuildConfig.FLAVOR.equals("universal");
    }

    public static final String TAG = CircleOf6Application.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static synchronized CircleOf6Application getInstance() {
        return context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static boolean isServiceRunning(Class<?> cls) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setUpLogo(NetworkImageView logo, DBHelper dbHelper){

        //Get the set college
        String college_id = AppPreferences.getInstance(this).getCollegeLocation();
        if (!college_id.equals("")) {
            CollegeCountry collegeCountry;
            if (college_id.startsWith("c_")) {
                //this is campus, find parent
                collegeCountry = dbHelper.getParentCollege(college_id.replace("c_", ""));
            } else {
                collegeCountry = dbHelper.getCollege(college_id);
            }
            if (!isUniversalFlavor()) {
                logo.setVisibility(View.VISIBLE);

                ImageLoader imageLoader = getImageLoader();

                logo.setImageUrl(collegeCountry.getLogoUrl(), imageLoader);

            }
        }

    }


    public Contact[] getContactList() {
        if (contactList == null) {
            // Lazy loading
            contactList = new Contact[Constants.MAX_NUMBER_OF_FRIENDS];
            for (int idContact = 1; idContact <= Constants.MAX_NUMBER_OF_FRIENDS; idContact++) {
                String name = AppPreferences.getInstance(this).getNameContact(idContact);
                String phone = AppPreferences.getInstance(this).getPhoneContact(idContact);
                String photo = AppPreferences.getInstance(this).getPhotoContact(idContact);
                Contact contact = new Contact(idContact, name, phone, photo);
                contactList[idContact - 1] = contact;
            }
        }
        return contactList;
    }

    public Contact getContactWithId(int id) {
        if (id == 0) {
            return getYouContact();
        } else {
            return getContactList()[id - 1];
        }
    }

    public StatusUpdate getContactStatus(Contact contact) {
        if (contact.isYou()) {
            return youStatus;
        }
        return contactStatusList[contact.getId() - 1];
    }

    public void setContactStatus(Contact contact, StatusUpdate statusUpdate) {
        if (contact.isYou()) {
            youStatus = statusUpdate;
        } else {
            contactStatusList[contact.getId() - 1] = statusUpdate;
            //TODO save
        }

        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, contact.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void setStatusSeen(StatusUpdate statusUpdate) {
        statusUpdate.setSeen(true);
        //TODO save
        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, statusUpdate.getContact().getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // TODO - Setup yourself
    public Contact getYouContact() {
        if (youContact == null) {
            String phone = AppPreferences.getInstance(this).getPhoneContact(0);
            String photo = AppPreferences.getInstance(this).getPhotoContact(0);
            youContact = new Contact(0, getString(R.string.you), phone, photo);
            youContact.setYou(true);
        }
        return youContact;
    }

    void initializeMockupContacts() {
        saveMockupPhoto(0);
        AppPreferences.getInstance(this).savaPhotoCotact(0, getFileStreamPath(MethodsUtils.getPhotoFileByContact(0)).toString());

        AppPreferences.getInstance(this).saveNameCotact(1, "Ana");
        AppPreferences.getInstance(this).savePhoneCotact(1, "555898989");
        saveMockupPhoto(1);
        AppPreferences.getInstance(this).savaPhotoCotact(1, getFileStreamPath(MethodsUtils.getPhotoFileByContact(1)).toString());
        AppPreferences.getInstance(this).saveNameCotact(2, "Rosa");
        AppPreferences.getInstance(this).savePhoneCotact(2, "555898989");
        saveMockupPhoto(2);
        AppPreferences.getInstance(this).savaPhotoCotact(2, getFileStreamPath(MethodsUtils.getPhotoFileByContact(2)).toString());
        AppPreferences.getInstance(this).saveNameCotact(3, "Mariel");
        AppPreferences.getInstance(this).savePhoneCotact(3, "555898989");
        saveMockupPhoto(3);
        AppPreferences.getInstance(this).savaPhotoCotact(3, getFileStreamPath(MethodsUtils.getPhotoFileByContact(3)).toString());
        AppPreferences.getInstance(this).saveNameCotact(4, "Jaqueline");
        AppPreferences.getInstance(this).savePhoneCotact(4, "555898989");
        saveMockupPhoto(4);
        AppPreferences.getInstance(this).savaPhotoCotact(4, getFileStreamPath(MethodsUtils.getPhotoFileByContact(4)).toString());
        AppPreferences.getInstance(this).saveNameCotact(5, "Marta");
        AppPreferences.getInstance(this).savePhoneCotact(5, "555898989");
        saveMockupPhoto(5);
        AppPreferences.getInstance(this).savaPhotoCotact(5, getFileStreamPath(MethodsUtils.getPhotoFileByContact(5)).toString());
        AppPreferences.getInstance(this).saveNameCotact(6, "Isabela");
        AppPreferences.getInstance(this).savePhoneCotact(6, "555898989");
        saveMockupPhoto(6);
        AppPreferences.getInstance(this).savaPhotoCotact(6, getFileStreamPath(MethodsUtils.getPhotoFileByContact(6)).toString());
    }

    void initializeMockupStatuses() {
        StatusUpdate update = new StatusUpdate();
        update.setDate(new Date(new Date().getTime() - 2 * 60000));
        update.setContact(getContactList()[4]);
        update.setEmoji(Emoji.Unsure);
        update.setLocation("12343,12342");
        update.setMessage("En una entrevista. Me siento insegura. LLámame para interrumpirme.");
        update.setSeen(false);
        update.setUrgent(false);
        contactStatusList[0] = update;

        update = new StatusUpdate();
        update.setDate(new Date(new Date().getTime() - 2 * 60000));
        update.setContact(getContactList()[4]);
        update.setEmoji(Emoji.Unsure);
        update.setLocation("12343,12342");
        update.setMessage("En una entrevista. Me siento insegura. LLámame para interrumpirme.");
        update.setSeen(false);
        update.setUrgent(false);
        contactStatusList[1] = update;


        ArrayList<StatusUpdateReply> replies = new ArrayList<>();
        StatusUpdateReply reply1 = new StatusUpdateReply();
        reply1.setContact(getContactList()[1]);
        reply1.setDate(new Date(118, 9, 31, 9, 52));
        reply1.setType(StatusUpdateReply.ReplyType.Call);
        replies.add(reply1);
        StatusUpdateReply reply2 = new StatusUpdateReply();
        reply2.setContact(getContactList()[2]);
        reply2.setDate(new Date(118, 9, 31, 10, 52));
        reply2.setType(StatusUpdateReply.ReplyType.Message);
        replies.add(reply2);
        StatusUpdateReply reply3 = new StatusUpdateReply();
        reply3.setContact(getContactList()[3]);
        reply3.setDate(new Date(118, 9, 32, 10, 52));
        reply3.setType(StatusUpdateReply.ReplyType.Emoji);
        reply3.setEmoji(0x1f600);
        replies.add(reply3);
        StatusUpdateReply reply4 = new StatusUpdateReply();
        reply4.setContact(getContactList()[4]);
        reply4.setDate(new Date(118, 9, 32, 10, 52));
        reply4.setType(StatusUpdateReply.ReplyType.Emoji);
        reply4.setEmoji(0x1f601);
        replies.add(reply4);
        update.setReplyList(replies);

        contactStatusList[0] = update;

        update = new StatusUpdate();
        update.setDate(new Date(118, 10, 3, 19, 12));
        update.setContact(getContactList()[3]);
        update.setEmoji(0x1f608);
        update.setLocation("1234;12342");
        update.setMessage("I am worried about my physical safety");
        update.setSeen(false);
        update.setUrgent(false);
        contactStatusList[3] = update;
    }

    void saveMockupPhoto(int friendId) {
        String photoPng = getPhotoFileByContact(friendId);
        FileOutputStream fos;
        try
        {
            int[] ids = new int[] {
                    R.raw.av_00, R.raw.av_01, R.raw.av_02, R.raw.av_03, R.raw.av_04, R.raw.av_05, R.raw.av_06
            };
            InputStream is = getResources().openRawResource(ids[friendId]);
            Bitmap image = BitmapFactory.decodeStream(is);
            fos = openFileOutput(photoPng, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
