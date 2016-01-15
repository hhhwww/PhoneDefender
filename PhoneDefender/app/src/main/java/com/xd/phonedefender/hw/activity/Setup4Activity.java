package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initViews();
    }

    private void initViews() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
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
}
