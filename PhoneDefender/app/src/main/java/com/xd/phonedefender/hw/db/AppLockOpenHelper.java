package com.xd.phonedefender.hw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hhhhwei on 16/2/13.
 */
public class AppLockOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "info";

    public AppLockOpenHelper(Context context) {
        super(context, "applock.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table info(" +
                "_id integer primary key autoincrement," +
                "packagename varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
