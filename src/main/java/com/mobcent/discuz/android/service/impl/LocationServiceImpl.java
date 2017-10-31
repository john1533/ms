package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.LocationRestfulApiRequester;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.service.LocationService;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationServiceImpl implements LocationService {
    private Context context;

    public LocationServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean saveLocation(double longitude, double latitude, String location) {
        try {
            if (new JSONObject(LocationRestfulApiRequester.saveLocation(this.context, longitude, latitude, location, SharedPreferencesDB.getInstance(this.context).getUserId())).optInt("rs") == 0) {
                return false;
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
