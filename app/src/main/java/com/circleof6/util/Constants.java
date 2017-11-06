package com.circleof6.util;

import android.provider.ContactsContract;

import java.util.Calendar;

/**
 * Created by sati on 11/08/2015.
 */
public class Constants
{
  //Constans

    public enum UniversityLocalization { NONE, USA, INDIA, OTHER_COUNTRY}

    //Response of intents
    public static final int FRIEND_ONE_INTENT = 1;
    public static final int FRIEND_TWO_INTENT = 2;
    public static final int FRIEND_THREE_INTENT = 3;
    public static final int FRIEND_FOUR_INTENT = 4;
    public static final int FRIEND_FIVE_INTENT = 5;
    public static final int FRIEND_SIX_INTENT = 6;
    public static final int PICK_COLLEGE_INTENT = 13;
    public static final int PAGED_TUTORIAL_INTENT = 14;

    //Query Contacts
    public static final String CONTACT_ID_SELECTION = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
    public static final String[] CONTACT_DATA_FIELDS = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_ID};

    //Total friends
    public static final int MAX_NUMBER_OF_FRIENDS = 6;

    //Photo default
    public static final String PHOTO_IS_DEFAULT = "DEFAULT";

    //Split Contact
    public static final int nameMaxChars = 11;


    //Error request play services
    public static final int REQUEST_PLAY_SERVICES_ERROR = 16;

    // Actions Send sms
    public static final String ACTION_SENT_SMS         = "sent_sms";
    public static final String ACTION_SENT_SMS_CONTACT = "sent_sms_contact";
    public static final String ARG_FRIEND_NAME_DIALOG     = "friend_name";

    //Animation Arrow Colleges
    public static final long   DEFAULT_DURATION_ANIMATION = 200;

    //Share notification
    public static final int DAYS_AFTER_NOTIFICATION_SHARE = 1;
    public static final int HOUR_NOTIFICATION_SHARE = 14;
    public static final int MINUTE_NOTIFICATION_SHARE = 0;
    public static final int ID_SHARE_NOTIFICATION = 435;

    /**
    public static String API_URL = "http://circleof6.herokuapp.com/api/";
    public static String UNIVERSITIES = "universities";
    public static String CAMPUSES = "campuses";
     **/
}
