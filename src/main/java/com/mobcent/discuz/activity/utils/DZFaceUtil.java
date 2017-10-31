package com.mobcent.discuz.activity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.mobcent.discuz.android.util.DZFaceLoaderUtils;
import com.mobcent.discuz.android.util.DZFaceLoaderUtils.BitmapImageCallback;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class DZFaceUtil {
    public static String TAG = "MCFaceUtil";
    private static LinkedHashMap<String, Integer> allHashMap = new LinkedHashMap();
    private static LinkedHashMap<String, Integer> allMHashMap = new LinkedHashMap();
    private static LinkedHashMap<String, Integer> allQQHashMap = new LinkedHashMap();
    static int endRight = -1;
    private static DZFaceUtil mcFaceUtil;
    private static MCResource resource;
    static int startLeft = -1;

    static class Clickable extends ClickableSpan implements OnClickListener {
        private final OnClickListener mListener;

        public Clickable(OnClickListener l) {
            this.mListener = l;
        }

        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        public void onClick(View v) {
            this.mListener.onClick(v);
        }
    }

    private DZFaceUtil(Context context) {
        resource = MCResource.getInstance(context);
        allHashMap.put(resource.getString("mc_forum_face_tear"), Integer.valueOf(resource.getDrawableId("mc_forum_face198")));
        allHashMap.put(resource.getString("mc_forum_face_haha"), Integer.valueOf(resource.getDrawableId("mc_forum_face234")));
        allHashMap.put(resource.getString("mc_forum_face_crazy"), Integer.valueOf(resource.getDrawableId("mc_forum_face239")));
        allHashMap.put(resource.getString("mc_forum_face_xixi"), Integer.valueOf(resource.getDrawableId("mc_forum_face233")));
        allHashMap.put(resource.getString("mc_forum_face_titter"), Integer.valueOf(resource.getDrawableId("mc_forum_face247")));
        allHashMap.put(resource.getString("mc_forum_face_clap"), Integer.valueOf(resource.getDrawableId("mc_forum_face255")));
        allHashMap.put(resource.getString("mc_forum_face_fury"), Integer.valueOf(resource.getDrawableId("mc_forum_face242")));
        allHashMap.put(resource.getString("mc_forum_face_heart"), Integer.valueOf(resource.getDrawableId("mc_forum_face279")));
        allHashMap.put(resource.getString("mc_forum_face_heart_broken"), Integer.valueOf(resource.getDrawableId("mc_forum_face280")));
        allHashMap.put(resource.getString("mc_forum_face_sick"), Integer.valueOf(resource.getDrawableId("mc_forum_face258")));
        allHashMap.put(resource.getString("mc_forum_face_love"), Integer.valueOf(resource.getDrawableId("mc_forum_face17")));
        allHashMap.put(resource.getString("mc_forum_face_shy"), Integer.valueOf(resource.getDrawableId("mc_forum_face201")));
        allHashMap.put(resource.getString("mc_forum_face_poor"), Integer.valueOf(resource.getDrawableId("mc_forum_face268")));
        allHashMap.put(resource.getString("mc_forum_face_greedy"), Integer.valueOf(resource.getDrawableId("mc_forum_face238")));
        allHashMap.put(resource.getString("mc_forum_face_faint"), Integer.valueOf(resource.getDrawableId("mc_forum_face7")));
        allHashMap.put(resource.getString("mc_forum_face_hana"), Integer.valueOf(resource.getDrawableId("mc_forum_face254")));
        allHashMap.put(resource.getString("mc_forum_face_too_happy"), Integer.valueOf(resource.getDrawableId("mc_forum_face261")));
        allHashMap.put(resource.getString("mc_forum_face_kiss"), Integer.valueOf(resource.getDrawableId("mc_forum_face259")));
        allHashMap.put(resource.getString("mc_forum_face_disdain"), Integer.valueOf(resource.getDrawableId("mc_forum_face252")));
        allHashMap.put(resource.getString("mc_forum_face_hehe"), Integer.valueOf(resource.getDrawableId("mc_forum_face25")));
        allHashMap.put(resource.getString("mc_forum_face_booger"), Integer.valueOf(resource.getDrawableId("mc_forum_face253")));
        allHashMap.put(resource.getString("mc_forum_face_decline"), Integer.valueOf(resource.getDrawableId("mc_forum_face6")));
        allHashMap.put(resource.getString("mc_forum_face_rabbit"), Integer.valueOf(resource.getDrawableId("mc_forum_rabbit_thumb")));
        allHashMap.put(resource.getString("mc_forum_face_good"), Integer.valueOf(resource.getDrawableId("mc_forum_face100")));
        allHashMap.put(resource.getString("mc_forum_face_come"), Integer.valueOf(resource.getDrawableId("mc_forum_face277")));
        allHashMap.put(resource.getString("mc_forum_face_force"), Integer.valueOf(resource.getDrawableId("mc_forum_face219")));
        allHashMap.put(resource.getString("mc_forum_face_circusee"), Integer.valueOf(resource.getDrawableId("mc_forum_face218")));
        allHashMap.put(resource.getString("mc_forum_face_sprout"), Integer.valueOf(resource.getDrawableId("mc_forum_kawayi_thumb")));
        allHashMap.put(resource.getString("mc_forum_face_flowers"), Integer.valueOf(resource.getDrawableId("mc_forum_face120")));
        allHashMap.put(resource.getString("mc_forum_face_confused"), Integer.valueOf(resource.getDrawableId("mc_forum_face121")));
        allQQHashMap.put(resource.getString("mc_forum_face_cruel"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_01")));
        allQQHashMap.put(resource.getString("mc_forum_face_solid_food"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_02")));
        allQQHashMap.put(resource.getString("mc_forum_face_mouth"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_03")));
        allQQHashMap.put(resource.getString("mc_forum_face_asleep"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_04")));
        allQQHashMap.put(resource.getString("mc_forum_face_sweat"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_05")));
        allQQHashMap.put(resource.getString("mc_forum_face_sleep"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_06")));
        allQQHashMap.put(resource.getString("mc_forum_face_surprise"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_11")));
        allQQHashMap.put(resource.getString("mc_forum_face_white_eye"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_12")));
        allQQHashMap.put(resource.getString("mc_forum_face_white_query"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_13")));
        allQQHashMap.put(resource.getString("mc_forum_face_cattiness"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_14")));
        allQQHashMap.put(resource.getString("mc_forum_face_left_humph"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_15")));
        allQQHashMap.put(resource.getString("mc_forum_face_right_humph"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_16")));
        allQQHashMap.put(resource.getString("mc_forum_face_knock"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_21")));
        allQQHashMap.put(resource.getString("mc_forum_face_chagrin"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_22")));
        allQQHashMap.put(resource.getString("mc_forum_face_hush"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_23")));
        allQQHashMap.put(resource.getString("mc_forum_face_spit"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_24")));
        allQQHashMap.put(resource.getString("mc_forum_face_grimace"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_25")));
        allQQHashMap.put(resource.getString("mc_forum_face_bye"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_26")));
        allQQHashMap.put(resource.getString("mc_forum_face_cry"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_31")));
        allQQHashMap.put(resource.getString("mc_forum_face_arrogance"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_32")));
        allQQHashMap.put(resource.getString("mc_forum_face_moon"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_33")));
        allQQHashMap.put(resource.getString("mc_forum_face_sun"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_34")));
        allQQHashMap.put(resource.getString("mc_forum_face_ye"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_35")));
        allQQHashMap.put(resource.getString("mc_forum_face_handshake"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_36")));
        allQQHashMap.put(resource.getString("mc_forum_face_ok"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_41")));
        allQQHashMap.put(resource.getString("mc_forum_face_coffee"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_42")));
        allQQHashMap.put(resource.getString("mc_forum_face_meal"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_43")));
        allQQHashMap.put(resource.getString("mc_forum_face_gift"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_44")));
        allQQHashMap.put(resource.getString("mc_forum_face_pig_head"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_45")));
        allQQHashMap.put(resource.getString("mc_forum_face_hug"), Integer.valueOf(resource.getDrawableId("mc_forum_qq_46")));
        allMHashMap.put(resource.getString("mc_forum_face_praise"), Integer.valueOf(resource.getDrawableId("mc_forum_m_01")));
        allMHashMap.put(resource.getString("mc_forum_face_hold"), Integer.valueOf(resource.getDrawableId("mc_forum_m_02")));
        allMHashMap.put(resource.getString("mc_forum_face_sleipnir"), Integer.valueOf(resource.getDrawableId("mc_forum_m_03")));
        allMHashMap.put(resource.getString("mc_forum_face_cheating"), Integer.valueOf(resource.getDrawableId("mc_forum_m_04")));
        allMHashMap.put(resource.getString("mc_forum_face_have"), Integer.valueOf(resource.getDrawableId("mc_forum_m_05")));
        allMHashMap.put(resource.getString("mc_forum_face_thanks"), Integer.valueOf(resource.getDrawableId("mc_forum_m_06")));
        allMHashMap.put(resource.getString("mc_forum_face_demon"), Integer.valueOf(resource.getDrawableId("mc_forum_m_11")));
        allMHashMap.put(resource.getString("mc_forum_face_saucer_man"), Integer.valueOf(resource.getDrawableId("mc_forum_m_12")));
        allMHashMap.put(resource.getString("mc_forum_face_blue_heart"), Integer.valueOf(resource.getDrawableId("mc_forum_m_13")));
        allMHashMap.put(resource.getString("mc_forum_face_purple_heart"), Integer.valueOf(resource.getDrawableId("mc_forum_m_14")));
        allMHashMap.put(resource.getString("mc_forum_face_yellow_heart"), Integer.valueOf(resource.getDrawableId("mc_forum_m_15")));
        allMHashMap.put(resource.getString("mc_forum_face_green_heart"), Integer.valueOf(resource.getDrawableId("mc_forum_m_16")));
        allMHashMap.put(resource.getString("mc_forum_face_music"), Integer.valueOf(resource.getDrawableId("mc_forum_m_21")));
        allMHashMap.put(resource.getString("mc_forum_face_flicker"), Integer.valueOf(resource.getDrawableId("mc_forum_m_22")));
        allMHashMap.put(resource.getString("mc_forum_face_star"), Integer.valueOf(resource.getDrawableId("mc_forum_m_23")));
        allMHashMap.put(resource.getString("mc_forum_face_drip"), Integer.valueOf(resource.getDrawableId("mc_forum_m_24")));
        allMHashMap.put(resource.getString("mc_forum_face_flame"), Integer.valueOf(resource.getDrawableId("mc_forum_m_25")));
        allMHashMap.put(resource.getString("mc_forum_face_shit"), Integer.valueOf(resource.getDrawableId("mc_forum_m_26")));
        allMHashMap.put(resource.getString("mc_forum_face_foot"), Integer.valueOf(resource.getDrawableId("mc_forum_m_31")));
        allMHashMap.put(resource.getString("mc_forum_face_rain"), Integer.valueOf(resource.getDrawableId("mc_forum_m_32")));
        allMHashMap.put(resource.getString("mc_forum_face_cloudy"), Integer.valueOf(resource.getDrawableId("mc_forum_m_33")));
        allMHashMap.put(resource.getString("mc_forum_face_lightning"), Integer.valueOf(resource.getDrawableId("mc_forum_m_34")));
        allMHashMap.put(resource.getString("mc_forum_face_snowflake"), Integer.valueOf(resource.getDrawableId("mc_forum_m_35")));
        allMHashMap.put(resource.getString("mc_forum_face_cyclone"), Integer.valueOf(resource.getDrawableId("mc_forum_m_36")));
        allMHashMap.put(resource.getString("mc_forum_face_bag"), Integer.valueOf(resource.getDrawableId("mc_forum_m_41")));
        allMHashMap.put(resource.getString("mc_forum_face_house"), Integer.valueOf(resource.getDrawableId("mc_forum_m_42")));
        allMHashMap.put(resource.getString("mc_forum_face_fireworks"), Integer.valueOf(resource.getDrawableId("mc_forum_m_43")));
    }

    public static DZFaceUtil getFaceConstant(Context context) {
        if (mcFaceUtil == null) {
            mcFaceUtil = new DZFaceUtil(context);
        }
        return mcFaceUtil;
    }

    public LinkedHashMap<String, Integer> getAllFaceMap() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap();
        map.putAll(allHashMap);
        map.putAll(allQQHashMap);
        map.putAll(allMHashMap);
        return map;
    }

    public static String replaceAll(String text, LinkedHashMap<String, String> dzfaceMap) {
        for (String key : dzfaceMap.keySet()) {
            text = text.replace((CharSequence) dzfaceMap.get(key), key);
        }
        return text;
    }

    public static String getFaceUrl(String str) {
        return str.substring(str.indexOf("=") + 1, str.indexOf("]"));
    }

    public static void setStrToFace(TextView tv, String text, Context context) {
        try {
            LinkedHashMap<String, Integer> faceMap = getFaceConstant(context).getAllFaceMap();
            LinkedHashMap<String, String> dzfaceMap = new LinkedHashMap();
            int index = 0;
            boolean isHavaLeftFace = false;
            if (!MCStringUtil.isEmpty(text)) {
                int i;
                char charText;
                String faceStr;
                for (i = 0; i < text.length(); i++) {
                    charText = text.charAt(i);
                    if (charText == '[') {
                        startLeft = i;
                        isHavaLeftFace = true;
                    }
                    if (isHavaLeftFace && charText == ']') {
                        endRight = i;
                        faceStr = text.substring(startLeft, endRight + 1);
                        if (faceStr.contains("mobcent_phiz=")) {
                            dzfaceMap.put("[DZFace" + index + "]", faceStr);
                            index++;
                        }
                        isHavaLeftFace = false;
                    }
                }
                text = replaceAll(text, dzfaceMap);
                final SpannableString sp = new SpannableString(text);
                for (i = 0; i < text.length(); i++) {
                    charText = text.charAt(i);
                    if (charText == '[') {
                        startLeft = i;
                        isHavaLeftFace = true;
                    }
                    if (isHavaLeftFace && charText == ']') {
                        endRight = i;
                        Set<String> faceNameSet = faceMap.keySet();
                        try {
                            faceStr = text.substring(startLeft, endRight + 1);
                            Drawable d;
                            if (faceNameSet.contains(faceStr)) {
                                d = context.getResources().getDrawable(((Integer) faceMap.get(faceStr)).intValue());
                                d.setBounds(0, 0, (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_width")), (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_height")));
                                sp.setSpan(new ImageSpan(d, 1), startLeft, endRight + 1, 17);
                            } else if (dzfaceMap.keySet().contains(faceStr)) {
                                d = context.getResources().getDrawable(resource.getDrawableId("mc_forum_face233"));
                                d.setBounds(0, 0, (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_width")), (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_height")));
                                sp.setSpan(new ImageSpan(d, 1), startLeft, endRight + 1, 17);
                                faceStr = (String) dzfaceMap.get(faceStr);
                                final Context context2 = context;
                                final TextView textView = tv;
                                DZFaceLoaderUtils.getInstance(context).loadAsync(faceStr.substring(14, faceStr.length() - 1), startLeft, endRight + 1, new BitmapImageCallback() {
                                    public void onImageLoaded(Bitmap bitmap, String url, int startIndex, int endIndex) {
                                        float width = (float) bitmap.getWidth();
                                        float height = (float) bitmap.getHeight();
                                        Drawable d = new BitmapDrawable(bitmap);
                                        d.setBounds(0, 0, (int) (context2.getResources().getDimension(DZFaceUtil.resource.getDimenId("mc_forum_face_width")) / (height / width)), (int) context2.getResources().getDimension(DZFaceUtil.resource.getDimenId("mc_forum_face_height")));
                                        sp.setSpan(new ImageSpan(d, 1), startIndex, endIndex, 17);
                                        textView.setText(sp);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isHavaLeftFace = false;
                    }
                }
                tv.setText(sp);
            }
        } catch (Exception e2) {
        }
    }

    public static void setStrToFaceWithSpan(TextView tv, String text, Context context, List<DZClickSpan> clickSpanList) {
        int i;
        LinkedHashMap<String, Integer> faceMap = getFaceConstant(context).getAllFaceMap();
        SpannableString sp = new SpannableString(text);
        tv.setText(sp);
        boolean isHavaLeftFace = false;
        int startLeft = -1;
        for (i = 0; i < text.length(); i++) {
            char charText = text.charAt(i);
            if (charText == '[') {
                startLeft = i;
                isHavaLeftFace = true;
            }
            if (isHavaLeftFace && charText == ']') {
                int endRight = i;
                Set<String> faceNameSet = faceMap.keySet();
                String faceStr = text.substring(startLeft, endRight + 1);
                if (faceNameSet.contains(faceStr)) {
                    Drawable d = context.getResources().getDrawable(((Integer) faceMap.get(faceStr)).intValue());
                    d.setBounds(0, 0, (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_width")), (int) context.getResources().getDimension(resource.getDimenId("mc_forum_face_height")));
                    sp.setSpan(new ImageSpan(d, 1), startLeft, endRight + 1, 17);
                }
                isHavaLeftFace = false;
            }
        }
        int s = clickSpanList.size();
        for (i = 0; i < s; i++) {
            DZClickSpan clickSpan = (DZClickSpan) clickSpanList.get(i);
            if (clickSpan.getClickListener() != null) {
                sp.setSpan(new Clickable(clickSpan.getClickListener()), clickSpan.getStartPostion(), clickSpan.getEndPostion(), 33);
            }
            sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(resource.getColorId(clickSpan.getColorName()))), clickSpan.getStartPostion(), clickSpan.getEndPostion(), 33);
        }
        tv.setText(sp);
    }

    public List<LinkedHashMap> getFaceList() {
        List<LinkedHashMap> list = new ArrayList();
        list.add(allHashMap);
        list.add(allQQHashMap);
        list.add(allMHashMap);
        return list;
    }
}
