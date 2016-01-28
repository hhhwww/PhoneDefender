package com.xd.phonedefender.hw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.BlackNumberInfo;

import java.util.List;

/**
 * Created by hhhhwei on 16/1/28.
 */
public class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

    private Context context;
    private List<BlackNumberInfo> mDatas;

    public CallSafeAdapter(Context context, List<BlackNumberInfo> mDatas) {
        super(context, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder myViewHolder = new MyViewHolder();

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.call_safe_item, null);
            myViewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            myViewHolder.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.tvNumber.setText(mDatas.get(i).getNumber());

        String mode = mDatas.get(i).getMode();

        switch (mode) {
            case "1":
                mode = "来电拦截+短信";
                break;

            case "2":
                mode = "电话拦截";
                break;

            case "3":
                mode = "短信拦截";
                break;
        }

        myViewHolder.tvMode.setText(mode);

        return convertView;
    }

    static class MyViewHolder {
        public TextView tvNumber;
        public TextView tvMode;
    }

}
