package com.mobcent.discuz.base.helper;

public class DZWebWhiteListHelper {
    public static boolean isInWhiteList(String url) {
        if (url.contains("wsh.appbyme.com")) {
            return true;
        }
        return false;
    }
}
