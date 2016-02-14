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
import com.xd.phonedefender.hw.db.dao.BlackNumberDao;
import com.xd.phonedefender.hw.utils.ToastUtil;

import java.util.List;

public class CallSafeFalseActivity extends AppCompatActivity {

    private ListView listView;
    private LinearLayout linearLayout;
    private CallSafeAdapter callSafeAdapter;
    private BlackNumberDao blackNumberDao;
    private List<BlackNumberInfo> mDatas;

    private EditText etNumber;
    private TextView tvNumber;

    private int totalPageNumber;
    private int mCurrentPageSize = 10;
    private int mCurrentPageNumber = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvNumber.setText(mCurrentPageNumber + "/" + totalPageNumber);
                callSafeAdapter = new CallSafeAdapter(CallSafeFalseActivity.this, mDatas);
                listView.setAdapter(callSafeAdapter);
                linearLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsafe);

        initViews();
        initDatas();

        totalPageNumber = blackNumberDao.getTotal() / mCurrentPageSize;
        tvNumber.setText(mCurrentPageNumber + "/" + totalPageNumber);
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
//                mDatas = blackNumberDao.findAll();
                mDatas = blackNumberDao.findPar(mCurrentPageNumber, mCurrentPageSize);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void prePage(View view) {
        if (mCurrentPageNumber <= 0) {
            ToastUtil.showMessage("已经翻到了第一页");
            return;
        }
        mCurrentPageNumber--;
        initDatas();
    }


    public void nextPage(View view) {
        if (mCurrentPageNumber >= totalPageNumber - 1) {
            ToastUtil.showMessage("已经翻到了最后一页");
            return;
        }
        mCurrentPageNumber++;
        initDatas();
    }


    public void jumpPage(View view) {
        String page = etNumber.getText().toString().trim();
        int i = Integer.parseInt(page);
        mCurrentPageNumber = i;
        initDatas();
    }

}
