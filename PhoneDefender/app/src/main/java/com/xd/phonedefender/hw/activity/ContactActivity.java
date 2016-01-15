package com.xd.phonedefender.hw.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xd.phonedefender.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hhhhwei on 16/1/15.
 */
public class ContactActivity extends AppCompatActivity {

    private ListView listView;
    private List<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        readContacts();

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new SimpleAdapter(this, list, R.layout.contact_list_item
                , new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_number}));
    }

    public List<HashMap<String, String>> readContacts() {
        //把表先从com.android.provider.contacts中揪出来,再看,注意几张表raw_contacts,data,mimetype
        //首先,从raw_contacts表中读取"contact_id"
        //其次,根据contact_id从data表中查询除相应的电话号码和联系人名称
        //然后,根据mimetype_id在mimetypes表中区分哪个是联系人，哪个是电话号码----注意视图在这的应用

        list = new ArrayList<>();

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        Cursor rawContactsCursor = getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);

        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(rawContactsCursor.getColumnIndex("contact_id"));
                Cursor rawDatasCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);
                if (rawDatasCursor != null) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    while (rawDatasCursor.moveToNext()) {
                        String data1 = rawDatasCursor.getString(rawDatasCursor.getColumnIndex("data1"));
                        String mimetype = rawDatasCursor.getString(rawDatasCursor.getColumnIndex("mimetype"));
                        if (mimetype.equals("vnd.android.cursor.item/name"))
                            hashMap.put("name", data1);
                        else if (mimetype.equals("vnd.android.cursor.item/phone_v2"))
                            hashMap.put("phone", data1);
                    }
                    list.add(hashMap);
                    rawDatasCursor.close();
                }//if
            }//while
            rawContactsCursor.close();
        }//if

        return list;
    }

}
