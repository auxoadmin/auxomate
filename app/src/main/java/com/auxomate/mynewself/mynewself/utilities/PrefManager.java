package com.auxomate.mynewself.mynewself.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    // shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "mynewself-welcome";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String IS_FIRST_USER = "IsFirstUser";
    public static final String USER_NAME = "UserName";
    public static final String TARGET_VIEW = "Target";

    public static final String PRF_USERNAME_WELCOME = "prf_username_welcome";
    public static final String PRF_WELCOME_BOARDS = "prf_welcome_boards";
    public static final String PRF_FROMWHERE_FRAGS = "prf_fromwhere_frags";
    public static final String PRF_USERKEY = "prf_userkey";

    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatus="reminderStatus";
    public static final String tonehour="hour";
    public static final String tonemin="min";

    public static final String ttwohour="hour";
    public static final String ttwomin="min";

    private static final String tthreehour="hour";
    private static final String tthreemin="min";

    private static final String vonehour="hour";
    private static final String vonemin="min";

    private static final String vtwohour="hour";
    private static final String vtwomin="min";

    private static final String vthreehour="hour";
    private static final String vthreemin="min";


    public static final String TASK1Time = "Task 1";
    public static final String TASK2Time = "Task 2";
    public static final String TASK3Time = "Task3";
    public static final String V1Time = "V 1";
    public static final String V2Time = "V 2";
    public static final String V3Time = "V 3";

    public static final String TASK1_DES = "task1_des";
    public static final String TASK2_DES = "task2_des";
    public static final String TASK3_DES = "task3_des";


    public PrefManager(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    // Settings Page Set Reminder

    public boolean getReminderStatus()
    {
        return appSharedPrefs.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status)
    {
        prefsEditor.putBoolean(reminderStatus, status);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_tonehour()
    {
        return appSharedPrefs.getInt(tonehour, 20);
    }

    public void set_tonehour( int h)
    {
        prefsEditor.putInt(tonehour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_tonemin()
    {
        return appSharedPrefs.getInt(tonemin, 0);
    }

    public void set_tonemin(int m)
    {
        prefsEditor.putInt(tonemin, m);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_ttwohour()
    {
        return appSharedPrefs.getInt(ttwohour, 20);
    }

    public void set_ttwohour(int h)
    {
        prefsEditor.putInt(ttwohour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_ttwomin()
    {
        return appSharedPrefs.getInt(ttwomin, 0);
    }

    public void set_ttwomin(int m)
    {
        prefsEditor.putInt(ttwomin, m);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_tthreehour()
    {
        return appSharedPrefs.getInt(tthreehour, 20);
    }

    public void set_tthreehour(int h)
    {
        prefsEditor.putInt(tthreehour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_tthreemin()
    {
        return appSharedPrefs.getInt(tthreemin, 0);
    }

    public void set_tthreemin(int m)
    {
        prefsEditor.putInt(tthreemin, m);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_vonehour()
    {
        return appSharedPrefs.getInt(vonehour, 20);
    }

    public void set_vonehour(int h)
    {
        prefsEditor.putInt(vonehour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_vonemin()
    {
        return appSharedPrefs.getInt(vonemin, 0);
    }

    public void set_vonemin(int m)
    {
        prefsEditor.putInt(vonemin, m);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_vtwohour()
    {
        return appSharedPrefs.getInt(vtwohour, 20);
    }

    public void set_vtwohour(int h)
    {
        prefsEditor.putInt(vtwohour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_vtwomin()
    {
        return appSharedPrefs.getInt(vtwomin, 0);
    }

    public void set_vtwoemin(int m)
    {
        prefsEditor.putInt(vtwomin, m);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_vthreehour()
    {
        return appSharedPrefs.getInt(vthreehour, 20);
    }

    public void set_vthreehour(int h)
    {
        prefsEditor.putInt(vthreehour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_vthreemin()
    {
        return appSharedPrefs.getInt(vthreemin, 0);
    }

    public void set_vthreemin(int m)
    {
        prefsEditor.putInt(vthreemin, m);
        prefsEditor.commit();
    }

    public void reset()
    {
        prefsEditor.clear();
        prefsEditor.commit();

    }


    public static void setFirstTimeLaunch(Context context,boolean isFirstTime) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences pref;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void putString(Context context, String key, String value){

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putString(key,value);
        editor.commit();
        editor.apply();

    }

    public static String getString(Context context, String key){

        SharedPreferences pref;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getString(key,"");

    }

    public static void putInt(Context context, String key, int value){

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putInt(key,value);
        editor.commit();
        editor.apply();

    }

    public static int getInt(Context context, String key){

        SharedPreferences pref;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getInt(key,0);

    }

    public static void putBoolean(Context context, String key, boolean value){

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(key,value);
        editor.commit();
        editor.apply();

    }

    public static boolean getBoolean(Context context, String key){

        SharedPreferences pref;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(key,false);

    }




}