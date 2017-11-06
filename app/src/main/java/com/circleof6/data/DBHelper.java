package com.circleof6.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.circleof6.model.CampusLang;
import com.circleof6.model.CollegeCountry;
import com.circleof6.model.CustomNumber;
import com.circleof6.model.InfoLink;
import com.circleof6.model.SMS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickhargreaves on 6/23/16.
 */

public class DBHelper extends SQLiteOpenHelper{

    //Tables
    public static String TABLE_INSTITUTIONS = "table_universities";
    public static String TABLE_CAMPUS = "table_campuses";
    public static String TABLE_CUSTOM_NUMBER = "table_customnumber";
    public static String TABLE_WEB_RESOURCES = "table_web_resources";
    public static String TABLE_SMS = "sms";

    //Common columns
    private static String COLUMN_ID = "id";
    public static String COLUMN_REMOTE_ID = "remote_id";

    //Institution columns
    private static String COLUMN_NAME = "name";
    private static String COLUMN_LOGO = "logo";
    private static String COLUMN_WELCOME_MESSAGE = "welcome_message";
    private static String COLUMN_CUSTOM_NUMBER_MESSAGE = "custom_number_message";
    private static String COLUMN_TERMS = "terms_of_service";
    public static String COLUMN_PARENT = "parent_university";
    
    //Custom number columns
    private static String COLUMN_HOTLINE_NAME = "hotline_name";
    private static String COLUMN_HOTLINE_NUMBER = "phone_number";

    //Web resources
    private static String COLUMN_RESOURCE_NAME = "name";
    private static String COLUMN_RESOURCE_LINK = "link";

    //SMS
    private static String COLUMN_LABEL = "label";
    private static String COLUMN_TEXT = "text";

    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "circleof6.db";

    private static final String TABLE_INSTITUTIONS_CREATE = "create table "
            + TABLE_INSTITUTIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REMOTE_ID + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_LOGO + " text, "
            + COLUMN_WELCOME_MESSAGE + " text, "
            + COLUMN_CUSTOM_NUMBER_MESSAGE + " text, "
            + COLUMN_TERMS + " text);";

    private static final String TABLE_CAMPUSES_CREATE = "create table "
            + TABLE_CAMPUS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REMOTE_ID + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_LOGO + " text, "
            + COLUMN_WELCOME_MESSAGE + " text, "
            + COLUMN_CUSTOM_NUMBER_MESSAGE + " text, "
            + COLUMN_PARENT + " text, "
            + COLUMN_TERMS + " text);";

    private static final String TABLE_CUSTOM_NUMBER_CREATE = "create table "
            + TABLE_CUSTOM_NUMBER + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REMOTE_ID + " text, "
            + COLUMN_HOTLINE_NAME + " text, "
            + COLUMN_HOTLINE_NUMBER + " text);";
    
    private static final String TABLE_WEB_RESOURCES_CREATE = "create table "
            + TABLE_WEB_RESOURCES + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REMOTE_ID + " text, "
            + COLUMN_RESOURCE_NAME + " text, "
            + COLUMN_RESOURCE_LINK + " text);";

    private static final String TABLE_SMS_CREATE = "create table "
            + TABLE_SMS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REMOTE_ID + " text, "
            + COLUMN_LABEL + " text, "
            + COLUMN_TEXT + " text);";
    
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_INSTITUTIONS_CREATE);
        db.execSQL(TABLE_CAMPUSES_CREATE);
        db.execSQL(TABLE_CUSTOM_NUMBER_CREATE);
        db.execSQL(TABLE_WEB_RESOURCES_CREATE);
        db.execSQL(TABLE_SMS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_NUMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEB_RESOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        onCreate(db);
    }

    public void clearCustomData(Context ctx){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CUSTOM_NUMBER);
        db.execSQL("DELETE FROM " + TABLE_WEB_RESOURCES);
        db.execSQL("DELETE FROM " + TABLE_SMS);

        //So it knows the content is not loaded yet
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(TABLE_CUSTOM_NUMBER);
        editor.remove(TABLE_WEB_RESOURCES);
        editor.remove(TABLE_SMS);

        editor.commit();
    }

    public List<Object> getCampuses(String remote_id, SQLiteDatabase db){

        List<Object> campuses = new ArrayList<>();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_CAMPUS + " WHERE " + DBHelper.COLUMN_PARENT + "=?";

        Cursor cursor = db.rawQuery(queryString, new String[]{remote_id});

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String campus_remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));

            campuses.add(new CampusLang(name, campus_remote_id));

        }
        cursor.close();

        return campuses;
    }

    public List<ParentObject> getCollegeList(){
        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_INSTITUTIONS;

        Cursor cursor = db.rawQuery(queryString, new String[]{});

        List<ParentObject> collegeList = new ArrayList<>();

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));
            String logo = cursor.getString(cursor.getColumnIndex("logo"));

            CollegeCountry collegeCountry = new CollegeCountry(name, remote_id, logo);
            //add campuses
            collegeCountry.setChildObjectList(getCampuses(remote_id, db));

            collegeList.add(collegeCountry);

        }
        cursor.close();

        return collegeList;
    }

    public List<InfoLink> getInformationLinks(){
        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_WEB_RESOURCES;

        Cursor cursor = db.rawQuery(queryString, new String[]{});

        List<InfoLink> infoLinks = new ArrayList<>();

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));
            String link = cursor.getString(cursor.getColumnIndex("link"));

            InfoLink infoLink = new InfoLink(name, remote_id, link);
            infoLinks.add(infoLink);

        }
        cursor.close();

        return infoLinks;
    }

    public List<CustomNumber> getCustomNumbers(){
        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_CUSTOM_NUMBER;

        Cursor cursor = db.rawQuery(queryString, new String[]{});

        List<CustomNumber> customNumbers = new ArrayList<>();

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("hotline_name"));
            String remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));
            String number = cursor.getString(cursor.getColumnIndex("phone_number"));

            CustomNumber customNumber = new CustomNumber(name, remote_id, number);
            customNumbers.add(customNumber);

        }
        cursor.close();

        return customNumbers;
    }

    public List<SMS> getSMS(){
        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_SMS;

        Cursor cursor = db.rawQuery(queryString, new String[]{});

        List<SMS> smses = new ArrayList<>();

        while (cursor.moveToNext()) {

            String label = cursor.getString(cursor.getColumnIndex("label"));
            String text = cursor.getString(cursor.getColumnIndex("text"));

            SMS sms = new SMS(label, text);
            smses.add(sms);

        }
        cursor.close();

        return smses;
    }

    public CollegeCountry getCollege(String college_id){
        CollegeCountry collegeCountry = null;

        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_INSTITUTIONS + " WHERE " + DBHelper.COLUMN_REMOTE_ID + "=?";

        Cursor cursor = db.rawQuery(queryString, new String[]{college_id});

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));
            String logo = cursor.getString(cursor.getColumnIndex("logo"));
            String welcome_message = cursor.getString(cursor.getColumnIndex("welcome_message"));
            String custom_number_message = cursor.getString(cursor.getColumnIndex("custom_number_message"));
            String terms_of_service = cursor.getString(cursor.getColumnIndex("terms_of_service"));

            collegeCountry = new CollegeCountry(name, remote_id, logo, welcome_message, custom_number_message, terms_of_service);

            cursor.close();
        }

        return collegeCountry;
    }

    public CampusLang getCampus(String campus_id){
        CampusLang campusLang = null;

        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_CAMPUS + " WHERE " + DBHelper.COLUMN_REMOTE_ID + "=?";

        Cursor cursor = db.rawQuery(queryString, new String[]{campus_id});

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String remote_id = cursor.getString(cursor.getColumnIndex("remote_id"));
            String welcome_message = cursor.getString(cursor.getColumnIndex("welcome_message"));
            String custom_number_message = cursor.getString(cursor.getColumnIndex("custom_number_message"));
            String terms_of_service = cursor.getString(cursor.getColumnIndex("terms_of_service"));

            campusLang = new CampusLang(name, remote_id, welcome_message, custom_number_message, terms_of_service);

        }
        cursor.close();

        return campusLang;
    }

    public CollegeCountry getParentCollege(String campus_id){
        String parent = "";
        SQLiteDatabase db = getWritableDatabase();

        String queryString = "SELECT * FROM " + DBHelper.TABLE_CAMPUS + " WHERE " + DBHelper.COLUMN_REMOTE_ID + "=?";

        Cursor cursor = db.rawQuery(queryString, new String[]{campus_id});

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();
            parent = cursor.getString(cursor.getColumnIndex("parent_university"));
            cursor.close();
        }

        return getCollege(parent);

    }

}
