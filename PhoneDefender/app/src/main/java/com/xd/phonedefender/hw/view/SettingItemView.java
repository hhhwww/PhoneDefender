package com.xd.phonedefender.hw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/13.
 */
public class SettingItemView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cb;

    private String title;
    private String onDesc;
    private String offDesc;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mtitle");
        onDesc = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_on");
        offDesc = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_off");

        //初始化的状态
        tvTitle.setText(title);
        setCheckBox(false);
    }

    //注意这个this的含义
    private void initView() {
//这一句非常的重要
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cb = (CheckBox) findViewById(R.id.cb);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    public void setCheckBox(boolean check) {
        cb.setChecked(check);
        if (check)
            setDesc(onDesc);
        else
            setDesc(offDesc);
    }

    public boolean isChecked() {
        return cb.isChecked();
    }

}
