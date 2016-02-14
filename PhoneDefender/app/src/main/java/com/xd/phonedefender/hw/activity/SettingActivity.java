package com.xd.phonedefender.hw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.service.AdressService;
import com.xd.phonedefender.hw.service.CallSafeService;
import com.xd.phonedefender.hw.service.WatchDogService;
import com.xd.phonedefender.hw.utils.ServiceStatusUtils;
import com.xd.phonedefender.hw.view.SettingClickView;
import com.xd.phonedefender.hw.view.SettingItemView;

/**
 * Created by hhhhwei on 16/1/13.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;

    private SettingItemView settingItemView1;
    private SettingItemView settingItemView2;

    private SettingClickView settingClickView1;
    private SettingClickView settingClickView2;

    private SettingItemView settingItemView6;

    private String[] items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

    private SettingItemView settingItemViewInterrupt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        setListeners();

        toJudgeStates1();
        toJudgeStates2();
        toJudgeStates3();
        toJudgeStates4();
        toJudgeStates5();
        toJudgeStates6();
    }

    private void toJudgeStates6() {
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.xd.phonedefender.hw.service.WatchDogService");
        if (serviceRunning)
            settingItemView6.setCheckBox(true);
        else
            settingItemView6.setCheckBox(false);
    }

    private void toJudgeStates5() {
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.xd.phonedefender.hw.service.CallSafeService");
        if (serviceRunning)
            settingItemViewInterrupt.setCheckBox(true);
        else
            settingItemViewInterrupt.setCheckBox(false);
    }

    private void toJudgeStates1() {
        boolean flag = sharedPreferences.getBoolean("auto_update", true);
        if (flag) {
            settingItemView1.setCheckBox(true);
        } else {
            settingItemView1.setCheckBox(false);
        }
    }

    private void toJudgeStates2() {
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.xd.phonedefender.hw.service.AdressService");
        if (serviceRunning)
            settingItemView2.setCheckBox(true);
        else
            settingItemView2.setCheckBox(false);
    }

    private void toJudgeStates3() {
        int position = sharedPreferences.getInt("address_style", 0);
        settingClickView1.setDesc(items[position]);
    }

    private void toJudgeStates4() {
        settingClickView2.setDesc("设置归属地提示框的显示位置");
    }

    private void initViews() {
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        settingItemView1 = (SettingItemView) findViewById(R.id.siv1);
        settingItemView2 = (SettingItemView) findViewById(R.id.siv2);
        settingItemView6 = (SettingItemView) findViewById(R.id.siv6);

        settingClickView1 = (SettingClickView) findViewById(R.id.siv3);
        settingClickView2 = (SettingClickView) findViewById(R.id.siv4);

        settingItemViewInterrupt = (SettingItemView) findViewById(R.id.siv5);
    }

    private void setListeners() {
        settingItemView1.setOnClickListener(this);
        settingItemView2.setOnClickListener(this);
        settingItemView6.setOnClickListener(this);

        settingClickView1.setOnClickListener(this);
        settingClickView2.setOnClickListener(this);

        settingItemViewInterrupt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siv1:
                toJudgeChecked1(settingItemView1);
                break;

            case R.id.siv2:
                toJudgeChecked2(settingItemView2);
                break;

            case R.id.siv3:
                showSingleChoiceDialog();
                break;

            case R.id.siv4:
                startActivity(new Intent(this, DragViewActivity.class));
                break;

            case R.id.siv5:
                toJudgeChecked5();
                break;

            case R.id.siv6:
                toJudgeChecked6();
                break;
        }
    }

    private void toJudgeChecked6() {
        if (settingItemView6.isChecked()) {
            settingItemView6.setCheckBox(false);
            stopService(new Intent(this, WatchDogService.class));
        } else {
            settingItemView6.setCheckBox(true);
            startService(new Intent(this, WatchDogService.class));
        }
    }

    private void toJudgeChecked5() {
        if (settingItemViewInterrupt.isChecked()) {
            settingItemViewInterrupt.setCheckBox(false);
            stopService(new Intent(this, CallSafeService.class));
        } else {
            settingItemViewInterrupt.setCheckBox(true);
            startService(new Intent(this, CallSafeService.class));
        }
    }

    private void toJudgeChecked1(SettingItemView settingItemView) {
        if (settingItemView.isChecked()) {
            settingItemView.setCheckBox(false);

            sharedPreferences.edit().putBoolean("auto_update", false).commit();
        } else {
            settingItemView.setCheckBox(true);

            sharedPreferences.edit().putBoolean("auto_update", true).commit();
        }
    }

    private void toJudgeChecked2(SettingItemView settingItemView2) {
        if (settingItemView2.isChecked()) {
            settingItemView2.setCheckBox(false);
            stopService(new Intent(this, AdressService.class));
        } else {
            settingItemView2.setCheckBox(true);
            startService(new Intent(this, AdressService.class));
        }
    }

    private void showSingleChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("归属地提示框风格");
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPreferences.edit().putInt("address_style", i).commit();
                toJudgeStates3();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }


}
