package com.circleof6.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Edgar Salvador Maurilio on 11/11/2015.
 */
public class PreferencesHelper {

    private SharedPreferences mSp;

    public PreferencesHelper(Context context, String namePreferences) {
        this.mSp = context.getSharedPreferences(namePreferences,Context.MODE_PRIVATE);
    }

    public String getString(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }


    public void applyPreference(String key, String value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearPreference(String key)
    {
        SharedPreferences.Editor editor = mSp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void applyPreference(String key, int value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void applyPreference(String key, boolean value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}
