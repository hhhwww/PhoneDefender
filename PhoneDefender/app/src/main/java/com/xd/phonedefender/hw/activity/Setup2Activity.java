package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.view.SettingItemView;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup2Activity extends AppCompatActivity implements View.OnClickListener {
    private SettingItemView settingItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initViews();
        setListeners();
    }

    private void initViews() {
        settingItemView = (SettingItemView) findViewById(R.id.siv1);
    }

    private void setListeners() {
        settingItemView.setOnClickListener(this);
    }

    public void next(View view) {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
    }

    public void previous(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in,R.anim.previous_tran_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siv1:
                if (settingItemView.isChecked())
                    settingItemView.setCheckBox(false);
                else
                    settingItemView.setCheckBox(true);
                break;
        }
    }
}
