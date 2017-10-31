package com.mobcent.discuz.module.person.activity.helper;

import java.util.ArrayList;

public class EmailConstantHelper {
    private static int length = 11;
    private static String[] emailSelections = new String[length];
    private static EmailConstantHelper helper;


    private EmailConstantHelper() {
        emailSelections[0] = "@163.com";
        emailSelections[1] = "@qq.com";
        emailSelections[2] = "@gmail.com";
        emailSelections[3] = "@126.com";
        emailSelections[4] = "@139.com";
        emailSelections[5] = "@hotmail.com";
        emailSelections[6] = "@sina.cn";
        emailSelections[7] = "@sina.com";
        emailSelections[8] = "@sohu.com";
        emailSelections[9] = "@yahoo.cn";
        emailSelections[10] = "@yahoo.com.cn";
    }

    public static synchronized EmailConstantHelper getInstance() {
        EmailConstantHelper emailConstantHelper;
        synchronized (EmailConstantHelper.class) {
            if (helper == null) {
                helper = new EmailConstantHelper();
            }
            emailConstantHelper = helper;
        }
        return emailConstantHelper;
    }

    public ArrayList<String> convertEmail(String str) {
        String tstr = str;
        String[] strs = new String[length];
        ArrayList<String> tstrs = new ArrayList();
        if (str.contains("@")) {
            str = str.substring(0, str.indexOf("@"));
        }
        int size = emailSelections.length;
        for (int i = 0; i < size; i++) {
            strs[i] = str.concat(emailSelections[i]);
        }
        size = strs.length;
        for (int j = 0; j < size; j++) {
            if (strs[j].startsWith(tstr)) {
                tstrs.add(strs[j]);
            }
        }
        if (tstrs.size() == 0) {
            tstrs.add(tstr);
        }
        return tstrs;
    }
}
