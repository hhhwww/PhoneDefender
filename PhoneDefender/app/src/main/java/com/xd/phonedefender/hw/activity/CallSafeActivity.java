package com.xd.phonedefender.hw.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.CallSafeAdapter;
import com.xd.phonedefender.hw.bean.BlackNumberInfo;
import com.xd.phonedefender.hw.db.BlackNumberDao;
import com.xd.phonedefender.hw.utils.ToastUtil;

import java.util.List;

public class CallSafeActivity extends AppCompatActivity {

    private ListView listView;
    private LinearLayout linearLayout;
    private CallSafeAdapter callSafeAdapter;
    private BlackNumberDao blackNumberDao;
    private List<BlackNumberInfo> mDatas;


    private int totalPage;
    private int startIndex = 0;
    private int maxCount = 20;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                if (callSafeAdapter == null) {
                    callSafeAdapter = new CallSafeAdapter(CallSafeActivity.this, mDatas);
                    listView.setAdapter(callSafeAdapter);
                } else
                    callSafeAdapter.notifyDataSetChanged();
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
        setListeners();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listview);
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        blackNumberDao = new BlackNumberDao(this);

        totalPage = blackNumberDao.getTotal();
    }

    private void initDatas() {
        linearLayout.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDatas == null)
                    mDatas = blackNumberDao.findPar2(startIndex, maxCount);
                else
                    mDatas.addAll(blackNumberDao.findPar2(startIndex, maxCount));
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void setListeners() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastVisiblePosition = listView.getLastVisiblePosition();
                        if (lastVisiblePosition + 1 == totalPage) {
                            ToastUtil.showMessage("已经没有数据啦");
                            break;
                        }
                        if ((lastVisiblePosition + 1) % 20 == 0) {
                            startIndex += maxCount;
                            initDatas();
//想一想如何设置
                            listView.setSelection(lastVisiblePosition);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    public void addBlackNumber(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final AlertDialog dialog = alertDialog.create();

        View viewBlack = View.inflate(this, R.layout.dialog_add_blacknumber, null);

        final EditText etNumber = (EditText) viewBlack.findViewById(R.id.et_blacknumber);
        final CheckBox cbPhone = (CheckBox) viewBlack.findViewById(R.id.cb_phone);
        final CheckBox cbSms = (CheckBox) viewBlack.findViewById(R.id.cb_sms);
        Button btOk = (Button) viewBlack.findViewById(R.id.bt_ok);
        Button btCancel = (Button) viewBlack.findViewById(R.id.bt_cancel);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etNumber.getText().toString().trim();
                String mode = "";
                if (TextUtils.isEmpty(number)) {
                    ToastUtil.showMessage("请输入黑名单号码");
                    return;
                }

                boolean isPhone = cbPhone.isChecked();
                boolean isSms = cbSms.isChecked();

                if (isPhone && isSms)
                    mode = "1";
                else if (isPhone)
                    mode = "2";
                else if (isSms)
                    mode = "3";
                else {
                    ToastUtil.showMessage("请勾选相应模式");
                    return;
                }

                BlackNumberInfo blackNumberInfo = new BlackNumberInfo(number, mode);
                mDatas.add(blackNumberInfo);
                callSafeAdapter.notifyDataSetChanged();
                blackNumberDao.add(number, mode);
                dialog.dismiss();
            }
        });

        dialog.setView(viewBlack);
        dialog.show();
    }

}
