package com.circleof6;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CollegeCountry;
import com.circleof6.model.Contact;
import com.circleof6.model.ContactStatus;
import com.circleof6.model.ContactStatusReply;
import com.circleof6.model.ContactStatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.ui.Broadcasts;
import com.circleof6.ui.Emoji;
import com.circleof6.util.Constants;
import com.circleof6.util.LruBitmapCache;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.circleof6.util.MethodsUtils;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import static com.circleof6.util.MethodsUtils.getPhotoFileByContact;

/**
 * Created by corbett on 9/28/14.
 */
public class CircleOf6Application extends Application {
    public static final String LOG_TAG = "CircleOf6";

    private static CircleOf6Application context;

    // The contact that represents the current user, i.e. "You". Lazy loaded in getYouContact().
    private Contact youContact;
    private Contact[] contactList;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        EmojiManager.install(new GoogleEmojiProvider());

        context = this;
        updateLangLocale();

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

    public void statusUpdated(Contact contact) {
        //TODO save
        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, contact.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void setStatusSeen(Contact contact) {
        ContactStatus status = contact.getStatus();
        for (ContactStatusUpdate update : status.getUpdates()) {
            update.setSeen(true);
        }
        //TODO save
        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, contact.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendReply(Contact contact, ContactStatusReply reply) {
        // TODO- Send and save!
        contact.getStatus().getReplyList().add(reply);
        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, contact.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void unsendReply(Contact contact, ContactStatusReply reply) {
        // TODO - Send and save!
        contact.getStatus().getReplyList().remove(reply);
        Intent intent = new Intent(Broadcasts.BROADCAST_STATUS_UPDATE_CHANGED);
        intent.putExtra(Broadcasts.EXTRAS_CONTACT_ID, contact.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // TODO - Setup yourself
    public Contact getYouContact() {
        if (youContact == null) {
            String phone = AppPreferences.getInstance(this).getPhoneContact(0);
            if (TextUtils.isEmpty(phone)) {
                phone = "_"; //TEMP TODO, need a phone number to be a valid contact
            }
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
        AppPreferences.getInstance(this).saveNameCotact(2, "Paulina");
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
        // Jaqueline
        Contact contact = getContactWithId(4);
        ContactStatusUpdate update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 2 * 60000));
        update.setEmoji(Emoji.Unsure);
        update.setLocation("12343,12342");
        update.setMessage("En una entrevista. Me siento insegura. LLámame para interrumpirme.");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);

        // Paulina
        contact = getContactWithId(2);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 20 * 60000));
        update.setEmoji(Emoji.Unsure);
        //update.setLocation("12343,12342");
        update.setMessage("Voy a entrevistar a un politico en Juarez hoy, a las 3 pm. Ya voy en camino.");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 0));
        update.setEmoji(Emoji.Safe);
        //update.setLocation("12343,12342");
        update.setMessage("Sigo en camino. Todo va bien.");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);

        // Mariel
        contact = getContactWithId(3);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 6 * 60 * 60000));
        //update.setEmoji(Emoji.Unsure);
        //update.setLocation("12343,12342");
        update.setMessage("Estoy recibiendo cientos de tweets, usando el lenguaje más obsceno, amenazando con matarme, amenazando con violarme. Es difícil describir el miedo que siento.");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);

        // Ana
        contact = getContactWithId(1);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 4 * 60 * 60000));
        update.setEmoji(Emoji.Scared);
        //update.setLocation("12343,12342");
        update.setMessage("asdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nasdfjkhasdkfasdfafadsf\nkjdsfhsdkjhfkajhfkjahsdfsa\nkjahsdfkjahsdfkjhakjdfhkajdsf\naksjdfhkajdhfkjadsf\nkjafhdsfkjhadskjfhads\n\nskdjfhskjdhfksdhfsdkf\nkdsjfhaksjdfhjaskdfhaskdf\nskjdfhadkjsfhd skafhakjsdfhasdf\nakdjshfgak sdfjghakjsdfg asjkdhfg aksjdhfg ajksdhfg asjhkdfg aksjdhfg jksadhfg asjkdhfg ajksdhfg akjshdfg ajskdhfg ajksdhgf ajshdfg jakshdgf jkahsgd fahjsgdf hjkasgdf asldfgasftweirgasdkjfgai weyfga sdyfg adhjsfga uweyfg aysdgf Me estan atacando en mi cuenta de Twitter. Por favor ayúdenme. https://twitter.com/reporter/status/1055410689425244161");
        //update.setMessage("Me estan atacando en mi cuenta de Twitter. Por favor ayúdenme. https://twitter.com/reporter/status/1055410689425244161");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);

        // Marta
        contact = getContactWithId(5);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 6 * 60 * 60000));
        update.setEmoji(Emoji.Unsure);
        //update.setLocation("12343,12342");
        update.setMessage("Estoy trabajando en una historia de un tema riesgoso, Por favor denme consejos.");
        update.setSeen(false);
        update.setUrgent(false);
        contact.getStatus().addUpdate(update);

        // Isabela
        contact = getContactWithId(6);
        update = new ContactStatusUpdate();
        update.setDate(new Date(new Date().getTime() - 24 * 60 * 60000));
        update.setEmoji(Emoji.Scared);
        //update.setLocation("12343,12342");
        update.setMessage("Me preocupa mi integridad física cuando estoy en las calles.");
        update.setSeen(false);
        update.setUrgent(true);
        contact.getStatus().addUpdate(update);

        ArrayList<ContactStatusReply> replies = new ArrayList<>();
        ContactStatusReply reply1 = new ContactStatusReply();
        reply1.setContact(getContactList()[1]);
        reply1.setDate(new Date(118, 9, 31, 9, 52));
        reply1.setType(ContactStatusReply.ReplyType.Call);
        replies.add(reply1);
        ContactStatusReply reply2 = new ContactStatusReply();
        reply2.setContact(getContactList()[2]);
        reply2.setDate(new Date(118, 9, 31, 10, 52));
        reply2.setType(ContactStatusReply.ReplyType.Message);
        replies.add(reply2);
        ContactStatusReply reply3 = new ContactStatusReply();
        reply3.setContact(getContactList()[3]);
        reply3.setDate(new Date(118, 9, 32, 10, 52));
        reply3.setType(ContactStatusReply.ReplyType.Emoji);
        reply3.setEmoji(0x1f600);
        replies.add(reply3);
        ContactStatusReply reply4 = new ContactStatusReply();
        reply4.setContact(getContactList()[4]);
        reply4.setDate(new Date(118, 9, 32, 10, 52));
        reply4.setType(ContactStatusReply.ReplyType.Emoji);
        reply4.setEmoji(0x1f601);
        replies.add(reply4);
        getContactWithId(1).getStatus().setReplyList(replies);
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
