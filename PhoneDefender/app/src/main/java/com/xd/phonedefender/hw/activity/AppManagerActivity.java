package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.AppManagerAdapter;
import com.xd.phonedefender.hw.bean.AppInfo;
import com.xd.phonedefender.hw.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/1/29.
 */
public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSd;
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    private List<AppInfo> mDatas;
    private AppManagerAdapter appManagerAdapter;

    private List<AppInfo> userInfos;
    private List<AppInfo> sysInfos;

    private PopupWindow popupWindow;

    private LinearLayout llUninstall;
    private LinearLayout llStart;
    private LinearLayout llShare;
    private LinearLayout llDetail;

    private AppInfo clickedAppInfo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (appManagerAdapter == null) {
                appManagerAdapter = new AppManagerAdapter(AppManagerActivity.this, mDatas, userInfos, sysInfos);
                listView.setAdapter(appManagerAdapter);
            } else
                appManagerAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appm);
        ViewUtils.inject(this);

        initViews();
        initDatas();
        setListeners();
    }

    private void setListeners() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int i1, int i2) {

                if (popupWindow != null) popupWindow.dismiss();

                if (sysInfos != null && userInfos != null) {
                    if (first > userInfos.size() + 1)
                        tvTitle.setText("系统程序" + "(" + sysInfos.size() + ")");
                    else
                        tvTitle.setText("用户程序" + "(" + userInfos.size() + ")");
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                clickedAppInfo = (AppInfo) listView.getItemAtPosition(i);

                if (popupWindow != null) popupWindow.dismiss();

                View v = View.inflate(AppManagerActivity.this, R.layout.item_popup, null);
                popupWindow = new PopupWindow(v, -2, -2);

                int[] location = new int[2];
                view.getLocationInWindow(location);

                popupWindow.showAtLocation(adapterView, Gravity.LEFT + Gravity.TOP, 50, location[1]);

                llUninstall = (LinearLayout) v.findViewById(R.id.ll_uninstall);
                llStart = (LinearLayout) v.findViewById(R.id.ll_start);
                llShare = (LinearLayout) v.findViewById(R.id.ll_share);
                llDetail = (LinearLayout) v.findViewById(R.id.ll_detail);

                llUninstall.setOnClickListener(AppManagerActivity.this);
                llStart.setOnClickListener(AppManagerActivity.this);
                llShare.setOnClickListener(AppManagerActivity.this);
                llDetail.setOnClickListener(AppManagerActivity.this);
            }
        });


    }

    private void initDatas() {
        long rom = Environment.getDataDirectory().getFreeSpace();
        long sd = Environment.getExternalStorageDirectory().getFreeSpace();

        tvRom.setText("内存可用:" + Formatter.formatFileSize(this, rom));
        tvSd.setText("SD可用:" + Formatter.formatFileSize(this, sd));

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = AppInfos.getAppInfos(AppManagerActivity.this);
                countSize();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void countSize() {
        userInfos = new ArrayList<>();
        sysInfos = new ArrayList<>();

        for (AppInfo appInfo : mDatas) {
            if (appInfo.isUserApp())
                userInfos.add(appInfo);
            else
                sysInfos.add(appInfo);
        }

    }

    private void initViews() {
        mDatas = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) popupWindow.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_uninstall:
                if (clickedAppInfo != null) {
                    Intent intentUnstall = new Intent();
                    intentUnstall.setAction("android.intent.action.VIEW");
                    intentUnstall.addCategory("android.intent.category.DEFAULT");
                    intentUnstall.setData(Uri.parse("package:" + clickedAppInfo.getApkPackageName()));
                    startActivity(intentUnstall);
                    popupWindow.dismiss();
                }
                break;

            case R.id.ll_start:
                if (clickedAppInfo != null) {
                    String apkPackageName = clickedAppInfo.getApkPackageName();
                    Intent intent = getPackageManager().getLaunchIntentForPackage(apkPackageName);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
                break;

            case R.id.ll_share:
                if (clickedAppInfo != null) {
                    Intent intentShare = new Intent("android.intent.action.SEND");
                    intentShare.setType("text/plain");
                    intentShare.putExtra("android.intent.extra.SUBJECT", "f分享");
                    intentShare.putExtra("android.intent.extra.TEXT", "Hi,推荐您使用软件:" + clickedAppInfo.getApkName()
                            + "下载地址:" + "https://play.google.com/stores/apps/details?id=" + clickedAppInfo.getApkPackageName());
                    startActivity(intentShare);
                    popupWindow.dismiss();
                }
                break;

            case R.id.ll_detail:
                Intent intentDetail = new Intent();
                intentDetail.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intentDetail.addCategory(Intent.CATEGORY_DEFAULT);
                intentDetail.setData(Uri.parse("package:" + clickedAppInfo.getApkPackageName()));
                startActivity(intentDetail);
                popupWindow.dismiss();
                break;
        }
    }

}
