package com.circleof6.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.circleof6.CircleOf6Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by nickhargreaves on 7/4/16.
 */
public class UpdateUtil {

    public static void requestContent(final Context ctx, String url, final String table, final String tag_json_obj){

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray array) {

                        addContent(array, table, ctx);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Service", "Error: " + error);

            }
        });

        // Adding request to request queue
        CircleOf6Application.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public static void addContent(JSONArray array, String table, Context ctx){
        DBHelper dbHelper = new DBHelper(ctx);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        final SharedPreferences.Editor editor = pref.edit();

        try {

            JSONArray returnArray = new JSONArray();

            for(int i = 0; i<array.length(); i++){
                JSONObject singleItem = array.getJSONObject(i);


                ContentValues values = new ContentValues();
                values.put("table_name", table); //Let's pop this value before inserting

                Iterator<?> keys = singleItem.keys();

                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    String value = singleItem.getString(key);

                    if(key.equals("id"))
                        key = "remote_id";
                    //check if table has column
                    if(hasColumn(table, key, db)) {
                        values.put(key, value);
                    }
                }

                //if item is not added insert
                String id = singleItem.getString("id");
                if(!isAdded(db, id, table)) {
                    ctx.getContentResolver().insert(CustomContentProvider.CONTENT_URI, values);
                    returnArray.put(singleItem);
                }else{
                    //else update
                    ctx.getContentResolver().update(CustomContentProvider.CONTENT_URI, values, DBHelper.COLUMN_REMOTE_ID + "=" + id, null);
                }
            }
            editor.putBoolean(table, true);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasColumn(String tableName, String fieldName, SQLiteDatabase db){
    {
        boolean isExist = false;
        Cursor res = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);


        if (res.moveToFirst()) {
            do {
                int value = res.getColumnIndex("name");
                if(value != -1 && res.getString(value).equals(fieldName))
                {
                    isExist = true;
                }

            } while (res.moveToNext());
        }

        res.close();

            return isExist;
        }
    }

    public static boolean isAdded(SQLiteDatabase db, String remote_id, String TABLE_NAME){
        String select_string =  "select count(*) from "+TABLE_NAME+" where remote_id='" + remote_id + "'; ";

        SQLiteStatement s = db.compileStatement(select_string );

        long count = s.simpleQueryForLong();

        if(count > 0){

            return true;
        }

        return false;
    }
}
