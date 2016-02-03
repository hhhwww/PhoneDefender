package com.xd.phonedefender.hw.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import com.xd.phonedefender.hw.interfa.BackUpCallBackSms;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by hhhhwei on 16/2/3.
 */
public class SmsUtils {


    public static boolean backUpSms(Context context, BackUpCallBackSms backUpCallBackSms) throws Exception {

        int process = 1;
        int count = 0;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fileOutputStream, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");

            ContentResolver resolver = context.getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

            count = cursor.getCount();
            backUpCallBackSms.beforeBackUp(count);

            while (cursor.moveToNext()) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String body = cursor.getString(cursor.getColumnIndex("body"));

                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");


                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");


                xmlSerializer.startTag(null, "date");
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");


                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");

                xmlSerializer.endTag(null, "sms");

                backUpCallBackSms.onBackUp(process++);

            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            if (cursor != null) cursor.close();
            if (fileOutputStream != null) fileOutputStream.close();
            return true;
        }
        return false;
    }

}
