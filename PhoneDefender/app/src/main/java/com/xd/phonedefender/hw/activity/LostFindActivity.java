package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class LostFindActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private TextView tvSafePhone;
    private ImageView ivLock;

    private boolean isLock;
    private String safePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);
        if (!configed) {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        } else {
            setContentView(R.layout.activity_lostfind);
            initViews();
            setInformation();
        }
    }

    private void setInformation() {
        isLock = sp.getBoolean("protect", false);
        safePhone = sp.getString("safe_phone", "");

        if (isLock) {
            ivLock.setImageResource(R.drawable.lock);
            tvSafePhone.setText(safePhone);
        } else {
            ivLock.setImageResource(R.drawable.unlock);
            tvSafePhone.setText("");
        }
    }

    private void initViews() {
        tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
        ivLock = (ImageView) findViewById(R.id.iv_lock);
    }

    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }

}
