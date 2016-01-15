package com.xd.phonedefender.hw.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hhhhwei on 16/1/15.
 * BaseSetupActivity不用在MF中配置，因为它不展示
 */
public abstract class BaseSetupActivity extends AppCompatActivity {

    protected GestureDetector gestureDetector;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config", MODE_PRIVATE);


        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY() - e1.getRawX()) > 100)
                    return true;
                if (Math.abs(velocityX) < 100 || Math.abs(velocityY) < 100)
                    return true;
                if (e2.getRawX() - e1.getRawX() > 200)
                    showPreviousPage();
                if (e1.getRawX() - e2.getRawX() > 200)
                    showNextPage();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void next(View view) {
        showNextPage();
    }

    public void previous(View view) {
        showPreviousPage();
    }

    public abstract void showNextPage();

    public abstract void showPreviousPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//为什么要加上这一句
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
