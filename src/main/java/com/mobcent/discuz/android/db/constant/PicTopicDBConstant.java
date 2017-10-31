package com.mobcent.discuz.android.db.constant;

public interface PicTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_PIC_TOPIC = "CREATE TABLE IF NOT EXISTS pic_topic(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_PIC_TOPTIC = "SELECT * FROM pic_topic";
    public static final String TABLE_PIC_TOPIC = "pic_topic";
}
