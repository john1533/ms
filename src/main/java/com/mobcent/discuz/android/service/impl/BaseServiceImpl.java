package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.db.SharedPreferencesDB;

public class BaseServiceImpl {
    protected Context context;
    protected SharedPreferencesDB db = SharedPreferencesDB.getInstance(this.context);

    public BaseServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }
}
