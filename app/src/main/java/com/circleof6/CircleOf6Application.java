package com.circleof6;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CollegeCountry;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.LruBitmapCache;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
/**
 * Created by corbett on 9/28/14.
 */
public class CircleOf6Application extends Application {
    public static final String LOG_TAG = "CircleOf6";

    private static CircleOf6Application context;


    @Override
    public void onCreate()
    {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());

        context = this;
        updateLangLocale();

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

    public StatusUpdate getContactStatus(Contact contact) {
        //TODO
        StatusUpdate update = new StatusUpdate();
        update.setDate(new Date(118, 9, 30, 9, 52));
        update.setEmoji(0x1f600 + (int)(20.0f * Math.random()));
        update.setLocation("1234;12342");
        update.setMessage("I am ok");
        update.setSeen(Math.random() > 0.5f);
        return update;
    }
}
