package com.mobcent.lowest.module.ad.utils;

import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.lowest.module.ad.api.BaseAdApiRequester;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdApkModel;
import java.net.URL;

public class AdStringUtils implements AdConstant, AdApiConstant {
    public static String parseImgUrl(int dt, String url) {
        String mobcentBase = "http://adapi.mobcent.com/";
        if (!url.contains(BaseAdApiRequester.BASE_URL + "img/") && !url.contains(mobcentBase)) {
            return url;
        }
        String fileType = getUrlLastName(url);
        if (dt == 3) {
            url = new StringBuilder(String.valueOf(url)).append(".").append(AdConstant.RESOLUTION_320X50).toString();
        } else if (dt == 4) {
            url = new StringBuilder(String.valueOf(url)).append(".").append(AdConstant.RESOLUTION_160X50).toString();
        } else if (dt == 9) {
            url = new StringBuilder(String.valueOf(url)).append(".").append(AdConstant.RESOLUTION_320X380).toString();
        } else if (dt == 12) {
            url = new StringBuilder(String.valueOf(url)).append(".").append("100x100").toString();
        } else if (dt == 13) {
            url = new StringBuilder(String.valueOf(url)).append(".").append(AdConstant.RESOLUTION_576X288).toString();
        }
        return new StringBuilder(String.valueOf(url)).append(fileType).toString();
    }

    private static String getUrlLastName(String url) {
        if (url == null || "".equals(url)) {
            return "";
        }
        url = url.trim();
        return url.substring(url.lastIndexOf("."), url.length());
    }

    public static AdApkModel parseAdApkModel(String url) {
        AdApkModel apkModel = new AdApkModel();
        if (!(url == null || url.equals(""))) {
            try {
                String[] strs = new URL(url).getQuery().split("&");
                int len = strs.length;
                for (int i = 0; i < len; i++) {
                    if (strs[i].contains("=")) {
                        int index = strs[i].indexOf("=");
                        String key = strs[i].substring(0, index);
                        String value = strs[i].substring(index + 1, strs[i].length());
                        if (key.equals("appName")) {
                            apkModel.setAppName(value);
                        } else if (key.equals("packageName")) {
                            apkModel.setPackageName(value);
                        } else if (key.equals(AdApiConstant.APP_SIZE)) {
                            apkModel.setAppSize(Integer.parseInt(value));
                        } else {
                            key.equals(AdApiConstant.APP_CLOSE);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return apkModel;
    }

    public static boolean isGif(String url) {
        if (url == null || "".equals(url) || !getUrlLastName(url.toLowerCase()).contains(FinalConstant.PIC_GIF)) {
            return false;
        }
        return true;
    }
}
