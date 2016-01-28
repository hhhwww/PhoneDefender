package com.xd.phonedefender.hw.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.xd.phonedefender.hw.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/1/28.
 */
public class BlackNumberDao {
    private final BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberDao(Context context) {
        blackNumberOpenHelper = new BlackNumberOpenHelper(context, "safe.db", null, 1);
    }

    public boolean add(String number, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
        long insert = writableDatabase.insert(BlackNumberOpenHelper.TABLE_NAME, null, contentValues);
        writableDatabase.close();
        if (insert == -1)
            return false;
        else
            return true;
    }

    public boolean delete(String number) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        int delete = writableDatabase.delete(BlackNumberOpenHelper.TABLE_NAME, "number=?", new String[]{number});
        writableDatabase.close();
        if (delete == 0)
            return false;
        else
            return true;
    }

    public boolean change(String number, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        int update = writableDatabase.update(BlackNumberOpenHelper.TABLE_NAME, contentValues, "number=?", new String[]{number});
        writableDatabase.close();
        if (update == 0)
            return false;
        else
            return true;
    }

    public String find(String number) {
        String mode = "";
        SQLiteDatabase readableDatabase = blackNumberOpenHelper.getReadableDatabase();
        Cursor query = readableDatabase.query(BlackNumberOpenHelper.TABLE_NAME, new String[]{"mode"}, "number=?", new String[]{number},
                null, null, null, null);
        if (query.moveToNext())
            mode = query.getString(query.getColumnIndex("mode"));
        query.close();
        readableDatabase.close();
        return mode;
    }

    public List<BlackNumberInfo> findAll() {
        List<BlackNumberInfo> list = new ArrayList<>();
        SQLiteDatabase readableDatabase = blackNumberOpenHelper.getReadableDatabase();
        Cursor query = readableDatabase.query(BlackNumberOpenHelper.TABLE_NAME, new String[]{"number", "mode"}
                , null, null, null, null, null, null);
        while (query.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(query.getString(query.getColumnIndex("number")));
            blackNumberInfo.setMode(query.getString(query.getColumnIndex("mode")));
            list.add(blackNumberInfo);
        }
        if (query != null) query.close();
        readableDatabase.close();
        SystemClock.sleep(2000);
        return list;
    }

    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
        List<BlackNumberInfo> lists = new ArrayList<>();
        SQLiteDatabase readableDatabase = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select number,mode from blacknumber limit ? offset ?",
                new String[]{String.valueOf(pageNumber), String.valueOf(pageNumber * pageSize)});
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            blackNumberInfo.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            lists.add(blackNumberInfo);
        }
        if (cursor != null) cursor.close();
        readableDatabase.close();
        return lists;
    }

    public int getTotal() {
        SQLiteDatabase readableDatabase = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int total = cursor.getInt(0);
        return total;
    }
}
