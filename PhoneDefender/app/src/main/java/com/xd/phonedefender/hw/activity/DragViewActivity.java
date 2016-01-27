package com.xd.phonedefender.hw.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/26.
 */
public class DragViewActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView ivDrag;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float dx;
    private float dy;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);

        initViews();
        setListeners();

        setStates();
    }

//不能设置直接居中
    private void setStates() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = sp.getInt("lastX", 0);
        layoutParams.topMargin = sp.getInt("lastY", 0);
        ivDrag.setLayoutParams(layoutParams);
    }

    private void initViews() {
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void setListeners() {
        ivDrag.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.iv_drag) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getRawX();
                    startY = motionEvent.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    endX = motionEvent.getRawX();
                    endY = motionEvent.getRawY();

                    dx = endX - startX;
                    dy = endY - startY;

                    float l = ivDrag.getLeft() + dx;
                    float t = ivDrag.getTop() + dy;

                    float r = ivDrag.getRight() + dx;
                    float b = ivDrag.getBottom() + dy;

                    ivDrag.layout((int) l, (int) t, (int) r, (int) b);

                    //初始化起点坐标
                    startX = motionEvent.getRawX();
                    startY = motionEvent.getRawY();

                    break;

                case MotionEvent.ACTION_UP:
                    sp.edit().putInt("lastX", ivDrag.getLeft()).commit();
                    sp.edit().putInt("lastY", ivDrag.getTop()).commit();
                    break;
            }//switch
        }
        return true;
    }
}
