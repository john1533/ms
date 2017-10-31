package com.mobcent.discuz.android.db.constant;

public interface NewsTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_NEWS_TOPIC = "CREATE TABLE IF NOT EXISTS news_topic(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_NEWS_TOPTIC = "SELECT * FROM news_topic";
    public static final String TABLE_NEWS_TOPIC = "news_topic";
}
