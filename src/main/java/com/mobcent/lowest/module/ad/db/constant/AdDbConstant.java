package com.mobcent.lowest.module.ad.db.constant;

public interface AdDbConstant {
    public static final String CREATE_TABLE_DOWNLOAD = "CREATE TABLE IF NOT EXISTS download_t(download_id integer primary key autoincrement,download_aid LONG,download_url TEXT,appName TEXT,download_status INTEGER,download_po INTEGER,download_date VARCHAR(100),current_pn VARCHAR(100),download_pn VARCHAR(100), downloadDo INTEGER,activeDo INTEGER );";
    public static final String DATABASE_NAME = "mc_ad_db";
    public static final String DOWNLOAD_AID = "download_aid";
    public static final String DOWNLOAD_APPNAME = "appName";
    public static final String DOWNLOAD_CURRENT_PN = "current_pn";
    public static final String DOWNLOAD_DATE = "download_date";
    public static final String DOWNLOAD_ID = "download_id";
    public static final String DOWNLOAD_IS_ACTIVE_DO = "activeDo";
    public static final String DOWNLOAD_IS_DOWNLOAD_DO = "downloadDo";
    public static final String DOWNLOAD_PN = "download_pn";
    public static final String DOWNLOAD_PO = "download_po";
    public static final String DOWNLOAD_STATUS = "download_status";
    public static final String DOWNLOAD_TABLE = "download_t";
    public static final String DOWNLOAD_URL = "download_url";
    public static final String DROP_TABLE_DOWNLOAD = "drop table download_t";
    public static final int IS_FAILED = 2;
    public static final int IS_SUCC = 1;
    public static final int STATUS_DOING = 1;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_FINISHED = 2;
    public static final int VERSION = 1;
}
