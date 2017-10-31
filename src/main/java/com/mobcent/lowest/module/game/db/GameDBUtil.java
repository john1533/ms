package com.mobcent.lowest.module.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.db.constant.GameDBConstant;
import com.mobcent.lowest.module.game.db.constant.GameDbTableConstant;
import com.mobcent.lowest.module.game.model.WebGameModel;
import java.util.ArrayList;
import java.util.List;

public class GameDBUtil extends GameBaseDBUtil implements GameDBConstant, GameDbTableConstant {
    private static GameDBUtil gameDBUtil;

    private GameDBUtil(Context context, String tagName) {
        this.autogenDBOpenHelper = new GameDBOpenHelper(context, GameDBConstant.MC_GAME_DB_NAME, 1, tagName);
    }

    public static GameDBUtil getInstance(Context context, String tagName) {
        if (gameDBUtil == null) {
            gameDBUtil = new GameDBUtil(context, tagName);
            MCLogUtil.e("GameDBUtil", "tagName = " + tagName);
        }
        return gameDBUtil;
    }

    public synchronized boolean saveWebGameList(int page, String tagName, String jsonStr) {
        boolean z = false;
        String tableName = getTableName(tagName);
        MCLogUtil.e("saveWebGameList", "tableName = " + tableName);
        try {
            ContentValues values = new ContentValues();
            values.put(GameDbTableConstant.PAGE, Integer.valueOf(page));
            values.put("tag_name", tagName);
            values.put(GameDbTableConstant.JSON_STRING, jsonStr);
            openWriteableDB();
            this.writableDatabase.insertOrThrow(tableName, null, values);
            closeWriteableDB();
            z = true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeWriteableDB();
            z = false;
        } catch (Throwable th) {
            closeWriteableDB();
        }
        return z;
    }

    private String getTableName(String tagName) {
        if (GameApiConstant.RECOMMEND_TAG.equals(tagName)) {
            return GameDbTableConstant.TABLE_RECOMMEND_NAME;
        }
        if (GameApiConstant.LATEST_TAG.equals(tagName)) {
            return GameDbTableConstant.TABLE_LATEST_NAME;
        }
        if (GameApiConstant.MY_TAG.equals(tagName)) {
            return GameDbTableConstant.TABLE_MY_GAME;
        }
        return null;
    }

    public synchronized String getWebGameListByTag(int page, String tagName) {
        String str = null;
        synchronized (this) {
            Cursor c = null;
            try {
                openReadableDB();
                String column = GameDbTableConstant.JSON_STRING;
                String sql = "select json_str from " + getTableName(tagName) + " where " + GameDbTableConstant.PAGE + "= " + page;
                MCLogUtil.e("getWebGameListByTag", "sql = " + sql);
                c = this.readableDatabase.rawQuery(sql, null);
                if (c.moveToFirst()) {
                    str = c.getString(c.getColumnIndex(column));
                    if (c != null) {
                        c.close();
                    }
                    closeReadableDB();
                } else {
                    if (c != null) {
                        c.close();
                    }
                    closeReadableDB();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (c != null) {
                    c.close();
                }
                closeReadableDB();
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
                closeReadableDB();
            }
        }
        return str;
    }

    public synchronized void updataWebGameList(int page, String tagName, String jsonStr) {
        openWriteableDB();
        this.writableDatabase.beginTransaction();
        boolean isRollBack = false;
        String tableName = getTableName(tagName);
        try {
            if (isRowExisted(this.writableDatabase, tableName, "tag_name", tagName)) {
                if (this.writableDatabase.delete(tableName, "tag_name = ? ", new String[]{tagName}) == 0) {
                    isRollBack = true;
                }
            }
            ContentValues values = new ContentValues();
            values.put(GameDbTableConstant.PAGE, Integer.valueOf(page));
            values.put("tag_name", tagName);
            values.put(GameDbTableConstant.JSON_STRING, jsonStr);
            if (this.writableDatabase.insertOrThrow(tableName, null, values) == -1) {
                isRollBack = true;
            }
            if (!isRollBack) {
                this.writableDatabase.setTransactionSuccessful();
            }
            this.writableDatabase.endTransaction();
            closeWriteableDB();
        } catch (Exception e) {
            e.printStackTrace();
            if (!true) {
                this.writableDatabase.setTransactionSuccessful();
            }
            this.writableDatabase.endTransaction();
            closeWriteableDB();
        } catch (Throwable th) {
            if (null == null) {
                this.writableDatabase.setTransactionSuccessful();
            }
            this.writableDatabase.endTransaction();
            closeWriteableDB();
        }
    }

    public synchronized int getMaxPage(String tagName) {
        int page = 0;
        Cursor c = null;
        int page2 = -1;
        try {
            openReadableDB();
            c = this.readableDatabase.rawQuery("select max(current_page) from " + getTableName(tagName), null);
            if (c.moveToFirst()) {
                page2 = c.getInt(0);
            }
            if (c != null) {
                c.close();
            }
            closeReadableDB();
            page = page2;
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
            closeReadableDB();
            page = -1;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            closeReadableDB();
        }
        return page;
    }

    public synchronized boolean saveMyWebGame(WebGameModel webGameModel, String tagName) {
        boolean z = false;
        String tableName = getTableName(tagName);
        MCLogUtil.e("saveMyWebGame", "tableName = " + tableName);
        try {
            openWriteableDB();
            if (isRowExisted(this.writableDatabase, tableName, "game_id", webGameModel.getGameId())) {
                closeWriteableDB();
                z = true;
            } else {
                ContentValues values = new ContentValues();
                values.put("game_name", webGameModel.getGameName());
                values.put("game_id", Long.valueOf(webGameModel.getGameId()));
                values.put("game_icon", webGameModel.getGameIcon());
                values.put("game_desc", webGameModel.getGameDesc());
                String[] strArr = webGameModel.getGameScreenshots();
                String ss = "";
                for (int j = 0; j < strArr.length; j++) {
                    if (!MCStringUtil.isEmpty(strArr[j])) {
                        ss = new StringBuilder(String.valueOf(ss)).append(strArr[j]).append("+").toString();
                    }
                }
                values.put("game_screenshots", ss.substring(0, ss.lastIndexOf("+")));
                values.put("game_tag", webGameModel.getGameTag());
                values.put("game_url", webGameModel.getGameUrl());
                values.put("hits", Integer.valueOf(webGameModel.getHits()));
                values.put("replies", Integer.valueOf(webGameModel.getReplies()));
                openWriteableDB();
                this.writableDatabase.insertOrThrow(tableName, null, values);
                closeWriteableDB();
                z = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            closeWriteableDB();
            z = false;
        } catch (Throwable th) {
            closeWriteableDB();
        }
        return z;
    }

    public synchronized List<WebGameModel> getMyWebGame(String tagName, int page, int pageSize) {
        List<WebGameModel> webGameList;
        Throwable th;
        Exception e;
        Cursor result = null;
        List<WebGameModel> webGameList2 = null;
        try {
            webGameList = new ArrayList();
            try {
                openReadableDB();
                int start = (page - 1) * pageSize;
                int end = start + pageSize;
                String tableName = getTableName(tagName);
                Cursor cursor = this.readableDatabase.rawQuery("select count(*) from " + tableName, null);
                int totalCount = 0;
                if (cursor.moveToFirst()) {
                    totalCount = cursor.getInt(0);
                }
                cursor.close();
                if (totalCount == 0) {
                    if (result != null) {
                        try {
                            result.close();
                        } catch (Throwable th2) {
                            th = th2;
                            webGameList2 = webGameList;
                            throw th;
                        }
                    }
                    closeReadableDB();
                    webGameList2 = webGameList;
                } else {
                    result = this.readableDatabase.rawQuery("select * from " + tableName + "  Limit " + pageSize + " offset " + start + ";", null);
                    while (result.moveToNext()) {
                        WebGameModel webGameModel = new WebGameModel();
                        int gameId = result.getInt(result.getColumnIndex("game_id"));
                        String icon = result.getString(result.getColumnIndex("game_icon"));
                        String desc = result.getString(result.getColumnIndex("game_desc"));
                        String[] s = result.getString(result.getColumnIndex("game_screenshots")).split("\\+");
                        String tag = result.getString(result.getColumnIndex("game_tag"));
                        String url = result.getString(result.getColumnIndex("game_url"));
                        int hits = result.getInt(result.getColumnIndex("hits"));
                        int replies = result.getInt(result.getColumnIndex("replies"));
                        String name = result.getString(result.getColumnIndex("game_name"));
                        webGameModel.setGameDesc(desc);
                        webGameModel.setGameIcon(icon);
                        webGameModel.setGameId((long) gameId);
                        webGameModel.setGameName(name);
                        webGameModel.setGameScreenshots(s);
                        webGameModel.setGameTag(tag);
                        webGameModel.setGameUrl(url);
                        webGameModel.setHits(hits);
                        webGameModel.setReplies(replies);
                        webGameList.add(webGameModel);
                    }
                    if (webGameList.size() > 0) {
                        if (totalCount <= end) {
                            ((WebGameModel) webGameList.get(0)).setHasNext(false);
                        } else {
                            ((WebGameModel) webGameList.get(0)).setHasNext(true);
                        }
                    }
                    if (result != null) {
                        result.close();
                    }
                    closeReadableDB();
                    webGameList2 = webGameList;
                    webGameList = webGameList2;
                    return webGameList;
                }
            } catch (Exception e2) {
                e = e2;
                webGameList2 = webGameList;
            } catch (Throwable th3) {
                th = th3;
                webGameList2 = webGameList;
            }
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                if (result != null) {
                    try {
                        result.close();
                    } catch (Throwable th4) {
                        th = th4;
                        throw th;
                    }
                }
                closeReadableDB();
                webGameList = webGameList2;
                return webGameList;
            } catch (Throwable th5) {
                th = th5;
                if (result != null) {
                    result.close();
                }
                closeReadableDB();
            }
        }
        return null;
    }

    public synchronized boolean deleteMyWebGame(String tagName, long gameId) {
        boolean isDelete = false;
        openWriteableDB();
//        boolean isDelete2 = false;
        String tableName = getTableName(tagName);
        try {
            if (isRowExisted(this.writableDatabase, tableName, "game_id", gameId)) {
                if (this.writableDatabase.delete(tableName, "game_id = ? ", new String[]{new StringBuilder(String.valueOf(gameId)).toString()}) >= 1) {
                    isDelete = true;
                }
            }
            closeWriteableDB();
//            isDelete = isDelete2;
        } catch (Exception e) {
            e.printStackTrace();
            closeWriteableDB();
            isDelete = false;
        }
        return isDelete;
    }
}
