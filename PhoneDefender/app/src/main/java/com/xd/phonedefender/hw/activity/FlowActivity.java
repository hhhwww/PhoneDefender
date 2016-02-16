package com.xd.phonedefender.hw.activity;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.FlowInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/15.
 */
public class FlowActivity extends AppCompatActivity {
    private ListView lvFlow;
    private List<FlowInfo> mDatasFlowInfo;
    private FlowAdapter flowAdapter;

    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        initViews();
        initDatas();
    }

    private void initViews() {
        lvFlow = (ListView) findViewById(R.id.lv_flow);
        mDatasFlowInfo = new ArrayList<>();

        packageManager = getPackageManager();
    }

    private void initDatas() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示框");
        progressDialog.setMessage("请稍等，正在奋力加载......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
                for (ApplicationInfo a : installedApplications) {
                    FlowInfo flowInfo = new FlowInfo();
                    int uid = a.uid;
                    long uidRxBytes = TrafficStats.getUidRxBytes(uid);
                    long uidTxBytes = TrafficStats.getUidTxBytes(uid);
                    long all = uidRxBytes + uidTxBytes;

                    Drawable appIcon = a.loadIcon(packageManager);
                    String appName = a.loadLabel(packageManager).toString();

                    flowInfo.setAppIcon(appIcon);
                    flowInfo.setAppName(appName);
                    flowInfo.setDownFlow(Formatter.formatFileSize(FlowActivity.this, uidRxBytes));
                    flowInfo.setUpFlow(Formatter.formatFileSize(FlowActivity.this, uidTxBytes));
                    flowInfo.setAllFlow(Formatter.formatFileSize(FlowActivity.this, all));

                    mDatasFlowInfo.add(flowInfo);
                }
                FlowActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        flowAdapter = new FlowAdapter();
                        lvFlow.setAdapter(flowAdapter);
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private class FlowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatasFlowInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatasFlowInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = new MyViewHolder();
            View view = null;
            if (convertView == null) {
                view = View.inflate(FlowActivity.this, R.layout.item_flow, null);
                myViewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_flow);

                myViewHolder.tvDown = (TextView) view.findViewById(R.id.tv_down);
                myViewHolder.tvUp = (TextView) view.findViewById(R.id.tv_up);
                myViewHolder.tvAll = (TextView) view.findViewById(R.id.tv_all);
                myViewHolder.tvName = (TextView) view.findViewById(R.id.tv_app_name_flow);
                view.setTag(myViewHolder);
            } else {
                view = convertView;
                myViewHolder = (MyViewHolder) view.getTag();
            }

            FlowInfo flowInfo = mDatasFlowInfo.get(position);
            myViewHolder.tvName.setText(flowInfo.getAppName());
            myViewHolder.ivIcon.setImageDrawable(flowInfo.getAppIcon());
            myViewHolder.tvDown.setText(flowInfo.getDownFlow());
            myViewHolder.tvUp.setText(flowInfo.getUpFlow());
            myViewHolder.tvAll.setText(flowInfo.getAllFlow());

            return view;
        }

        private class MyViewHolder {
            public ImageView ivIcon;
            public TextView tvName;
            public TextView tvUp;
            public TextView tvDown;
            public TextView tvAll;
        }
    }
}
