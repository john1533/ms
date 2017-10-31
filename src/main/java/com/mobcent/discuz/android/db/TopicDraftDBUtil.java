package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.mobcent.discuz.android.db.constant.TopicDraftDBConstant;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TopicDraftDBUtil extends BaseDBUtil implements TopicDraftDBConstant {
    private static TopicDraftDBUtil topicDraftDBUtil;
    private MyObserver observer;

    private class MyObserver extends Observable {
        private MyObserver() {
        }

        protected synchronized void setChanged() {
            super.setChanged();
        }
    }

    protected TopicDraftDBUtil(Context ctx) {
        super(ctx);
        this.observer = null;
        this.observer = new MyObserver();
    }

    public static synchronized TopicDraftDBUtil getInstance(Context context) {
        synchronized (TopicDraftDBUtil.class) {
            if (topicDraftDBUtil == null) {
                topicDraftDBUtil = new TopicDraftDBUtil(context);
            }
        }
        return topicDraftDBUtil;
    }

    public void saveDraftModel(TopicDraftModel draftModel) {
        try {
            openWriteableDB();
            synchronized (this.writableDatabase) {
                ContentValues values = parseValues(draftModel);
                if (draftModel.getId() != 0) {
                    List draftModels = queryDraftModel(draftModel);
                    if (!MCListUtils.isEmpty(draftModels)) {
                        draftModel.setSaveTime(((TopicDraftModel) draftModels.get(0)).getSaveTime());
                    }
                    updateDraftModel(draftModel);
                } else {
                    this.writableDatabase.insert(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, values);
                }
            }
        } catch (Exception e) {
        } finally {
            closeWriteableDB();
            notifyObservers();
        }
    }

    public void deleteDraftModel(TopicDraftModel draftModel) {
        try {
            openWriteableDB();
            synchronized (this.writableDatabase) {
                this.writableDatabase.beginTransaction();
                if (draftModel == null) {
                    this.writableDatabase.delete(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, null);
                } else if (draftModel.getId() != 0) {
                    this.writableDatabase.delete(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, "id= ? ", new String[]{new StringBuilder(String.valueOf(draftModel.getId())).toString()});
                } else if (draftModel.getType() != 0) {
                    this.writableDatabase.delete(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, "type= ? ", new String[]{new StringBuilder(String.valueOf(draftModel.getType())).toString()});
                } else {
                    this.writableDatabase.delete(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, "draft_id= ?", new String[]{getDraftId(draftModel)});
                }
                this.writableDatabase.setTransactionSuccessful();
            }
        } catch (Exception e) {
        } finally {
            this.writableDatabase.endTransaction();
            closeWriteableDB();
            notifyObservers();
        }
    }

    public void updateDraftModel(TopicDraftModel draftModel) {
        try {
            openWriteableDB();
            ContentValues values = parseValues(draftModel);
            if (draftModel.getSaveTime() != 0) {
                values.put(TopicDraftDBConstant.COLUMN_TIME, Long.valueOf(draftModel.getSaveTime()));
            }
            synchronized (this.writableDatabase) {
                this.writableDatabase.update(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, values, "id= ? ", new String[]{new StringBuilder(String.valueOf(draftModel.getId())).toString()});
            }
        } catch (Exception e) {
        } finally {
            closeWriteableDB();
        }
    }

    public List<TopicDraftModel> queryDraftModel(TopicDraftModel draftModel) {
        List<TopicDraftModel> draftList = new ArrayList();
        try {
            openReadableDB();
            synchronized (this.writableDatabase) {
                Cursor cur;
                if (draftModel == null) {
                    cur = this.writableDatabase.query(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, null, null, null, null, "save_time DESC");
                } else if (draftModel.getId() != 0) {
                    cur = this.writableDatabase.query(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, "id= ?", new String[]{new StringBuilder(String.valueOf(draftModel.getId())).toString()}, null, null, null);
                } else if (draftModel.getType() != 0 && draftModel.getCommonId() != 0) {
                    cur = this.writableDatabase.query(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, "draft_id= ?", new String[]{getDraftId(draftModel)}, null, null, null);
                } else if (draftModel.getType() != 0) {
                    cur = this.writableDatabase.query(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, "type= ?", new String[]{new StringBuilder(String.valueOf(draftModel.getType())).toString()}, null, null, null);
                } else {
                    cur = this.writableDatabase.query(TopicDraftDBConstant.TABLE_TOPIC_DRAFT, null, "draft_id= ?", new String[]{getDraftId(draftModel)}, null, null, null);
                }
                if (cur != null) {
                    while (cur.moveToNext()) {
                        int id = cur.getInt(cur.getColumnIndex("id"));
                        TopicDraftModel topicDraftModel = (TopicDraftModel) new Gson().fromJson(cur.getString(cur.getColumnIndex("json")), TopicDraftModel.class);
                        topicDraftModel.setId(id);
                        draftList.add(topicDraftModel);
                    }
                    cur.close();
                }
            }
        } catch (Exception e) {
        } finally {
            closeWriteableDB();
        }
        return draftList;
    }

    public boolean isHasDraftToAlert(long lastAlertTime, int type) {
        if (getNewestTime(type) > lastAlertTime) {
            return true;
        }
        return false;
    }

    public long getNewestTime(int type) {
        TopicDraftModel topDraftModel = queryByTimeDesc(type);
        if (topDraftModel != null) {
            return topDraftModel.getSaveTime();
        }
        return 0;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.mobcent.discuz.android.model.TopicDraftModel queryByTimeDesc(int r15) {
        this.openWriteableDB();
        SQLiteDatabase db = this.writableDatabase;
        TopicDraftModel ret = null;
        synchronized (db){
            String table = "topic_draft";
            String r3 = null;
            if(r15 != 0){
                r3 = "type = ?";
            }
            String[] r4 = new String[]{String.valueOf(r15)};
            Cursor cursor = db.query(table,null,r3,r4,null,null,"save_time DESC");
            if(cursor!=null&&cursor.moveToNext()){
                int r11 = cursor.getInt(cursor.getColumnIndex("id"));
                String r12 = cursor.getString(cursor.getColumnIndex("json"));
                Gson r10 = new Gson();
                ret = (TopicDraftModel)r10.fromJson(r12,TopicDraftModel.class);
                ret.setId(r11);
            }
            if(cursor!=null){
                cursor.close();
            }
            this.closeWriteableDB();

        }
        return ret;
        /*
        r14 = this;
        r8 = 0;
        r14.openReadableDB();	 Catch:{ Exception -> 0x006d, all -> 0x0075 }
        r13 = r14.writableDatabase;	 Catch:{ Exception -> 0x006d, all -> 0x0075 }
        monitor-enter(r13);	 Catch:{ Exception -> 0x006d, all -> 0x0075 }
        r0 = r14.writableDatabase;	 Catch:{ all -> 0x006a }
        r1 = "topic_draft";
        r2 = 0;
        if (r15 != 0) goto L_0x005e;
    L_0x000e:
        r3 = 0;
    L_0x000f:
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ all -> 0x006a }
        r5 = 0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r7 = java.lang.String.valueOf(r15);	 Catch:{ all -> 0x006a }
        r6.<init>(r7);	 Catch:{ all -> 0x006a }
        r6 = r6.toString();	 Catch:{ all -> 0x006a }
        r4[r5] = r6;	 Catch:{ all -> 0x006a }
        r5 = 0;
        r6 = 0;
        r7 = "save_time DESC";
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x006a }
        if (r8 == 0) goto L_0x0061;
    L_0x002c:
        r0 = r8.moveToNext();	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x0061;
    L_0x0032:
        r0 = "id";
        r0 = r8.getColumnIndex(r0);	 Catch:{ all -> 0x006a }
        r11 = r8.getInt(r0);	 Catch:{ all -> 0x006a }
        r0 = "json";
        r0 = r8.getColumnIndex(r0);	 Catch:{ all -> 0x006a }
        r12 = r8.getString(r0);	 Catch:{ all -> 0x006a }
        r10 = new com.google.gson.Gson;	 Catch:{ all -> 0x006a }
        r10.<init>();	 Catch:{ all -> 0x006a }
        r0 = com.mobcent.discuz.android.model.TopicDraftModel.class;
        r9 = r10.fromJson(r12, r0);	 Catch:{ all -> 0x006a }
        r9 = (com.mobcent.discuz.android.model.TopicDraftModel) r9;	 Catch:{ all -> 0x006a }
        r9.setId(r11);	 Catch:{ all -> 0x006a }
        monitor-exit(r13);	 Catch:{ all -> 0x006a }
        r14.closeWriteableDB();
        r8.close();
    L_0x005d:
        return r9;
    L_0x005e:
        r3 = "type = ?";
        goto L_0x000f;
    L_0x0061:
        monitor-exit(r13);	 Catch:{ all -> 0x006a }
        r14.closeWriteableDB();
        r8.close();
    L_0x0068:
        r9 = 0;
        goto L_0x005d;
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x006a }
        throw r0;	 Catch:{ Exception -> 0x006d, all -> 0x0075 }
    L_0x006d:
        r0 = move-exception;
        r14.closeWriteableDB();
        r8.close();
        goto L_0x0068;
    L_0x0075:
        r0 = move-exception;
        r14.closeWriteableDB();
        r8.close();
        throw r0;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.TopicDraftDBUtil.queryByTimeDesc(int):com.mobcent.discuz.android.model.TopicDraftModel");
    }

    private ContentValues parseValues(TopicDraftModel draftModel) {
        ContentValues values = new ContentValues();
        if (draftModel.getId() != 0) {
            values.put("id", Integer.valueOf(draftModel.getId()));
        } else {
            draftModel.setSaveTime(System.currentTimeMillis());
        }
        values.put(TopicDraftDBConstant.COLUMN_TIME, Long.valueOf(draftModel.getSaveTime()));
        values.put(TopicDraftDBConstant.COLUMN_DRAFT, getDraftId(draftModel));
        values.put("type", Integer.valueOf(draftModel.getType()));
        values.put("json", new Gson().toJson((Object) draftModel));
        return values;
    }

    public static String getDraftId(TopicDraftModel draftModel) {
        if (draftModel == null) {
            return "";
        }
        return draftModel.getCommonId() + "=" + draftModel.getType();
    }

    public void register(Observer o) {
        if (this.observer != null) {
            this.observer.addObserver(o);
        }
    }

    public void unRegister(Observer o) {
        if (this.observer != null) {
            this.observer.deleteObserver(o);
        }
    }

    public void notifyObservers() {
        if (this.observer != null) {
            this.observer.setChanged();
            this.observer.notifyObservers();
        }
    }
}
