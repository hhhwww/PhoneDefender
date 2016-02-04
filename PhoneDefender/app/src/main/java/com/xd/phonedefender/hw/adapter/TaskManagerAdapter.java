package com.xd.phonedefender.hw.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.TaskInfo;

import java.util.List;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class TaskManagerAdapter extends BaseAdapter {

    private Context context;
    private List<TaskInfo> userDatas;
    private List<TaskInfo> sysDatas;

    public TaskManagerAdapter(Context context, List<TaskInfo> userDatas, List<TaskInfo> sysDatas) {
        this.context = context;
        this.userDatas = userDatas;
        this.sysDatas = sysDatas;
    }

    @Override
    public int getCount() {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean is_show_system = sp.getBoolean("is_show_system", false);
        if (is_show_system)
            return userDatas.size() + sysDatas.size() + 2;
        else
            return userDatas.size() + 1;
    }

    @Override
    public Object getItem(int i) {

        if (i == 0 || i == userDatas.size() + 1)
            return null;

        if (i <= userDatas.size())
            return userDatas.get(i - 1);
        else
            return sysDatas.get(i - userDatas.size() - 2);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TaskInfo taskInfo = null;

        if (i == 0) {
            return initFlags("用户程序", userDatas.size());
        } else if (i == userDatas.size() + 1) {
            return initFlags("系统程序", sysDatas.size());
        }

        TaskManagerAdapter.MyViewHolder myViewHolder;
        if (view != null && view instanceof LinearLayout) {
            myViewHolder = (MyViewHolder) view.getTag();
        } else {
            myViewHolder = new TaskManagerAdapter.MyViewHolder();
            view = View.inflate(context, R.layout.item_task_manager, null);
            myViewHolder.ivAppIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
            myViewHolder.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
            myViewHolder.tvAppMemory = (TextView) view.findViewById(R.id.tv_memory);
            myViewHolder.cbFire = (CheckBox) view.findViewById(R.id.cb_fire);
            view.setTag(myViewHolder);
        }

        if (i <= userDatas.size())
            taskInfo = userDatas.get(i - 1);
        else
            taskInfo = sysDatas.get(i - userDatas.size() - 2);

        myViewHolder.ivAppIcon.setImageDrawable(taskInfo.getAppIcon());
        myViewHolder.tvAppName.setText(taskInfo.getAppName());
        myViewHolder.tvAppMemory.setText(
                Formatter.formatFileSize(context, taskInfo.getMemorySize()));

        if (taskInfo.isChecked())
            myViewHolder.cbFire.setChecked(true);
        else
            myViewHolder.cbFire.setChecked(false);

        if (taskInfo.getPackageName().equals(context.getPackageName()))
            myViewHolder.cbFire.setVisibility(View.INVISIBLE);

        return view;
    }

    private View initFlags(String name, int count) {
        TextView tv = new TextView(context);
        tv.setText(name + ":" + count + "个");
        tv.setBackgroundColor(Color.GRAY);
        return tv;
    }

    static class MyViewHolder {
        public ImageView ivAppIcon;
        public TextView tvAppName;
        public TextView tvAppMemory;
        public CheckBox cbFire;
    }
}
