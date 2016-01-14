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
public class LostFindActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config",MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed",false);
        if(!configed){
            startActivity(new Intent(this,Setup1Activity.class));
        }
        else
            setContentView(R.layout.activity_lostfind);
    }

    public void reEnter(View view){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }

}
