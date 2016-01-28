package com.xd.phonedefender.hw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hhhhwei on 16/1/28.
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "blacknumber";

    public BlackNumberOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table blacknumber(" +
                "_id integer primary key autoincrement," +
                "number varchar(20)," +
                "mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
