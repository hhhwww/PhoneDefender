package com.xd.phonedefender.hw.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class AntivirusDao {
    public static String isAntivirus(String md5, Context context) {
        String desc = "";
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(context.getFilesDir().toString() + "/antivirus.db"
                , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.rawQuery("select desc from datable where md5 = ?", new String[]{md5});
        if (cursor.moveToNext()) {
            desc = cursor.getString(cursor.getColumnIndex("desc"));
        }
        return desc;
    }

    public static void addAntivirus(String md5, String desc, Context context) {
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(context.getFilesDir().toString() + "/antivirus.db"
                , null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues contentValues = new ContentValues();
        contentValues.put("md5", md5);
        contentValues.put("desc", desc);
        contentValues.put("type", 6);
        contentValues.put("name", "Android.Adware.AirAD.a");
        sqLiteDatabase.insert("datable", null, contentValues);
    }
}
