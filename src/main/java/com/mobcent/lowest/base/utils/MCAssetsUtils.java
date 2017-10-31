package com.mobcent.lowest.base.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MCAssetsUtils {
    public static String getFromAssets(Context context, String fileName) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(fileName)));
            String line = "";
            String result = "";
            while (true) {
                line = bufReader.readLine();
                if (line == null) {
                    return result;
                }
                result = new StringBuilder(String.valueOf(result)).append(line).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
