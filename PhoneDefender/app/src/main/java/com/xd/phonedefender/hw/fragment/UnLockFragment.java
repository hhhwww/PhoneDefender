package com.xd.phonedefender.hw.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.UnLockAdapter;
import com.xd.phonedefender.hw.bean.AppInfo;
import com.xd.phonedefender.hw.db.dao.AppLockDao;
import com.xd.phonedefender.hw.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class UnLockFragment extends Fragment {

    private ListView lvUnlock;
    private List<AppInfo> appInfos;
    private UnLockAdapter unLockAdapter;

    private AppLockDao appLockDao;
    private List<AppInfo> unlockDatas;

    private TextView tvUnlock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_unlock_fragment, null);
        initViews(view);
        return view;
    }

    //每次"进来"都会调用
    @Override
    public void onStart() {
        super.onStart();
        appInfos = AppInfos.getAppInfos(getActivity());

        for (AppInfo a : appInfos) {
            if (!appLockDao.find(a.getApkPackageName()))
                unlockDatas.add(a);
        }

        unLockAdapter = new UnLockAdapter(getActivity(), unlockDatas, appLockDao);

        tvUnlock.setText("未加锁软件" + unlockDatas.size() + "个");

        lvUnlock.setAdapter(unLockAdapter);
    }

    private void initViews(View view) {
        lvUnlock = (ListView) view.findViewById(R.id.lv_unlock);
        tvUnlock = (TextView) view.findViewById(R.id.tv_unlock);

        unlockDatas = new ArrayList<>();
        appLockDao = new AppLockDao(getActivity());
    }
}
