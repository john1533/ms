package com.mobcent.discuz.android.db;

import android.content.Context;
import android.content.SharedPreferences;
import com.mobcent.discuz.android.db.constant.SharedPreferencesDBConstant;
import com.mobcent.discuz.android.model.PersonalSettingModel;

public class SettingSharePreference implements SharedPreferencesDBConstant {
    private Context context;
    private SharedPreferences pref;

    public SettingSharePreference(Context context) {
        this.context = context.getApplicationContext();
        this.pref = context.getApplicationContext().getSharedPreferences(SharedPreferencesDBConstant.SETTING_PREFS, 3);
    }

    public PersonalSettingModel querySetting(long userId) {
        PersonalSettingModel setting = new PersonalSettingModel();
        setting.setReplyNotify(isReplyNotify(userId));
        setting.setAtNotify(isAtNotify(userId));
        setting.setPicAvailable(isPicAvailable(userId));
        setting.setSoundOpen(isSoundOpen(userId));
        setting.setLocationOpen(isLocationOpen(userId));
        setting.setLocationAvailable(isLocationAvailable(userId));
        return setting;
    }

    public void updateSetting(PersonalSettingModel setting, long userId) {
        if (setting != null) {
            setReplyNotify(setting.isReplyNotify(), userId);
            setAtNotify(setting.isAtNotify(), userId);
            setSoundOpen(setting.isSoundOpen(), userId);
            setPicAvailable(setting.isPicAvailable(), userId);
            setLocationOpen(setting.isLocationOpen(), userId);
            setLocationAvailable(setting.isLocationAvailable(), userId);
        }
    }

    public boolean isReplyNotify(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_REPLY_NOTIFY).append(userId).toString(), true);
    }

    public void setReplyNotify(boolean isReplyNotify, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_REPLY_NOTIFY).append(userId).toString(), isReplyNotify).commit();
    }

    public boolean isAtNotify(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_AT_NOTIFY).append(userId).toString(), true);
    }

    public void setAtNotify(boolean isAtNotify, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_AT_NOTIFY).append(userId).toString(), isAtNotify).commit();
    }

    public boolean isSoundOpen(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_SOUND_OPEN).append(userId).toString(), true);
    }

    public void setSoundOpen(boolean isSoundOpen, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_SOUND_OPEN).append(userId).toString(), isSoundOpen).commit();
    }

    public boolean isLocationOpen(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_LOCATION_OPEN).append(userId).toString(), true);
    }

    public void setLocationOpen(boolean isLocationOpen, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_LOCATION_OPEN).append(userId).toString(), isLocationOpen).commit();
    }

    public boolean isLocationAvailable(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_LOCATION_AVAILABLE).append(userId).toString(), true);
    }

    public void setLocationAvailable(boolean isLocationAvailable, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_LOCATION_AVAILABLE).append(userId).toString(), isLocationAvailable).commit();
    }

    public boolean isPicAvailable(long userId) {
        return this.pref.getBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_PIC_AVAILABLE).append(userId).toString(), true);
    }

    public boolean isPicAvailable() {
        return isPicAvailable(SharedPreferencesDB.getInstance(this.context).getUserId());
    }

    public void setPicAvailable(boolean isPicAvailable, long userId) {
        this.pref.edit().putBoolean(new StringBuilder(SharedPreferencesDBConstant.IS_PIC_AVAILABLE).append(userId).toString(), isPicAvailable).commit();
    }
}
