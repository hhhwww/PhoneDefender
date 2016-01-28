package com.xd.phonedefender.hw.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/26.
 */
public class DragViewActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private ImageView ivDrag;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float dx;
    private float dy;

    private SharedPreferences sp;

    private int winWidth;
    private int winHeight;

    private TextView tvTop;
    private TextView tvBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);

        initViews();
        setListeners();

        winWidth = getWindowManager().getDefaultDisplay().getWidth();
        winHeight = getWindowManager().getDefaultDisplay().getHeight();

        setStates();

    }

    //不能设置直接居中
    private void setStates() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = sp.getInt("lastX", 0);
        layoutParams.topMargin = sp.getInt("lastY", 0);
        ivDrag.setLayoutParams(layoutParams);

        if (sp.getInt("lastY", 0) < (winHeight - 20) / 2) {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        } else {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);

        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void setListeners() {
        ivDrag.setOnTouchListener(this);
        ivDrag.setOnClickListener(this);
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

                    if (l < 0 || t < 0 || r > winWidth || b > winHeight - 20)
                        break;

                    if (t < (winHeight - 20) / 2) {
                        tvTop.setVisibility(View.INVISIBLE);
                        tvBottom.setVisibility(View.VISIBLE);
                    } else {
                        tvTop.setVisibility(View.VISIBLE);
                        tvBottom.setVisibility(View.INVISIBLE);
                    }

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
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_drag) {
            toJudgeTimes();
        }
    }

    private long[] times = new long[2];

    private void toJudgeTimes() {
        System.arraycopy(times, 1, times, 0, times.length - 1);
        times[times.length - 1] = System.currentTimeMillis();
        if (System.currentTimeMillis() - times[0] < 500)
            ivDrag.layout(winWidth / 2 - ivDrag.getWidth() / 2, ivDrag.getTop(),
                    winWidth / 2 + ivDrag.getWidth() / 2, ivDrag.getBottom());
    }
}
