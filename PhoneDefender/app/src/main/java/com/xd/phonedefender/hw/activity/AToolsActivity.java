package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/25.
 */
public class AToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void numberAdressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

}
