package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup4Activity extends AppCompatActivity {

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

    public void next(View view) {
        sp.edit().putBoolean("configed", true).commit();
        startActivity(new Intent(this, LostFindActivity.class));
        finish();
    }

    public void previous(View view) {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
    }
}
