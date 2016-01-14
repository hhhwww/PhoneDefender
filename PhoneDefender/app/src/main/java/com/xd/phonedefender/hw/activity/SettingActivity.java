package com.xd.phonedefender.hw.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.view.SettingItemView;

/**
 * Created by hhhhwei on 16/1/13.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;

    private SettingItemView settingItemView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        setListeners();

        toJudgeStates();
    }

    private void toJudgeStates() {
        boolean flag = sharedPreferences.getBoolean("auto_update", true);
        if (flag) {
            settingItemView1.setCheckBox(true);
        } else {
            settingItemView1.setCheckBox(false);
        }

        //
    }

    private void initViews() {
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        settingItemView1 = (SettingItemView) findViewById(R.id.siv1);
    }

    private void setListeners() {
        settingItemView1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siv1:
                toJudgeChecked1(settingItemView1);
                break;

            //
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

    //

}
