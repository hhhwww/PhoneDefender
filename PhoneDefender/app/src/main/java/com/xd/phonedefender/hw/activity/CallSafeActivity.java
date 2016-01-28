package com.xd.phonedefender.hw.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.CallSafeAdapter;
import com.xd.phonedefender.hw.bean.BlackNumberInfo;
import com.xd.phonedefender.hw.db.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends AppCompatActivity {

    private ListView listView;
    private LinearLayout linearLayout;
    private CallSafeAdapter callSafeAdapter;
    private BlackNumberDao blackNumberDao;
    private List<BlackNumberInfo> mDatas;

    private EditText etNumber;
    private TextView tvNumber;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                callSafeAdapter = new CallSafeAdapter(CallSafeActivity.this, mDatas);
                listView.setAdapter(callSafeAdapter);
                linearLayout.setVisibility(View.INVISIBLE);
            }
        }
    };
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsafe);

        initViews();
        initDatas();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listview);
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        blackNumberDao = new BlackNumberDao(this);

        etNumber = (EditText) findViewById(R.id.et_pagenumber);
        tvNumber = (TextView) findViewById(R.id.tv_pagenumber);
    }

    private void initDatas() {
        linearLayout.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = blackNumberDao.findAll();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void prePage(View view) {

    }


    public void nextPage(View view) {

    }


    public void jumpPage(View view) {

    }

}
