package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup4Activity extends BaseSetupActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initViews();
        setListeners();

        boolean protect = sp.getBoolean("protect", false);
        if (protect) {
            cbProtect.setChecked(true);
            cbProtect.setText("防盗保护已经开启");
        } else {
            cbProtect.setChecked(false);
            cbProtect.setText("防盗保护没有开启");
        }
    }

    private void setListeners() {
        cbProtect.setOnCheckedChangeListener(this);
    }

    private void initViews() {
        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
    }

    @Override
    public void showNextPage() {
        sp.edit().putBoolean("configed", true).commit();
        startActivity(new Intent(this, LostFindActivity.class));
        finish();

        overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in, R.anim.previous_tran_out);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_protect:
                if (b) {
                    cbProtect.setText("防盗保护已经开启");
                    sp.edit().putBoolean("protect", true).commit();
                } else {
                    cbProtect.setText("防盗保护没有开启");
                    sp.edit().putBoolean("protect", false).commit();
                }
                break;
        }
    }
}
