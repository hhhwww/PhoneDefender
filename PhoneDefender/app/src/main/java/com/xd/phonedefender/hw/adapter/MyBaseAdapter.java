package com.xd.phonedefender.hw.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hhhhwei on 16/1/28.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> mDatas;

    public MyBaseAdapter(Context context, List<T> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
