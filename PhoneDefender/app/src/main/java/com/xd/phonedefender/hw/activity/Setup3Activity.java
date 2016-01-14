package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void next(View view) {
        startActivity(new Intent(this, Setup4Activity.class));
        finish();

        overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
    }

    public void previous(View view) {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in, R.anim.previous_tran_out);
    }
}
