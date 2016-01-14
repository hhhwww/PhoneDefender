package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void next(View view){
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
    }
}
