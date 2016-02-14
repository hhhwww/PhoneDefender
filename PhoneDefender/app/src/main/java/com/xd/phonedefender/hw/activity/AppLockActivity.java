package com.xd.phonedefender.hw.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.fragment.LockFragment;
import com.xd.phonedefender.hw.fragment.UnLockFragment;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener {

    private TextView tvUnlock;
    private TextView tvLock;
    private FrameLayout flFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);

        initViews();
        setListeners();
        initDatas();
    }

    private void initViews() {
        tvUnlock = (TextView) findViewById(R.id.tv_unlock);
        tvLock = (TextView) findViewById(R.id.tv_lock);
        flFragment = (FrameLayout) findViewById(R.id.fl_fragment);

        fragmentManager = getSupportFragmentManager();

        unLockFragment = new UnLockFragment();
        lockFragment = new LockFragment();
    }

    private void setListeners() {
        tvUnlock.setOnClickListener(this);
        tvLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_unlock:
                tvUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tvLock.setBackgroundResource(R.drawable.tab_right_default);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_fragment, unLockFragment);
                fragmentTransaction.commit();
                break;

            case R.id.tv_lock:
                tvLock.setBackgroundResource(R.drawable.tab_right_pressed);
                tvUnlock.setBackgroundResource(R.drawable.tab_left_default);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_fragment, lockFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    private void initDatas() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, unLockFragment);
        fragmentTransaction.commit();
    }
}