package com.xd.phonedefender.hw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.phonedefender.R;

/**
 * Created by hhhhwei on 16/1/12.
 */
public class GvHomeAdapter extends BaseAdapter {

    private String[] lablesName = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
            "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] picturesId = {R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};

    private Context mContext;

    public GvHomeAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lablesName.length;
    }

    @Override
    public Object getItem(int i) {
        return lablesName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(mContext,R.layout.home_list_item,null);

        ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
        TextView tv = (TextView) view.findViewById(R.id.tv_item);

        iv.setImageResource(picturesId[i]);
        tv.setText(lablesName[i]);

        return view;
    }
}
