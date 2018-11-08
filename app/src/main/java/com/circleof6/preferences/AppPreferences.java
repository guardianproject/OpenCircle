package com.circleof6.preferences;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.circleof6.BuildConfig;
import com.circleof6.CircleOf6Application;
import com.circleof6.util.Constants;

import java.util.Calendar;


import static com.circleof6.model.CampusLang.Lang;

/**
 * Created by Edgar Salvador Maurilio on 04/11/2015.
 */
public class AppPreferences
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String PREFS_NAME = "Circleof6Prefs";

    //Type of Campus
    public static final String KEY_LOCALIZATION     = "localizationSetting";
    public static final String ID_NONE              = "none";
    public static final String ID_HOBART            = "hobartandwilliam";
    public static final String ID_HOUSTON_MAIN      = "uhouston_main";
    public static final String ID_HOUSTON_VICTORIA  = "uhouston_victoria";
    public static final String ID_HOUSTON_CLEARLAKE = "uhouston_clearlake";
    public static final String ID_HOUSTON_DOWNTOWN  = "uhouston_downtown";
    public static final String ID_PRINCE_WILLIAM    = "princewilliamcounty";
    public static final String ID_UCLA              = "ucla";
    public static final String ID_WILLIAMS          = "williams";

    public static final String ID_US    = "id_us";
    public static final String ID_INDIA = "id_ind";
    public static final String ID_OTHER = "id_oth";

    // Tutorial completed
    public static final String HAS_COMPLETED_TUTORIAL_KEY = "hasCompletedTutorial";

    //Notification
    public static final String IS_CREATED_SHARE_NOTIFICATION = "is_created_notification";
    public static final String IS_SHOW_SHARE_NOTIFICATION    = "is_show_notification";

    public static final String DAY_SHARE_NOTIFICATION   = "day_notification";
    public static final String MONTH_SHARE_NOTIFICATION = "month_notification";
    public static final String YEAR_SHARE_NOTIFICATION  = "year_notification";

    //Hotline Number
    public static final String HOLINE_PHONE = "hotline_phone";

    public static final String SHOW_ALLOW_CONTACT_DIALOG = "show_allow_contact_dialoga";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private static AppPreferences    INSTANCE;
    private        PreferencesHelper preferencesHelper;

    public static synchronized AppPreferences getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    private AppPreferences(Context context)
    {
        preferencesHelper = new PreferencesHelper(context, PREFS_NAME);
    }

    public boolean isCreateNotificationShare()
    {
        return preferencesHelper.getBoolean(IS_CREATED_SHARE_NOTIFICATION, false);
    }

    public boolean isShowNotificationShare()
    {
        return preferencesHelper.getBoolean(IS_SHOW_SHARE_NOTIFICATION, false);
    }

    public void setCreateNotifcation(boolean value)
    {
        preferencesHelper.applyPreference(IS_CREATED_SHARE_NOTIFICATION, value);
    }

    public void setShowNotifcation(boolean value)
    {
        preferencesHelper.applyPreference(IS_SHOW_SHARE_NOTIFICATION, value);
    }

    public void saveDateNotificationShare(int day, int month, int year)
    {
        preferencesHelper.applyPreference(DAY_SHARE_NOTIFICATION, day);
        preferencesHelper.applyPreference(MONTH_SHARE_NOTIFICATION, month);
        preferencesHelper.applyPreference(YEAR_SHARE_NOTIFICATION, year);
    }

    public int getDayNotificationShare()
    {
        return preferencesHelper.getInt(DAY_SHARE_NOTIFICATION, Calendar.getInstance()
                                                                        .get(Calendar.DAY_OF_MONTH) + Constants.DAYS_AFTER_NOTIFICATION_SHARE);
    }

    public int getMonthNotificationShare()
    {
        return preferencesHelper
                .getInt(MONTH_SHARE_NOTIFICATION, Calendar.getInstance().get(Calendar.MONTH));
    }

    public int getYearNotificationShare()
    {
        return preferencesHelper
                .getInt(YEAR_SHARE_NOTIFICATION, Calendar.getInstance().get(Calendar.YEAR));
    }

    public void saveCustomHotline(String customHotline)
    {
        preferencesHelper.applyPreference(HOLINE_PHONE, customHotline);
    }

    public String getCustomNumberHotline() {
        String oldVal = preferencesHelper.getString("hotline5phone", null);
        if(oldVal != null)
        {
            saveCustomHotline(oldVal);
            preferencesHelper.clearPreference("hotline5phone");
        }
        return preferencesHelper.getString(HOLINE_PHONE, "");
    }

    public boolean isEmptyCustomNumberHorline()
    {
        return TextUtils.isEmpty(preferencesHelper.getString(HOLINE_PHONE, ""));
    }

    public void saveCollegeLocalization(String collegeId)
    {
        preferencesHelper.applyPreference(KEY_LOCALIZATION, collegeId);
    }

    public String getCollegeLocation()
    {
        return preferencesHelper.getString(KEY_LOCALIZATION, "");
    }

    public boolean isEmptyCollegeLocation()
    {
        return TextUtils.isEmpty(preferencesHelper.getString(KEY_LOCALIZATION, ""));
    }


    public Constants.UniversityLocalization getUniversityLocalization()
    {
        String localization = getCollegeLocation();

        switch(localization)
        {
            case ID_US:
                return Constants.UniversityLocalization.USA;
            case ID_INDIA:
                return Constants.UniversityLocalization.INDIA;
            case ID_OTHER:
                return Constants.UniversityLocalization.OTHER_COUNTRY;
            default:
                return Constants.UniversityLocalization.NONE;
        }
    }


    public void saveHasCompletedTutorial(boolean hasCompletedTutorial)
    {
        preferencesHelper.applyPreference(HAS_COMPLETED_TUTORIAL_KEY, hasCompletedTutorial);
    }

    public boolean hasCompletedTutorial()
    {
        if (BuildConfig.DEBUG) {
            return true; //TEMP TEMP - ignore for demo
        }
        //final Constants.UniversityLocalization universityLocalization = getUniversityLocalization();
        return preferencesHelper.getBoolean(HAS_COMPLETED_TUTORIAL_KEY, false); // && universityLocalization != null && universityLocalization != Constants.UniversityLocalization.NONE;
    }

    public void saveNameCotact(int idContact, String name)
    {
        preferencesHelper.applyPreference("friend" + idContact + "name", name);
    }

    public void savaPhotoCotact(int idContact, String photo)
    {
        preferencesHelper.applyPreference("friend" + idContact + "photo", photo);
    }

    public void savePhoneCotact(int idContact, String phone)
    {
        preferencesHelper.applyPreference("friend" + idContact + "phone", phone);
    }

    public String getNameContact(int idContact)
    {
        return preferencesHelper.getString("friend" + idContact + "name", "");
    }

    public String getPhotoContact(int idContact)
    {
        return preferencesHelper.getString("friend" + idContact + "photo", "");
    }

    public String getPhoneContact(int idContact)
    {
        return preferencesHelper.getString("friend" + idContact + "phone", "");
    }

    public void saveShowAllowContactDialog(boolean allow)
    {
        preferencesHelper.applyPreference(SHOW_ALLOW_CONTACT_DIALOG, allow);
    }

    public boolean isShowAllowContactDialog()
    {
        return preferencesHelper.getBoolean(SHOW_ALLOW_CONTACT_DIALOG, false);
    }
}
