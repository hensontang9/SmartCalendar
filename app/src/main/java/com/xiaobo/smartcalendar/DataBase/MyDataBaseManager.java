package com.xiaobo.smartcalendar.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;

import java.util.UUID;

public class MyDataBaseManager {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDataBase.db";

    private Context mContext;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public static MyDataBaseManager instance = null;

    private static final String CREATE_TABLE_MYEVENTS = "create table " + MyEventDbSchema.MyEventTable.NAME +
            "(" + "_id integer primary key autoincrement," +
            MyEventDbSchema.MyEventTable.Cols.UUID + "," +
            MyEventDbSchema.MyEventTable.Cols.TITLE + ", " +
            MyEventDbSchema.MyEventTable.Cols.START_TIME + ", " +
            MyEventDbSchema.MyEventTable.Cols.CALENDAR + ", " +
            MyEventDbSchema.MyEventTable.Cols.DURATION + ", " +
            MyEventDbSchema.MyEventTable.Cols.TYPE + ", " +
            MyEventDbSchema.MyEventTable.Cols.LOCATION + ", " +
            MyEventDbSchema.MyEventTable.Cols.LONGITUDE + ", " +
            MyEventDbSchema.MyEventTable.Cols.LATITUDE + ", " +
            MyEventDbSchema.MyEventTable.Cols.HOST + ", " +
            MyEventDbSchema.MyEventTable.Cols.PARTICIPANT + ", " +
            MyEventDbSchema.MyEventTable.Cols.PERIODICITY + ", " +
            MyEventDbSchema.MyEventTable.Cols.IMPORTANT + "," +
            MyEventDbSchema.MyEventTable.Cols.COMMUTING_TIME + "," +
            MyEventDbSchema.MyEventTable.Cols.MINIMUM_DURATION + "," +
            MyEventDbSchema.MyEventTable.Cols.EARLIEST_START_TIME + "," +
            MyEventDbSchema.MyEventTable.Cols.LATEST_START_TIME + "," +
            MyEventDbSchema.MyEventTable.Cols.EARLIEST_END_TIME + "," +
            MyEventDbSchema.MyEventTable.Cols.LATEST_END_TIME + "," +
            MyEventDbSchema.MyEventTable.Cols.CITY + ")";

    private static final String CREATE_TABLE_MYTEMPORALINCONSISTENCYS = "create table " + MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME +
            "(" + "_id integer primary key autoincrement," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT1 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT2 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CTC + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.COMMUTING_TIME + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CONFLICTING_LENGTH + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION1 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION2 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION3 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION4 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION1 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION2 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION3 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION4 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION1 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION2 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION3 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION4 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION1 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION2 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION3 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION4 + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.ERROR + "," +
            MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.HANDLED + ")";

    
    private MyDataBaseManager() {




        Log.d("MyDataBaseManager", "数据库管理对象创建");

    }

    
    public static final MyDataBaseManager getInstance() {
        if (instance == null) {
            instance = new MyDataBaseManager();
        }
        return instance;
    }

    
    public void initMyDataBaseManager(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context);
        mDatabase = dbHelper.getWritableDatabase();

        Log.d("MyDataBaseManager", "数据库管理对象初始化");
    }

    
    public void close() {
        if (mDatabase.isOpen()) {
            mDatabase.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (instance != null) {
            instance = null;
        }

    }
    private static class MyDatabaseHelper extends SQLiteOpenHelper
    {
        MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("MyDataBaseManager", "创建事件表");
            db.execSQL(CREATE_TABLE_MYEVENTS);
            Log.d("MyDataBaseManager", "创建冲突表");
            db.execSQL(CREATE_TABLE_MYTEMPORALINCONSISTENCYS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            
        }
    }

    
    public Long insertData(String table, ContentValues values) {
        Log.d("MyDataBaseManager", "开始准备添加到数据库");
        long result = 0;
        if(mDatabase.isOpen()){
            result = mDatabase.insert(table, null, values);
        }

        Log.d("MyDataBaseManager", "DataBase添加到数据库成功");

        return result;
    }

    
    public Long deleteData(String table, String uuid) {
        long result = 0;
        Log.d("MyDataBaseManager", "删除数据 table:" + table + " uuid:" + uuid);
        Log.d("MyDataBaseManager", "同时删除事件所在冲突");
        MyTemporalInconsistencyManager.getInstance().deleteMyInconWithEventID(UUID.fromString(uuid));
        if (mDatabase.isOpen()) {
            result = mDatabase.delete(table, "UUID=?", new String[]{uuid.toString()});
        }

        return result;
    }

    public Long deleteDataAsRule(String table, String uuid) {
        long result = 0;
        if (mDatabase.isOpen()) {
            result = mDatabase.delete(table, "UUID LIKE ?", new String[]{"%" + uuid.toString() + "%"});
        }

        return result;
    }

    
    public MyEventCursorWrapper queryData(String table, String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new MyEventCursorWrapper(cursor);
    }
    public MyTemporalInconsistencyCursorWrapper queryInconData(String table, String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new MyTemporalInconsistencyCursorWrapper(cursor);
    }
}
