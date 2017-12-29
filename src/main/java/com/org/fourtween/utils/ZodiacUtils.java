package com.org.fourtween.utils;

import java.util.Calendar;

/**
 * Created by John on 2017/12/20.
 */

public class ZodiacUtils {
//获取当年的 index, index+1为totalExpect, totalExpect-ma%12为所取值的index,若idex<-1,则index=index+12
    enum ZodiaEnum{
        HOU("猴",0),
        JI("鸡",1),
        GOU("狗",2),
        ZHU("猪",3),
        SHU("鼠",4),
        NIU("牛",5),
        HU("虎",6),
        TU("兔",7),
        LONG("龙",8),
        SHE("蛇",9),
        MA("马",10),
        YANG("羊",11);
        private String name;
        private int index;

        public int getIndex() {
            return index;
        }


        public static String getName(int index) {
            for (ZodiaEnum c : ZodiaEnum.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        ZodiaEnum(String name,int index){
            this.name = name;
            this.index = index;
        }
    }







    public static String getZodiaName(int num){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int magicNum = year%12;
        int totalExpect = magicNum + 1;
        int magicNum1 = totalExpect - num%12;
        if (magicNum1<0){
            magicNum1 += 12;
        }else if(magicNum1 == 12){
            magicNum1 = 0;
        }
        return ZodiaEnum.getName(magicNum1);
    }
}
