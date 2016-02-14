package com.xd.phonedefender.hw.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hhhhwei on 16/1/25.
 */
public class AddressDao {
    public static String path = "data/data/com.xd.phonedefender/files/address.db";

    //^1[3-8]\d{9}$
    public static String getAddress(String number) {
        SQLiteDatabase sqLiteDatabase = null;
        String address = "未知号码";
        if (number.matches("^1[3-8]\\d{9}$")) {//匹配的是电话号码
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)"
                    , new String[]{number.substring(0, 7)});
            if (cursor.moveToNext())
                address = cursor.getString(cursor.getColumnIndex("location"));
            if (cursor != null) cursor.close();
        } else if (number.matches("^\\d+$")) {//匹配的是纯数字
            switch (number.length()) {
                case 3:
                    address = "报警电话";
                    break;

                case 4:
                    address = "模拟器";
                    break;

                case 5:
                    address = "客服电话";
                    break;

                case 7:
                case 8:
                    address = "本地电话";
                    break;

                default:
                    //有可能是长途电话，需要查询区号
                    if (number.startsWith("0") && number.length() > 10) {
                        //先查询四位区号
                        Cursor cursor1 = sqLiteDatabase.rawQuery("select location from data2 where area = ?"
                                , new String[]{number.substring(1, 4)});
                        if (cursor1.moveToNext())
                            if (cursor1.moveToNext())
                                address = cursor1.getString(cursor1.getColumnIndex("location"));
                        if (cursor1 != null) cursor1.close();
                        else {
                            Cursor cursor2 = sqLiteDatabase.rawQuery("select location from data2 where area = ?"
                                    , new String[]{number.substring(1, 3)});
                            if (cursor2.moveToNext())
                                address = cursor2.getString(cursor2.getColumnIndex("location"));
                            if (cursor2 != null) cursor2.close();
                        }
                    }
                    break;
            }//switch
        }
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        return address;
    }
}
