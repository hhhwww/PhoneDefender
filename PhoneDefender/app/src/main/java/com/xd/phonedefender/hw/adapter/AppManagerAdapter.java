package com.xd.phonedefender.hw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.AppInfo;
import com.xd.phonedefender.hw.utils.DensityUtil;

import java.util.List;

/**
 * Created by hhhhwei on 16/1/29.
 */
public class AppManagerAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> mDatas;

    private List<AppInfo> mUserDatas;
    private List<AppInfo> mSysDatas;

    public AppManagerAdapter(Context context, List<AppInfo> mDatas, List<AppInfo> userDatas, List<AppInfo> sysDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.mUserDatas = userDatas;
        this.mSysDatas = sysDatas;
    }

    @Override
    public int getCount() {
        return mUserDatas.size() + mSysDatas.size() + 2;
    }

    @Override
    public Object getItem(int i) {
        AppInfo appInfo;

        if (i == 0 || i == mUserDatas.size() + 1)
            return null;

        if (i < mUserDatas.size() + 1)
            appInfo = mUserDatas.get(i - 1);
        else
            appInfo = mSysDatas.get(i - mUserDatas.size() - 2);

        return appInfo;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AppInfo appInfo;

        if (i == 0) {
            return initFlag("用户程序", mUserDatas.size());
        } else if (i == mUserDatas.size() + 1)
            return initFlag("系统程序", mSysDatas.size());

        if (i < mUserDatas.size() + 1)
            appInfo = mUserDatas.get(i - 1);
        else
            appInfo = mSysDatas.get(i - mUserDatas.size() - 2);

        MyViewHolder myViewHolder;
        if (view != null && view instanceof RelativeLayout) {
            myViewHolder = (MyViewHolder) view.getTag();
        } else {
            myViewHolder = new MyViewHolder();
            view = View.inflate(context, R.layout.app_manager_item, null);
            myViewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            myViewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
            myViewHolder.tvPosition = (TextView) view.findViewById(R.id.tv_position);
            myViewHolder.tvSize = (TextView) view.findViewById(R.id.tv_size);
            view.setTag(myViewHolder);
        }

        myViewHolder.ivIcon.setBackground(appInfo.getApkIcon());
        myViewHolder.tvName.setText(appInfo.getApkName());
        myViewHolder.tvSize.setText(Formatter.formatFileSize(context, appInfo.getApkSize()));

        if (appInfo.isRom())
            myViewHolder.tvPosition.setText("手机内存");
        else
            myViewHolder.tvPosition.setText("SD卡");

        return view;
    }

    private View initFlag(String name, int size) {
        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.GRAY);
        textView.setText(name + "(" + size + ")");
        textView.setTextSize(DensityUtil.px2dip(context, 20));
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textView.setGravity(Gravity.CENTER);
//        textView.setLayoutParams(layoutParams);
        return textView;
    }

    static class MyViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvPosition;
        public TextView tvSize;
    }
}
