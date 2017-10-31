package com.mobcent.lowest.module.game.db.constant;

public interface GameDbSqlConstant extends GameDbTableConstant {
    public static final String LATEST_TABLE_SQL = "CREATE TABLE IF NOT EXISTS t_latest ( id INTEGER PRIMARY KEY AUTOINCREMENT,tag_name VARCHAR(255),current_page INTEGER,json_str TEXT);";
    public static final String MY_GAME_TABLE_SQL = "CREATE TABLE IF NOT EXISTS my_game ( id INTEGER PRIMARY KEY AUTOINCREMENT,game_id INTEGER, game_icon TEXT, game_desc TEXT, game_screenshots TEXT, game_tag VARCHAR(255), game_url TEXT, hits INTEGER, replies INTEGER, game_name VARCHAR(255));";
    public static final String RECOMMEND_TABLE_SQL = "CREATE TABLE IF NOT EXISTS t_recommend ( id INTEGER PRIMARY KEY AUTOINCREMENT,tag_name VARCHAR(255),current_page INTEGER,json_str TEXT);";
}
