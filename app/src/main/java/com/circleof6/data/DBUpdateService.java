package com.circleof6.data;

/**
 * Created by nickhargreaves on 6/25/16.
 */

import android.app.Service;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.circleof6.CircleOf6Application;
import com.circleof6.preferences.AppPreferences;

import java.util.Iterator;

public class DBUpdateService extends Service {

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

        //get campuses
        String campuses_url = api_url + "campuses" + "/?format=json";
        UpdateUtil.requestContent(this, campuses_url, DBHelper.TABLE_CAMPUS, "campuses");
        //get universities
        String institutions_url = api_url + "universities" + "/?format=json";
        UpdateUtil. requestContent(this, institutions_url, DBHelper.TABLE_INSTITUTIONS, "institutions");

    }

    @Override
    public void onStart(Intent intent, int startId) {

    }
}

