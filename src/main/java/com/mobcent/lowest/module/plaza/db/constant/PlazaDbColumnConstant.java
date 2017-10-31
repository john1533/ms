package com.mobcent.lowest.module.plaza.db.constant;

public interface PlazaDbColumnConstant {
    public static final String CREATE_TABLE = "create table t_plaza (id integer primary key autoincrement,jsonStr TEXT)";
    public static final String DROP_TABLE = "drop table t_plaza";
    public static final String ID = "id";
    public static final String JSON_STR = "jsonStr";
    public static final String PLAZA_DB_NAME = "plaza.db";
    public static final String TABLE_NAME = "t_plaza";
    public static final int VERSION = 1;
}
