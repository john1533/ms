package com.mobcent.lowest.module.place.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import com.mobcent.lowest.module.place.db.constant.PlaceSharedPreferenceDbConstant;

public class PlaceSharedPreferenceDb implements PlaceSharedPreferenceDbConstant {
    private static PlaceSharedPreferenceDb sharedPreferencesDB = null;
    public Context context;
    private SharedPreferences prefs = null;

    protected PlaceSharedPreferenceDb(Context cxt) {
        this.context = cxt;
        try {
            this.prefs = cxt.createPackageContext(cxt.getPackageName(), 2).getSharedPreferences(PlaceSharedPreferenceDbConstant.PREFS_FILE, 3);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PlaceSharedPreferenceDb getInstance(Context context) {
        if (sharedPreferencesDB == null) {
            sharedPreferencesDB = new PlaceSharedPreferenceDb(context);
        }
        return sharedPreferencesDB;
    }

    public static void newInstance(Context context) {
        sharedPreferencesDB = new PlaceSharedPreferenceDb(context);
    }

    public String getAreaJsonStr(String areaCode) {
        return this.prefs.getString(areaCode, null);
    }

    public void setAreaJsonStr(String areaCode, String jsonStr) {
        this.prefs.edit().putString(areaCode, jsonStr).commit();
    }
}
