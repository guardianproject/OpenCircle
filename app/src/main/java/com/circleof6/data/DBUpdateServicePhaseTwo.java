package com.circleof6.data;

/**
 * Created by nickhargreaves on 6/25/16.
 */

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.circleof6.CircleOf6Application;
import com.circleof6.preferences.AppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class DBUpdateServicePhaseTwo extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String api_url = "http://circleof6.herokuapp.com/api/";

        //first get the id
        String college_id = AppPreferences.getInstance(this).getCollegeLocation();
        String college_type = "";
        if (college_id.startsWith("c_")) {
            //this is campus
            college_id = college_id.replace("c_", "");
            college_type = "campus";
        } else {
            college_type = "university";
        }
        String institutions_url = api_url + "webresources6u/?format=json&" + college_type + "=" + college_id;
        UpdateUtil.requestContent(this, institutions_url, DBHelper.TABLE_WEB_RESOURCES, "webresources");

        String sms_url = api_url + "sms6u/?format=json&" + college_type + "=" + college_id;
        UpdateUtil.requestContent(this, sms_url, DBHelper.TABLE_SMS, "sms");

        String custom_number_url = api_url + "customnumbers6u/?format=json&" + college_type + "=" + college_id;
        UpdateUtil.requestContent(this,custom_number_url, DBHelper.TABLE_CUSTOM_NUMBER, "custom_number");
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }
}

