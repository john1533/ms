package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.LinkedHashMap;
import java.util.Map;

public class MCWeatherUtil {
    private static MCResource mcResource;
    private static MCWeatherUtil mcWeatherUtil = null;
    private Map<Integer, Integer> weatherMap = new LinkedHashMap();

    public MCWeatherUtil(Context context) {
        mcResource = MCResource.getInstance(context);
        this.weatherMap.put(Integer.valueOf(0), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon1")));
        this.weatherMap.put(Integer.valueOf(1), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon2")));
        this.weatherMap.put(Integer.valueOf(2), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon3")));
        this.weatherMap.put(Integer.valueOf(3), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon4")));
        this.weatherMap.put(Integer.valueOf(4), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon5")));
        this.weatherMap.put(Integer.valueOf(5), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon6")));
        this.weatherMap.put(Integer.valueOf(6), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon7")));
        this.weatherMap.put(Integer.valueOf(7), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon8")));
        this.weatherMap.put(Integer.valueOf(8), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon9")));
        this.weatherMap.put(Integer.valueOf(9), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon10")));
        this.weatherMap.put(Integer.valueOf(10), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon11")));
        this.weatherMap.put(Integer.valueOf(11), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon12")));
        this.weatherMap.put(Integer.valueOf(12), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon13")));
        this.weatherMap.put(Integer.valueOf(13), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon32")));
        this.weatherMap.put(Integer.valueOf(14), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon14")));
        this.weatherMap.put(Integer.valueOf(15), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon15")));
        this.weatherMap.put(Integer.valueOf(16), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon16")));
        this.weatherMap.put(Integer.valueOf(17), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon17")));
        this.weatherMap.put(Integer.valueOf(18), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon18")));
        this.weatherMap.put(Integer.valueOf(19), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon19")));
        this.weatherMap.put(Integer.valueOf(20), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon30")));
        this.weatherMap.put(Integer.valueOf(21), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon20")));
        this.weatherMap.put(Integer.valueOf(22), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon21")));
        this.weatherMap.put(Integer.valueOf(23), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon22")));
        this.weatherMap.put(Integer.valueOf(24), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon23")));
        this.weatherMap.put(Integer.valueOf(25), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon24")));
        this.weatherMap.put(Integer.valueOf(26), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon25")));
        this.weatherMap.put(Integer.valueOf(27), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon26")));
        this.weatherMap.put(Integer.valueOf(28), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon27")));
        this.weatherMap.put(Integer.valueOf(29), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon28")));
        this.weatherMap.put(Integer.valueOf(30), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon29")));
        this.weatherMap.put(Integer.valueOf(31), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon31")));
        this.weatherMap.put(Integer.valueOf(32), Integer.valueOf(mcResource.getDrawableId("mc_forum_weather_icon33")));
    }

    public static synchronized MCWeatherUtil getInstance(Context context) {
        MCWeatherUtil mCWeatherUtil;
        synchronized (MCWeatherUtil.class) {
            if (mcWeatherUtil == null) {
                mcWeatherUtil = new MCWeatherUtil(context.getApplicationContext());
            }
            mCWeatherUtil = mcWeatherUtil;
        }
        return mCWeatherUtil;
    }

    public int getWeatherIcon(int weatherIcon) {
        if (this.weatherMap.containsKey(Integer.valueOf(weatherIcon))) {
            return ((Integer) this.weatherMap.get(Integer.valueOf(weatherIcon))).intValue();
        }
        return ((Integer) this.weatherMap.get(Integer.valueOf(0))).intValue();
    }
}
