package com.xd.phonedefender.hw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.GvHomeAdapter;

/**
 * Created by hhhhwei on 16/1/11.
 */
public class HomeActivity extends AppCompatActivity {

    private GridView gvHome;
    private GvHomeAdapter gvHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        gvHome.setAdapter(gvHomeAdapter);
    }

    private void initViews() {
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHomeAdapter = new GvHomeAdapter(this);
    }

}
