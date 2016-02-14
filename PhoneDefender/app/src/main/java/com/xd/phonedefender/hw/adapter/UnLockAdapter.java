package com.xd.phonedefender.hw.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.AppInfo;
import com.xd.phonedefender.hw.db.dao.AppLockDao;

import java.util.List;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class UnLockAdapter extends MyBaseAdapter<AppInfo> {

    private Context context;
    private List<AppInfo> mDatas;
    private AppLockDao appLockDao;

    public UnLockAdapter(Context context, List<AppInfo> mDatas, AppLockDao appLockDao) {
        super(context, mDatas);
        this.context = context;
        this.mDatas = mDatas;
        this.appLockDao = appLockDao;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder myViewHolder = new MyViewHolder();
        final View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_unlock, null);
            myViewHolder.ivAppIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
            myViewHolder.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
            myViewHolder.ivIsLock = (ImageView) view.findViewById(R.id.iv_unlock);
            view.setTag(myViewHolder);
        } else {
            view = convertView;
            myViewHolder = (MyViewHolder) view.getTag();
        }

        myViewHolder.ivAppIcon.setImageDrawable(mDatas.get(i).getApkIcon());
        myViewHolder.tvAppName.setText(mDatas.get(i).getApkName());

        myViewHolder.ivIsLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF
                        , 0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translateAnimation.setDuration(1000);
                view.startAnimation(translateAnimation);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appLockDao.add(mDatas.get(i).getApkPackageName());
                                mDatas.remove(i);
                                UnLockAdapter.this.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });

        return view;
    }

    private static class MyViewHolder {
        public ImageView ivAppIcon;
        public TextView tvAppName;
        public ImageView ivIsLock;
    }
}
