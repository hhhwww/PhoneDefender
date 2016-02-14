package com.xd.phonedefender.hw.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.AppInfo;
import com.xd.phonedefender.hw.db.dao.AppLockDao;
import com.xd.phonedefender.hw.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class LockFragment extends Fragment {
    private ListView lvLock;
    private List<AppInfo> mDatas;
    private LockFragmentAdapter lockFragmentAdapter;

    private AppLockDao appLockDao;

    private TextView tvLock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<AppInfo> appInfos = AppInfos.getAppInfos(getActivity());

        for (AppInfo a : appInfos) {
            if (appLockDao.find(a.getApkPackageName()))
                mDatas.add(a);
        }

        lvLock.setAdapter(lockFragmentAdapter);
    }

    private void initViews(View view) {
        lvLock = (ListView) view.findViewById(R.id.lv_lock);
        mDatas = new ArrayList<>();

        appLockDao = new AppLockDao(getActivity());
        lockFragmentAdapter = new LockFragmentAdapter();

        tvLock = (TextView) view.findViewById(R.id.tv_lock);
    }

    private class LockFragmentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            tvLock.setText("已加锁软件" + mDatas.size() + "个");
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

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            MyViewHolder myViewHolder = new MyViewHolder();
            final View view;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.item_lock, null);
                myViewHolder.ivAppIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
                myViewHolder.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
                myViewHolder.ivIsLock = (ImageView) view.findViewById(R.id.iv_lock);
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
                            , 0f, Animation.RELATIVE_TO_SELF, -1f
                            , Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                    translateAnimation.setDuration(1000);
                    view.startAnimation(translateAnimation);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            LockFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    appLockDao.delete(mDatas.get(i).getApkPackageName());
                                    mDatas.remove(i);
                                    lockFragmentAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }
            });
            return view;
        }
    }

    private static class MyViewHolder {
        public ImageView ivAppIcon;
        public TextView tvAppName;
        public ImageView ivIsLock;
    }
}
