package com.squalala.dz6android.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squalala.dz6android.utils.DateUtils;

import java.util.Date;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class Preferences  {

    private SharedPreferences preferences;

    private static final String KEY_DATE = "date";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HIDE_READ_POST = "hide_read_post";
    private static final String KEY_SPLASH = "splash";

    public Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLastDatePost(String date) {
        preferences.edit().putString(KEY_DATE, date).commit();
    }

    @DebugLog
    public void setEmail(String email) {
        preferences.edit().putString(KEY_EMAIL, email).commit();
    }

    public boolean isLoggedIn() {

        String email = preferences.getString(KEY_EMAIL, null);

        return !(email == null);
    }

    public void setSpashScreen(boolean value) {
        preferences.edit().putBoolean(KEY_SPLASH, value).commit();
    }

    public boolean isSplashScreenViewed() {
        return preferences.getBoolean(KEY_SPLASH, false);
    }

    public void setHideReadPost(boolean value) {
        preferences.edit().putBoolean(KEY_HIDE_READ_POST, value).commit();
    }

    public void incrementValue() {
        preferences.edit().putInt("note_app", getNoteAppValue() + 1).commit();
    }

    public void setValueNoteApp(int value) {
        preferences.edit().putInt("note_app", value).commit();
    }

    public int getNoteAppValue() {
        return preferences.getInt("note_app", 0);
    }

    public boolean isHideReadPost() {
        return preferences.getBoolean(KEY_HIDE_READ_POST, false);
    }

    public boolean isNotification() {
        return preferences.getBoolean("notification", true);
    }

    public boolean isLedFlash() {
        return preferences.getBoolean("led", true);
    }

    public boolean isVibreur() {
        return preferences.getBoolean("vibreur", true);
    }

    public boolean isSound() {
        return preferences.getBoolean("sound", true);
    }

    public Date getDate() {
        String dateStr = preferences.getString(KEY_DATE, null);

        if (dateStr == null)
            return null;

        return DateUtils.strToDate(dateStr);
    }

}
