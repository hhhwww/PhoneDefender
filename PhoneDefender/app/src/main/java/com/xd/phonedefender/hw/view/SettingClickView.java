package com.xd.phonedefender.hw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/13.
 */
public class SettingClickView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvDesc;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        String mtitle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mtitle");
        setTitle(mtitle);
    }

    //注意这个this的含义
    private void initView() {
//这一句非常的重要
        View.inflate(getContext(), R.layout.view_click_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }
}
