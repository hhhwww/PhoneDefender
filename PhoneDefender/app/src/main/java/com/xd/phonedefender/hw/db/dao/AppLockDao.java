package com.xd.phonedefender.hw.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xd.phonedefender.hw.application.MyApplication;
import com.xd.phonedefender.hw.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/13.
 */
public class AppLockDao {
    private final AppLockOpenHelper appLockOpenHelper;

    public AppLockDao(Context context) {
        appLockOpenHelper = new AppLockOpenHelper(context);
    }

    public boolean add(String packageName) {
        SQLiteDatabase writableDatabase = appLockOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("packagename", packageName);
        long isRight = writableDatabase.insert(AppLockOpenHelper.TABLE_NAME, null, contentValues);
        writableDatabase.close();

        MyApplication.getContext().getContentResolver().notifyChange(
                Uri.parse("content://hewei"), null
        );

        if (isRight == -1)
            return false;
        else
            return true;
    }

    public boolean delete(String packagename) {
        SQLiteDatabase writableDatabase = appLockOpenHelper.getWritableDatabase();
        int isRight = writableDatabase.delete(AppLockOpenHelper.TABLE_NAME, "packagename = ?", new String[]{packagename});
        writableDatabase.close();

        MyApplication.getContext().getContentResolver().notifyChange(
                Uri.parse("content://hewei"), null
        );

        if (isRight == 0)
            return false;
        else
            return true;
    }

    public boolean find(String packagename) {
        boolean result = false;
        SQLiteDatabase writableDatabase = appLockOpenHelper.getWritableDatabase();
        Cursor query = writableDatabase.query(AppLockOpenHelper.TABLE_NAME, null, "packagename = ?",
                new String[]{packagename}, null, null, null, null);
        if (query.moveToNext())
            result = true;
        query.close();
        return result;
    }

    public List<String> findAll() {
        List<String> mDatas = new ArrayList<>();
        SQLiteDatabase writableDatabase = appLockOpenHelper.getWritableDatabase();
        Cursor query = writableDatabase.query(AppLockOpenHelper.TABLE_NAME, null, null, null, null, null, null, null);
        while (query.moveToNext()) {
            mDatas.add(query.getString(query.getColumnIndex("packagename")));
        }
        return mDatas;
    }

}
