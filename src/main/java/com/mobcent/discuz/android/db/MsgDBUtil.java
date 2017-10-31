package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.MsgDBConstant;
import com.mobcent.discuz.android.model.BaseModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.model.UserInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MsgDBUtil extends BaseDBUtil implements MsgDBConstant {
    private static final String TAG = "MsgDBUtil";
    private static MsgDBUtil msgDBUtil;

    public static class MsgDBModel extends BaseModel {
        private static final long serialVersionUID = -581357882721364665L;
        private int audioRead;
        private int cacheCount;
        private String content;
        private long fromUid;
        private String icon;
        private String name;
        private long plid;
        private int pmLimit;
        private long pmid;
        private int source;
        private long startTime;
        private int status;
        private long stopTime;
        private long time;
        private String type;
        private int unReadCount;

        public long getFromUid() {
            return this.fromUid;
        }

        public void setFromUid(long fromUid) {
            this.fromUid = fromUid;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getStopTime() {
            return this.stopTime;
        }

        public void setStopTime(long stopTime) {
            this.stopTime = stopTime;
        }

        public long getPlid() {
            return this.plid;
        }

        public void setPlid(long plid) {
            this.plid = plid;
        }

        public long getPmid() {
            return this.pmid;
        }

        public void setPmid(long pmid) {
            this.pmid = pmid;
        }

        public int getCacheCount() {
            return this.cacheCount;
        }

        public void setCacheCount(int cacheCount) {
            this.cacheCount = cacheCount;
        }

        public int getPmLimit() {
            return this.pmLimit;
        }

        public void setPmLimit(int pmLimit) {
            this.pmLimit = pmLimit;
        }

        public long getTime() {
            return this.time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSource() {
            return this.source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUnReadCount() {
            return this.unReadCount;
        }

        public void setUnReadCount(int unReadCount) {
            this.unReadCount = unReadCount;
        }

        public int getAudioRead() {
            return this.audioRead;
        }

        public void setAudioRead(int audioRead) {
            this.audioRead = audioRead;
        }
    }

    public java.util.List<com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel> getChatList(long r12, long r14, long r16, int r18, int r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x017b in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/

        this.openWriteableDB();
        ArrayList<com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel> r1 = new ArrayList<com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel>();
        SQLiteDatabase db = this.writableDatabase;
        db.beginTransaction();
        Cursor cursor = null;
//        L_0x0108:
//        r6 = 1;
//        r0 = r19;
//        if (r0 != r6) goto L_0x0139;
//        L_0x010d:
//        r6 = r11.writableDatabase;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r7 = "select * from (select * from table_msg_list_@ where ouid=? and time<? order by time desc limit ?) order by time";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r8 = "@";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r9 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r7 = r7.replaceAll(r8, r9);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }

//        r8 = 3;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r8 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r9 = 0;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r10 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r9 = 1;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r10 = java.lang.String.valueOf(r16);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r9 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r10 = java.lang.String.valueOf(r18);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r2 = r6.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        goto L_0x0038;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        L_0x0139:
//        r6 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        r0 = r19;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
//        if (r0 != r6) goto L_0x0038;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }


        if(r16!=0){//L_0x0108
            if(r19 == 1){
                String r7 = "select * from (select * from table_msg_list_@ where ouid=? and time<? order by time desc limit ?) order by time";
                r7 = r7.replaceAll("@",String.valueOf(r12));
                cursor = db.rawQuery(r7,new String[]{String.valueOf(r14),String.valueOf(r16),String.valueOf(r18)});
                //L_0x0038
            }else{//L_0x0139
                String r7 = "select * from table_msg_list_@ where ouid=? and time>? order by time";
                r7 = r7.replaceAll("@",String.valueOf(r12));
                cursor = db.rawQuery(r7,new String[]{String.valueOf(r14),String.valueOf(r16)});
            }
        }else{
            String r7 = "select * from (select * from table_msg_list_@ where ouid=? order by time desc limit ?) order by time";
            r7 = r7.replaceAll("@",String.valueOf(r12));
            cursor = db.rawQuery(r7,new String[]{String.valueOf(r14),String.valueOf(r18)});//r2

        }

        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                MsgDBModel msgDBModel = new MsgDBModel();
                msgDBModel.setPmid(cursor.getLong(cursor.getColumnIndex("mid")));
                msgDBModel.setPlid(cursor.getLong(cursor.getColumnIndex("sid")));
                msgDBModel.setFromUid(cursor.getLong(cursor.getColumnIndex("ouid")));
                msgDBModel.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                msgDBModel.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msgDBModel.setType(cursor.getString(cursor.getColumnIndex("contenttype")));
                msgDBModel.setSource(cursor.getInt(cursor.getColumnIndex("source")));
                msgDBModel.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                msgDBModel.setIcon(cursor.getString(cursor.getColumnIndex("ouicon")));
                msgDBModel.setName(cursor.getString(cursor.getColumnIndex("ouname")));
                String type = msgDBModel.getType();
                if("audio".equals(type)){
                    if(cursor.getColumnIndex("audioread") != -1){
                        msgDBModel.setAudioRead(cursor.getInt(cursor.getColumnIndex("audioread")));
                    }
                }
                r1.add(msgDBModel);
            }while (cursor.moveToNext());

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        if(cursor != null)
            cursor.close();
        return r1;

//        r6 = r11.writableDatabase;
//        r6.endTransaction();
//        if (r2 == 0) goto L_0x0104;
//        L_0x0101:
//        r2.close();
//        L_0x0104:
//        r11.closeWriteableDB();
//        L_0x0107:
//        return r1;


        /*
        r11 = this;
        r2 = 0;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r4 = 0;
        r11.openWriteableDB();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r11.writableDatabase;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6.beginTransaction();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = 0;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r6 != 0) goto L_0x0108;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x0015:
        r6 = r11.writableDatabase;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = "select * from (select * from table_msg_list_@ where ouid=? order by time desc limit ?) order by time";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = "@";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = r7.replaceAll(r8, r9);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }

        r8 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 0;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 1;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r18);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r2 = r6.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x0038:
        if (r2 == 0) goto L_0x00f5;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x003a:
        r6 = r2.getCount();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r6 <= 0) goto L_0x00f5;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x0040:
        r2.moveToFirst();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x0043:
        r5 = new com.mobcent.discuz.android.db.MsgDBUtil$MsgDBModel;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.<init>();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "mid";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getLong(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setPmid(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "sid";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getLong(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setPlid(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "ouid";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getLong(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setFromUid(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "time";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getLong(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setTime(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "content";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getString(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setContent(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "contenttype";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getString(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setType(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "source";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getInt(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setSource(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "status";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getInt(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setStatus(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "ouicon";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getString(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setIcon(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "ouname";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getString(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setName(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = "audio";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = r5.getType();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r6.equals(r7);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r6 == 0) goto L_0x00ec;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x00d6:
        r6 = "audioread";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = -1;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r6 == r7) goto L_0x00ec;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x00df:
        r6 = "audioread";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getColumnIndex(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.getInt(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r5.setAudioRead(r6);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x00ec:
        r1.add(r5);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r6 = r2.moveToNext();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r6 != 0) goto L_0x0043;
    L_0x00f5:
        if (r4 == 0) goto L_0x019b;
    L_0x00f7:
        r1.clear();
    L_0x00fa:
        r6 = r11.writableDatabase;
        r6.endTransaction();
        if (r2 == 0) goto L_0x0104;
    L_0x0101:
        r2.close();
    L_0x0104:
        r11.closeWriteableDB();
    L_0x0107:
        return r1;
    L_0x0108:
        r6 = 1;
        r0 = r19;
        if (r0 != r6) goto L_0x0139;
    L_0x010d:
        r6 = r11.writableDatabase;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = "select * from (select * from table_msg_list_@ where ouid=? and time<? order by time desc limit ?) order by time";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = "@";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = r7.replaceAll(r8, r9);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = 3;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 0;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 1;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r16);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r18);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r2 = r6.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        goto L_0x0038;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x0139:
        r6 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r0 = r19;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        if (r0 != r6) goto L_0x0038;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
    L_0x013e:
        r6 = r11.writableDatabase;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = "select * from table_msg_list_@ where ouid=? and time>? order by time";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = "@";	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r7 = r7.replaceAll(r8, r9);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = 2;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 0;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r9 = 1;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r10 = java.lang.String.valueOf(r16);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r8[r9] = r10;	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r2 = r6.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        goto L_0x0038;
    L_0x0163:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ Exception -> 0x0163, all -> 0x0181 }
        r4 = 1;
        if (r4 == 0) goto L_0x017b;
    L_0x016a:
        r1.clear();
    L_0x016d:
        r6 = r11.writableDatabase;
        r6.endTransaction();
        if (r2 == 0) goto L_0x0177;
    L_0x0174:
        r2.close();
    L_0x0177:
        r11.closeWriteableDB();
        goto L_0x0107;
    L_0x017b:
        r6 = r11.writableDatabase;
        r6.setTransactionSuccessful();
        goto L_0x016d;
    L_0x0181:
        r6 = move-exception;
        if (r4 == 0) goto L_0x0195;
    L_0x0184:
        r1.clear();
    L_0x0187:
        r7 = r11.writableDatabase;
        r7.endTransaction();
        if (r2 == 0) goto L_0x0191;
    L_0x018e:
        r2.close();
    L_0x0191:
        r11.closeWriteableDB();
        throw r6;
    L_0x0195:
        r7 = r11.writableDatabase;
        r7.setTransactionSuccessful();
        goto L_0x0187;
    L_0x019b:
        r6 = r11.writableDatabase;
        r6.setTransactionSuccessful();
        goto L_0x00fa;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.getChatList(long, long, long, int, int):java.util.List<com.mobcent.discuz.android.db.MsgDBUtil$MsgDBModel>");
    }

    public java.lang.String getCommentAtJson(long r9, java.lang.String r11) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0043 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        String ret = "";
        this.openWriteableDB();
        String sql = " select * from commentAt_list where userId=? and type=?;";
        String[] pValues = new String[]{String.valueOf(r9),r11};
        Cursor cursor = this.writableDatabase.rawQuery(sql,pValues);
        if(cursor != null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                ret = cursor.getString(cursor.getColumnIndexOrThrow("commentAtJson"));
            }
            cursor.close();
        }
        return ret;
        /*
        r8 = this;
        r0 = "";
        r1 = 0;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = r8.writableDatabase;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r4 = " select * from commentAt_list where userId=? and type=?;";	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5 = 2;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r6 = 0;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5[r6] = r7;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r6 = 1;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5[r6] = r11;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r1 = r3.rawQuery(r4, r5);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        if (r1 == 0) goto L_0x0030;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x001d:
        r3 = r1.getCount();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        if (r3 <= 0) goto L_0x0030;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x0023:
        r1.moveToFirst();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = "commentAtJson";	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = r1.getColumnIndexOrThrow(r3);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r0 = r1.getString(r3);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x0030:
        if (r1 == 0) goto L_0x0035;
    L_0x0032:
        r1.close();
    L_0x0035:
        r8.closeWriteableDB();
    L_0x0038:
        return r0;
    L_0x0039:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r0 = 0;
        if (r1 == 0) goto L_0x0043;
    L_0x0040:
        r1.close();
    L_0x0043:
        r8.closeWriteableDB();
        goto L_0x0038;
    L_0x0047:
        r3 = move-exception;
        if (r1 == 0) goto L_0x004d;
    L_0x004a:
        r1.close();
    L_0x004d:
        r8.closeWriteableDB();
        throw r3;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.getCommentAtJson(long, java.lang.String):java.lang.String");
    }

    public String getSessionJson(long r9) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0040 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        this.openWriteableDB();
        String ret = "";
        String strQuery = " select sessionJson from session_list where userId=?";
        String[] r5 = new String[]{String.valueOf(r9)};
        Cursor cursor = this.writableDatabase.rawQuery(strQuery,r5);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                ret = cursor.getString(cursor.getColumnIndexOrThrow("sessionJson"));
            }
            cursor.close();
        }
        closeWriteableDB();
        return ret;

        /*
        r8 = this;
        r2 = "";
        r0 = 0;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r3 = r8.writableDatabase;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r4 = " select sessionJson from session_list where userId=?";	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r5 = 1;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r6 = 0;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r5[r6] = r7;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r0 = r3.rawQuery(r4, r5);	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        if (r0 == 0) goto L_0x002d;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
    L_0x001a:
        r3 = r0.getCount();	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        if (r3 <= 0) goto L_0x002d;	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
    L_0x0020:
        r0.moveToFirst();	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r3 = "sessionJson";	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r3 = r0.getColumnIndexOrThrow(r3);	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r2 = r0.getString(r3);	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
    L_0x002d:
        if (r0 == 0) goto L_0x0032;
    L_0x002f:
        r0.close();
    L_0x0032:
        r8.closeWriteableDB();
    L_0x0035:
        return r2;
    L_0x0036:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x0036, all -> 0x0044 }
        r2 = 0;
        if (r0 == 0) goto L_0x0040;
    L_0x003d:
        r0.close();
    L_0x0040:
        r8.closeWriteableDB();
        goto L_0x0035;
    L_0x0044:
        r3 = move-exception;
        if (r0 == 0) goto L_0x004a;
    L_0x0047:
        r0.close();
    L_0x004a:
        r8.closeWriteableDB();
        throw r3;
        */
    }

    public boolean saveSessionJson(long r16, java.lang.String r18) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0093 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        boolean ret = false;
        this.openWriteableDB();
        SQLiteDatabase db = this.writableDatabase;
        db.beginTransaction();
        ContentValues r14 = new ContentValues();
        r14.put("userId", String.valueOf(r16));
        r14.put("sessionJson", r18);
        if(this.isRowExisted(db,"session_list","userId",r16)){
            StringBuilder stringBuilder = new StringBuilder("userId=");
            stringBuilder.append(r16);
            int affected = db.update("session_list",r14,stringBuilder.toString(),null);
            if(affected!=0){
                ret = true;
            }
        }else{
            if(db.insert("session_list",null,r14)>-1){
                ret = true;
            }
        }

        db.endTransaction();
//        db.close();
        this.closeWriteableDB();
        return ret;

        /*
        r15 = this;
        r8 = 0;
        r12 = 0;
        r15.openWriteableDB();	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = r15.writableDatabase;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2.beginTransaction();	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r14 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r14.<init>();	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = "userId";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r3 = java.lang.Long.valueOf(r16);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r14.put(r2, r3);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = "sessionJson";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r0 = r18;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r14.put(r2, r0);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r3 = r15.writableDatabase;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4 = "session_list";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r5 = "userId";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = r15;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r6 = r16;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = r2.isRowExisted(r3, r4, r5, r6);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        if (r2 == 0) goto L_0x0061;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
    L_0x002e:
        r2 = r15.writableDatabase;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r3 = "session_list";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r5 = "userId=";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r0 = r16;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r5 = 0;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r13 = r2.update(r3, r14, r4, r5);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        if (r13 > 0) goto L_0x00b8;
    L_0x004a:
        r12 = 1;
        if (r12 != 0) goto L_0x0052;
    L_0x004d:
        r2 = r15.writableDatabase;
        r2.setTransactionSuccessful();
    L_0x0052:
        r2 = r15.writableDatabase;
        r2.endTransaction();
        if (r8 == 0) goto L_0x005c;
    L_0x0059:
        r8.close();
    L_0x005c:
        r15.closeWriteableDB();
        r2 = 0;
    L_0x0060:
        return r2;
    L_0x0061:
        r2 = r15.writableDatabase;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r3 = "session_list";	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r4 = 0;	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r10 = r2.insert(r3, r4, r14);	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r2 = -1;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x00b8;
    L_0x0070:
        r12 = 1;
        if (r12 != 0) goto L_0x0078;
    L_0x0073:
        r2 = r15.writableDatabase;
        r2.setTransactionSuccessful();
    L_0x0078:
        r2 = r15.writableDatabase;
        r2.endTransaction();
        if (r8 == 0) goto L_0x0082;
    L_0x007f:
        r8.close();
    L_0x0082:
        r15.closeWriteableDB();
        r2 = 0;
        goto L_0x0060;
    L_0x0087:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x0087, all -> 0x00a2 }
        r12 = 1;
        if (r12 != 0) goto L_0x0093;
    L_0x008e:
        r2 = r15.writableDatabase;
        r2.setTransactionSuccessful();
    L_0x0093:
        r2 = r15.writableDatabase;
        r2.endTransaction();
        if (r8 == 0) goto L_0x009d;
    L_0x009a:
        r8.close();
    L_0x009d:
        r15.closeWriteableDB();
    L_0x00a0:
        r2 = 1;
        goto L_0x0060;
    L_0x00a2:
        r2 = move-exception;
        if (r12 != 0) goto L_0x00aa;
    L_0x00a5:
        r3 = r15.writableDatabase;
        r3.setTransactionSuccessful();
    L_0x00aa:
        r3 = r15.writableDatabase;
        r3.endTransaction();
        if (r8 == 0) goto L_0x00b4;
    L_0x00b1:
        r8.close();
    L_0x00b4:
        r15.closeWriteableDB();
        throw r2;
    L_0x00b8:
        if (r12 != 0) goto L_0x00bf;
    L_0x00ba:
        r2 = r15.writableDatabase;
        r2.setTransactionSuccessful();
    L_0x00bf:
        r2 = r15.writableDatabase;
        r2.endTransaction();
        if (r8 == 0) goto L_0x00c9;
    L_0x00c6:
        r8.close();
    L_0x00c9:
        r15.closeWriteableDB();
        goto L_0x00a0;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.saveSessionJson(long, java.lang.String):boolean");
    }

    protected MsgDBUtil(Context context) {
        super(context);
    }

    public static synchronized MsgDBUtil getInstance(Context context) {
        MsgDBUtil msgDBUtil1;
        synchronized (MsgDBUtil.class) {
            if (msgDBUtil == null) {
                msgDBUtil = new MsgDBUtil(context.getApplicationContext());
            }
            msgDBUtil1 = msgDBUtil;
        }
        return msgDBUtil1;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveCommentAtJson(long r9, java.lang.String r11, java.lang.String r12) {
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put("userId",String.valueOf(r9));
        values.put("commentAtJson",r12);
        SQLiteDatabase db = this.writableDatabase;
        String[] r4 = new String[]{"userId","type"};
        String[] r5 = new String[]{String.valueOf(r9),r11};
        if(this.isRowExisted(db,"commentAt_list",r4,r5)){
            StringBuilder stringBuilder = new StringBuilder("userId=");
            stringBuilder.append(r9).append(" and ").append("type='").append(r11).append("'");
            db.update("commentAt_list",values,stringBuilder.toString(),null);
        }else{
            db.insert("commentAt_list",null,values);
        }
        this.closeWriteableDB();
        return;
        /*
        r8 = this;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x007f }
        r1 = new android.content.ContentValues;	 Catch:{ Exception -> 0x007f }
        r1.<init>();	 Catch:{ Exception -> 0x007f }
        r2 = "userId";
        r3 = java.lang.Long.valueOf(r9);	 Catch:{ Exception -> 0x007f }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x007f }
        r2 = "type";
        r1.put(r2, r11);	 Catch:{ Exception -> 0x007f }
        r2 = "commentAtJson";
        r1.put(r2, r12);	 Catch:{ Exception -> 0x007f }
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "commentAt_list";
        r4 = 2;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x007f }
        r5 = 0;
        r6 = "userId";
        r4[r5] = r6;	 Catch:{ Exception -> 0x007f }
        r5 = 1;
        r6 = "type";
        r4[r5] = r6;	 Catch:{ Exception -> 0x007f }
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x007f }
        r6 = 0;
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x007f }
        r5[r6] = r7;	 Catch:{ Exception -> 0x007f }
        r6 = 1;
        r5[r6] = r11;	 Catch:{ Exception -> 0x007f }
        r2 = r8.isRowExisted(r2, r3, r4, r5);	 Catch:{ Exception -> 0x007f }
        if (r2 == 0) goto L_0x0076;
    L_0x003f:
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "commentAt_list";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007f }
        r5 = "userId=";
        r4.<init>(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.append(r9);	 Catch:{ Exception -> 0x007f }
        r5 = " and ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r5 = "type";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r5 = "='";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.append(r11);	 Catch:{ Exception -> 0x007f }
        r5 = "'";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.toString();	 Catch:{ Exception -> 0x007f }
        r5 = 0;
        r2.update(r3, r1, r4, r5);	 Catch:{ Exception -> 0x007f }
    L_0x0072:
        r8.closeWriteableDB();
    L_0x0075:
        return;
    L_0x0076:
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "commentAt_list";
        r4 = 0;
        r2.insert(r3, r4, r1);	 Catch:{ Exception -> 0x007f }
        goto L_0x0072;
    L_0x007f:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0087 }
        r8.closeWriteableDB();
        goto L_0x0075;
    L_0x0087:
        r2 = move-exception;
        r8.closeWriteableDB();
        throw r2;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.saveCommentAtJson(long, java.lang.String, java.lang.String):void");
    }

    public boolean createTable(long userId) {
        boolean ret = false;
        try {
            openWriteableDB();
            this.writableDatabase.execSQL(MsgDBConstant.SQL_CREATE_MSG_TABLE.replaceAll("@", String.valueOf(userId)));
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            closeWriteableDB();
        }
        return ret;
    }

    public Map<Long, MsgDBModel> getLastTimeAndCacheCount(long userId, List<Long> fromUidList) {
        Exception e;
        Throwable th;
        Map<Long, MsgDBModel> map = null;
        String sqlStr = "";
        if (!(fromUidList == null || fromUidList.isEmpty())) {
            for (int i = 0; i < fromUidList.size(); i++) {
                if (i == 0) {
                    sqlStr = new StringBuilder(String.valueOf(sqlStr)).append(" having ").append(MsgDBConstant.COLUMN_OTHER_USER_ID).append("=").append(fromUidList.get(i)).toString();
                } else {
                    sqlStr = new StringBuilder(String.valueOf(sqlStr)).append(" or ").append(MsgDBConstant.COLUMN_OTHER_USER_ID).append("=").append(fromUidList.get(i)).toString();
                }
            }
        }
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(MsgDBConstant.SQL1.replaceAll("@", String.valueOf(userId)).replace("?", sqlStr), null);
            if (cursor != null && cursor.getCount() > 0) {
                Map<Long, MsgDBModel> timeAndCountMap = new HashMap();
                try {
                    cursor.moveToFirst();
                    do {
                        MsgDBModel msgDBModel = new MsgDBModel();
                        msgDBModel.setFromUid(cursor.getLong(cursor.getColumnIndexOrThrow(MsgDBConstant.COLUMN_OTHER_USER_ID)));
                        msgDBModel.setStartTime(cursor.getLong(cursor.getColumnIndexOrThrow("time")));
                        msgDBModel.setCacheCount(cursor.getInt(cursor.getColumnIndexOrThrow("count")));
                        timeAndCountMap.put(Long.valueOf(msgDBModel.getFromUid()), msgDBModel);
                    } while (cursor.moveToNext());
                    map = timeAndCountMap;
                } catch (Exception e2) {
                    e = e2;
                    map = timeAndCountMap;
                    try {
                        e.printStackTrace();
                        map = null;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
                        return map;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
//                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    map = timeAndCountMap;
                    if (cursor != null) {
                        cursor.close();
                    }
                    closeWriteableDB();
//                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            map = null;
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return map;
        }
        return map;
    }

    public Map<Long, MsgDBModel> getCacheMsgList(long userId, long fromUid, long time) {
        Exception e;
        Throwable th;
        Map<Long, MsgDBModel> map = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(MsgDBConstant.SQL2.replaceAll("@", String.valueOf(userId)), new String[]{String.valueOf(fromUid), String.valueOf(time)});
            if (cursor != null && cursor.getCount() > 0) {
                Map<Long, MsgDBModel> cacheMap = new HashMap();
                try {
                    cursor.moveToFirst();
                    do {
                        MsgDBModel msgDBModel = new MsgDBModel();
                        msgDBModel.setFromUid(cursor.getLong(cursor.getColumnIndex(MsgDBConstant.COLUMN_OTHER_USER_ID)));
                        msgDBModel.setPlid(cursor.getLong(cursor.getColumnIndex(MsgDBConstant.COLUMN_SESSION_ID)));
                        msgDBModel.setPmid(cursor.getLong(cursor.getColumnIndex("mid")));
                        cacheMap.put(Long.valueOf(msgDBModel.getPmid()), msgDBModel);
                    } while (cursor.moveToNext());
                    map = cacheMap;
                } catch (Exception e2) {
                    e = e2;
                    map = cacheMap;
                    try {
                        e.printStackTrace();
                        map = null;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
                        return map;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
//                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    map = cacheMap;
                    if (cursor != null) {
                        cursor.close();
                    }
                    closeWriteableDB();
//                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            map = null;
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return map;
        }
        return map;
    }

    public boolean saveMsg(UserInfoModel userInfoModel, List<MsgUserListModel> pmList) {
        long userId = userInfoModel.getUserId();
        Iterator it = pmList.iterator();
        while (it.hasNext()) {
            MsgUserListModel msgUserListModel = (MsgUserListModel) it.next();
            List<MsgContentModel> msgList = msgUserListModel.getMsgList();
            boolean isRollBack = false;
            openWriteableDB();
            this.writableDatabase.beginTransaction();
            int j = 0;
            while (j < msgList.size()) {
                try {
                    MsgContentModel msg = (MsgContentModel) msgList.get(j);
                    ContentValues values = new ContentValues();
                    values.put("mid", Long.valueOf(msg.getPmid()));
                    values.put(MsgDBConstant.COLUMN_SESSION_ID, Long.valueOf(msgUserListModel.getPlid()));
                    values.put("time", Long.valueOf(msg.getTime()));
                    values.put(MsgDBConstant.COLUMN_OTHER_USER_ID, Long.valueOf(msgUserListModel.getToUserId()));
                    values.put(MsgDBConstant.COLUMN_OTHER_USER_NAME, msgUserListModel.getToUserName());
                    values.put(MsgDBConstant.COLUMN_MSG_CONTENT_TYPE, msg.getType());
                    values.put("content", msg.getContent());
                    if (userId == msg.getSender()) {
                        values.put("source", Integer.valueOf(0));
                        values.put(MsgDBConstant.COLUMN_OTHER_USER_ICON, userInfoModel.getIcon());
                        values.put("status", Integer.valueOf(0));
                    } else {
                        values.put("source", Integer.valueOf(1));
                        values.put(MsgDBConstant.COLUMN_OTHER_USER_ICON, msgUserListModel.getToUserAvatar());
                        values.put("status", Integer.valueOf(1));
                    }
                    if ("audio".equals(msg.getType())) {
                        values.put(MsgDBConstant.COLUMN_AUDIO_READ, Integer.valueOf(1));
                    }
                    if (this.writableDatabase.insertOrThrow(MsgDBConstant.TABLE_MSG_LIST.replaceAll("@", String.valueOf(userId)), null, values) == -1) {
                        if (!true) {
                            this.writableDatabase.setTransactionSuccessful();
                        }
                        this.writableDatabase.endTransaction();
                        closeWriteableDB();
                        return false;
                    }
                    j++;
                } catch (Exception e) {
                    e.printStackTrace();
                    isRollBack = true;
                } finally {
                    if (!isRollBack) {
                        this.writableDatabase.setTransactionSuccessful();
                    }
                    this.writableDatabase.endTransaction();
                    closeWriteableDB();
                }
            }
        }
        return true;
    }

    public Map<Long, MsgDBModel> getCacheSessionList(long userId) {
        Exception e;
        Throwable th;
        Map<Long, MsgDBModel> map = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            if (createTable(userId)) {
                cursor = this.writableDatabase.rawQuery(MsgDBConstant.SQL3.replaceAll("@", String.valueOf(userId)), null);
                if (cursor != null && cursor.getCount() > 0) {
                    Map<Long, MsgDBModel> cacheMap = new LinkedHashMap();
                    try {
                        cursor.moveToFirst();
                        do {
                            MsgDBModel msgDBModel = new MsgDBModel();
                            msgDBModel.setPmid(cursor.getLong(cursor.getColumnIndex("mid")));
                            msgDBModel.setPlid(cursor.getLong(cursor.getColumnIndex(MsgDBConstant.COLUMN_SESSION_ID)));
                            msgDBModel.setFromUid(cursor.getLong(cursor.getColumnIndex(MsgDBConstant.COLUMN_OTHER_USER_ID)));
                            msgDBModel.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                            msgDBModel.setContent(cursor.getString(cursor.getColumnIndex("content")));
                            msgDBModel.setType(cursor.getString(cursor.getColumnIndex(MsgDBConstant.COLUMN_MSG_CONTENT_TYPE)));
                            msgDBModel.setSource(cursor.getInt(cursor.getColumnIndex("source")));
                            msgDBModel.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                            msgDBModel.setIcon(cursor.getString(cursor.getColumnIndex(MsgDBConstant.COLUMN_OTHER_USER_ICON)));
                            msgDBModel.setName(cursor.getString(cursor.getColumnIndex(MsgDBConstant.COLUMN_OTHER_USER_NAME)));
                            msgDBModel.setUnReadCount(cursor.getInt(cursor.getColumnIndex("count")));
                            cacheMap.put(Long.valueOf(msgDBModel.getFromUid()), msgDBModel);
                        } while (cursor.moveToNext());
                        map = cacheMap;
                    } catch (Exception e2) {
                        e = e2;
                        map = cacheMap;
                        try {
                            e.printStackTrace();
                            map = null;
                            if (cursor != null) {
                                cursor.close();
                            }
                            closeWriteableDB();
                            return map;
                        } catch (Throwable th2) {
                            th = th2;
                            if (cursor != null) {
                                cursor.close();
                            }
                            closeWriteableDB();
//                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        map = cacheMap;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
//                        throw th;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                closeWriteableDB();
                return map;
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return null;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            map = null;
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return map;
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean modifyMsgStatus(long r9, long r11) {
        boolean ret = false;
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put("status",0);
        String r5 = "table_msg_list_@".replaceAll("@",String.valueOf(r9));
        StringBuilder r6 = new StringBuilder("status=1 and ouid=");
        r6.append(r11);
        if(this.writableDatabase.update(r5,values,r6.toString(),null)>0){
            ret = true;
        }
        this.closeWriteableDB();
        return ret;
        /*
        r8 = this;
        r1 = 0;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x003e }
        r3 = new android.content.ContentValues;	 Catch:{ Exception -> 0x003e }
        r3.<init>();	 Catch:{ Exception -> 0x003e }
        r4 = "status";
        r5 = 0;
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x003e }
        r3.put(r4, r5);	 Catch:{ Exception -> 0x003e }
        r4 = r8.writableDatabase;	 Catch:{ Exception -> 0x003e }
        r5 = "table_msg_list_@";
        r6 = "@";
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x003e }
        r5 = r5.replaceAll(r6, r7);	 Catch:{ Exception -> 0x003e }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x003e }
        r7 = "status=1 and ouid=";
        r6.<init>(r7);	 Catch:{ Exception -> 0x003e }
        r6 = r6.append(r11);	 Catch:{ Exception -> 0x003e }
        r6 = r6.toString();	 Catch:{ Exception -> 0x003e }
        r7 = 0;
        r2 = r4.update(r5, r3, r6, r7);	 Catch:{ Exception -> 0x003e }
        if (r2 > 0) goto L_0x003c;
    L_0x0037:
        r1 = 0;
    L_0x0038:
        r8.closeWriteableDB();
    L_0x003b:
        return r1;
    L_0x003c:
        r1 = 1;
        goto L_0x0038;
    L_0x003e:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0047 }
        r1 = 0;
        r8.closeWriteableDB();
        goto L_0x003b;
    L_0x0047:
        r4 = move-exception;
        r8.closeWriteableDB();
        throw r4;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.modifyMsgStatus(long, long):boolean");
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveSendMsg(com.mobcent.discuz.android.model.MsgContentModel r11, long r12, long r14, java.lang.String r16, java.lang.String r17) {
        boolean ret = false;
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put("mid",0);
        values.put("sid",r11.getPlid());
        values.put("time",r11.getTime());
        values.put("ouid",r14);
        values.put("ouname",r16);
        values.put("contenttype",r11.getType());
        values.put("content",r11.getContent());
        values.put("source",0);
        values.put("ouicon",r17);
        if("audio".equals(r11.getType())){
            values.put("audioread",0);
        }
        values.put("status",1);
        String r7 = "table_msg_list_@".replaceAll("@",String.valueOf(r12));
        if(this.writableDatabase.insertOrThrow(r7,null,values)>-1){
            ret = true;
        }
        this.closeWriteableDB();
        return ret;
        /*
        r10 = this;
        r4 = 0;
        r10.openWriteableDB();	 Catch:{ Exception -> 0x009e }
        r5 = new android.content.ContentValues;	 Catch:{ Exception -> 0x009e }
        r5.<init>();	 Catch:{ Exception -> 0x009e }
        r6 = "mid";
        r7 = 0;
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "sid";
        r7 = r11.getPlid();	 Catch:{ Exception -> 0x009e }
        r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "time";
        r7 = r11.getTime();	 Catch:{ Exception -> 0x009e }
        r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "ouid";
        r7 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "ouname";
        r0 = r16;
        r5.put(r6, r0);	 Catch:{ Exception -> 0x009e }
        r6 = "contenttype";
        r7 = r11.getType();	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "content";
        r7 = r11.getContent();	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "source";
        r7 = 0;
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = "ouicon";
        r0 = r17;
        r5.put(r6, r0);	 Catch:{ Exception -> 0x009e }
        r6 = "audio";
        r7 = r11.getType();	 Catch:{ Exception -> 0x009e }
        r6 = r6.equals(r7);	 Catch:{ Exception -> 0x009e }
        if (r6 == 0) goto L_0x0076;
    L_0x006c:
        r6 = "audioread";
        r7 = 0;
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
    L_0x0076:
        r6 = "status";
        r7 = 1;
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x009e }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x009e }
        r6 = r10.writableDatabase;	 Catch:{ Exception -> 0x009e }
        r7 = "table_msg_list_@";
        r8 = "@";
        r9 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x009e }
        r7 = r7.replaceAll(r8, r9);	 Catch:{ Exception -> 0x009e }
        r8 = 0;
        r2 = r6.insertOrThrow(r7, r8, r5);	 Catch:{ Exception -> 0x009e }
        r6 = -1;
        r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r6 == 0) goto L_0x009a;
    L_0x0099:
        r4 = 1;
    L_0x009a:
        r10.closeWriteableDB();
    L_0x009d:
        return r4;
    L_0x009e:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x00a7 }
        r4 = 0;
        r10.closeWriteableDB();
        goto L_0x009d;
    L_0x00a7:
        r6 = move-exception;
        r10.closeWriteableDB();
        throw r6;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.saveSendMsg(com.mobcent.discuz.android.model.MsgContentModel, long, long, java.lang.String, java.lang.String):boolean");
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean modifyMsgAfterSend(long r11, com.mobcent.discuz.android.model.MsgContentModel r13, long r14) {

        boolean ret = false;
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put("mid",r13.getPmid());
        values.put("sid",r13.getPlid());
        values.put("time",r13.getTime());
        values.put("content",r13.getContent());
        values.put("status",r13.getStatus());
        String r2 = "table_msg_list_@".replaceAll("@",String.valueOf(r14));
        if(this.isRowExisted(this.writableDatabase,r2,"time",r11)){
            StringBuilder stringBuilder = new StringBuilder("time=");
            stringBuilder.append(r11);
            if(this.writableDatabase.update(r2,values,stringBuilder.toString(),null)>0){
                ret = true;
            }
        }
        this.closeWriteableDB();
        return ret;
        /*
        r10 = this;
        r7 = 0;
        r10.openWriteableDB();	 Catch:{ Exception -> 0x0087 }
        r9 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0087 }
        r9.<init>();	 Catch:{ Exception -> 0x0087 }
        r0 = "mid";
        r1 = r13.getPmid();	 Catch:{ Exception -> 0x0087 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0087 }
        r9.put(r0, r1);	 Catch:{ Exception -> 0x0087 }
        r0 = "sid";
        r1 = r13.getPlid();	 Catch:{ Exception -> 0x0087 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0087 }
        r9.put(r0, r1);	 Catch:{ Exception -> 0x0087 }
        r0 = "time";
        r1 = r13.getTime();	 Catch:{ Exception -> 0x0087 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0087 }
        r9.put(r0, r1);	 Catch:{ Exception -> 0x0087 }
        r0 = "content";
        r1 = r13.getContent();	 Catch:{ Exception -> 0x0087 }
        r9.put(r0, r1);	 Catch:{ Exception -> 0x0087 }
        r0 = "status";
        r1 = r13.getStatus();	 Catch:{ Exception -> 0x0087 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x0087 }
        r9.put(r0, r1);	 Catch:{ Exception -> 0x0087 }
        r1 = r10.writableDatabase;	 Catch:{ Exception -> 0x0087 }
        r0 = "table_msg_list_@";
        r2 = "@";
        r3 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0087 }
        r2 = r0.replaceAll(r2, r3);	 Catch:{ Exception -> 0x0087 }
        r3 = "time";
        r0 = r10;
        r4 = r11;
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x0087 }
        if (r0 == 0) goto L_0x0083;
    L_0x005e:
        r0 = r10.writableDatabase;	 Catch:{ Exception -> 0x0087 }
        r1 = "table_msg_list_@";
        r2 = "@";
        r3 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0087 }
        r1 = r1.replaceAll(r2, r3);	 Catch:{ Exception -> 0x0087 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0087 }
        r3 = "time=";
        r2.<init>(r3);	 Catch:{ Exception -> 0x0087 }
        r2 = r2.append(r11);	 Catch:{ Exception -> 0x0087 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0087 }
        r3 = 0;
        r8 = r0.update(r1, r9, r2, r3);	 Catch:{ Exception -> 0x0087 }
        if (r8 <= 0) goto L_0x0083;
    L_0x0082:
        r7 = 1;
    L_0x0083:
        r10.closeWriteableDB();
    L_0x0086:
        return r7;
    L_0x0087:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ all -> 0x0090 }
        r7 = 0;
        r10.closeWriteableDB();
        goto L_0x0086;
    L_0x0090:
        r0 = move-exception;
        r10.closeWriteableDB();
        throw r0;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.MsgDBUtil.modifyMsgAfterSend(long, com.mobcent.discuz.android.model.MsgContentModel, long):boolean");
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteUserMsg(long r8, long r10) {
        boolean ret = false;
        this.openWriteableDB();
        String r4 = "table_msg_list_@";
        r4 = r4.replaceAll("@",String.valueOf(r8));
        StringBuilder r5 = new StringBuilder("ouid=");
        r5.append(r10);
        if(this.writableDatabase.delete(r4,r5.toString(),null)>0){
            ret = true;
        }
        this.closeWriteableDB();
        return ret;
        /*
        r7 = this;
        r2 = 0;
        r7.openWriteableDB();	 Catch:{ Exception -> 0x002d }
        r3 = r7.writableDatabase;	 Catch:{ Exception -> 0x002d }
        r4 = "table_msg_list_@";
        r5 = "@";
        r6 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x002d }
        r4 = r4.replaceAll(r5, r6);	 Catch:{ Exception -> 0x002d }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002d }
        r6 = "ouid=";
        r5.<init>(r6);	 Catch:{ Exception -> 0x002d }
        r5 = r5.append(r10);	 Catch:{ Exception -> 0x002d }
        r5 = r5.toString();	 Catch:{ Exception -> 0x002d }
        r6 = 0;
        r0 = r3.delete(r4, r5, r6);	 Catch:{ Exception -> 0x002d }
        if (r0 <= 0) goto L_0x0029;
    L_0x0028:
        r2 = 1;
    L_0x0029:
        r7.closeWriteableDB();
    L_0x002c:
        return r2;
    L_0x002d:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x0036 }
        r2 = 0;
        r7.closeWriteableDB();
        goto L_0x002c;
    L_0x0036:
        r3 = move-exception;
        r7.closeWriteableDB();
        throw r3;
        */
    }
}
