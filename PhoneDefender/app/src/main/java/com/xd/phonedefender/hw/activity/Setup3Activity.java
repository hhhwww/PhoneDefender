package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup3Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, Setup4Activity.class));
        finish();

        overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in, R.anim.previous_tran_out);
    }

    public void setContact(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }

}
