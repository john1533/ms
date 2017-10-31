package com.mobcent.lowest.base.utils;

import android.support.v4.view.MotionEventCompat;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCStringUtil {
    public static boolean isEmpty(String s) {
        if (s == null || s.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isEmail(String strEmail) {
        return Pattern.compile("^([a-z0-9A-Z_]+[-\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$").matcher(strEmail).matches();
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static boolean isPwdMatchRule(String pwd) {
        if (pwd == null) {
            return false;
        }
        return Pattern.compile("[A-Za-z0-9]+").matcher(pwd).matches();
    }

    public static int getChineseCount(String str) {
        int count = 0;
        Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count++;
            }
        }
        return count;
    }

    public static boolean isChinese(String str) {
        return Pattern.matches("[一-龥]", str);
    }

    public static boolean isNameMatchRule(String name) {
        if (name == null) {
            return false;
        }
        return Pattern.compile("[A-Za-z0-9_]+").matcher(name).matches();
    }

    public static boolean isNickNameMatchRule(String nickName) {
        int len = nickName.length();
        for (int i = 0; i < len; i++) {
            String str = nickName.substring(i, i + 1);
            if (!isChinese(str) && !isNameMatchRule(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkNickNameMinLen(String str, int len) {
        int chinaCount = getChineseCount(str);
        if ((chinaCount * 2) + (str.length() - chinaCount) < len) {
            return false;
        }
        return true;
    }

    public static boolean checkNickNameMaxLen(String str, int len) {
        int chinaCount = getChineseCount(str);
        if ((chinaCount * 2) + (str.length() - chinaCount) > len) {
            return false;
        }
        return true;
    }

    public static String getExtensionName(String filename) {
        if (filename == null || filename.length() <= 0) {
            return filename;
        }
        int dot = filename.lastIndexOf(46);
        if (dot <= -1 || dot >= filename.length() - 1) {
            return filename;
        }
        return filename.substring(dot + 1);
    }

    public static String getMD5Str(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            url = new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return url;
    }

    public static String stringToMD5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & MotionEventCompat.ACTION_MASK) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & MotionEventCompat.ACTION_MASK));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static List<String> getTokenizer(String str, String split) {
        if (str == null || str.isEmpty()) {
            return new ArrayList();
        }
        List<String> strs = new ArrayList();
        StringTokenizer strToken = new StringTokenizer(str, split);
        while (strToken.hasMoreTokens()) {
            strs.add(strToken.nextToken());
        }
        return strs;
    }

    public static String formatImgUrl(String url, String resolution) {
        if (url == null) {
            return "";
        }
        if (resolution == null || !url.contains("xgsize")) {
            return url;
        }
        return url.replace("xgsize", resolution);
    }

    public static boolean isUrl(String urlString) {
        if (Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]").matcher(urlString).matches()) {
            return true;
        }
        return false;
    }

    public static String subString(String str, int len) {
        if (isEmpty(str) || str.length() <= len) {
            return str;
        }
        return str.substring(0, len) + "...";
    }
}
