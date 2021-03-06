package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.utils.ToastUtil;
import com.xd.phonedefender.hw.view.SettingItemView;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup2Activity extends BaseSetupActivity implements View.OnClickListener {

    private SettingItemView settingItemView;

    private String sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initViews();
        setListeners();

        String saveSim = sp.getString("sim", null);
        if (TextUtils.isEmpty(saveSim))
            settingItemView.setCheckBox(false);
        else
            settingItemView.setCheckBox(true);
    }

    private void initViews() {
        settingItemView = (SettingItemView) findViewById(R.id.siv1);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        sim = telephonyManager.getSimSerialNumber();
    }

    private void setListeners() {
        settingItemView.setOnClickListener(this);
    }

    @Override
    public void showNextPage() {
        if (settingItemView.isChecked()) {
            startActivity(new Intent(this, Setup3Activity.class));
            overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
            finish();
        }else
            ToastUtil.showMessage("请绑定sim卡");
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in, R.anim.previous_tran_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siv1:
                if (settingItemView.isChecked()) {
                    settingItemView.setCheckBox(false);
                    sp.edit().remove("sim").commit();
                } else {
                    settingItemView.setCheckBox(true);
                    sp.edit().putString("sim", sim).commit();
                }
                break;
        }
    }
}
