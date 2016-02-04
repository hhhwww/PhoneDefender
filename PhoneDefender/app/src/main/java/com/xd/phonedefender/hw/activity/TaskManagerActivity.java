package com.xd.phonedefender.hw.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.TaskManagerAdapter;
import com.xd.phonedefender.hw.bean.TaskInfo;
import com.xd.phonedefender.hw.engine.TaskInfosParse;
import com.xd.phonedefender.hw.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class TaskManagerActivity extends AppCompatActivity {

    private TextView tvProcessCount;
    private TextView tvLeftAll;

    private ListView listView;
    private TaskManagerAdapter taskManagerAdapter;
    private List<TaskInfo> taskInfos;
    private List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
    private List<TaskInfo> userDatas;
    private List<TaskInfo> sysDatas;
    private ActivityManager.MemoryInfo memoryInfo;
    private long totalMem;
    private long availMem;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);

        initViews();
        initDatas();
        initListViews();
    }

    private void initViews() {
        tvProcessCount = (TextView) findViewById(R.id.tv_process_count);
        tvLeftAll = (TextView) findViewById(R.id.tv_left_all);
        listView = (ListView) findViewById(R.id.listview_manager);
    }

    private void initDatas() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        runningAppProcesses = activityManager.getRunningAppProcesses();
        size = runningAppProcesses.size();
        tvProcessCount.setText("进程" + runningAppProcesses.size());

        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        availMem = memoryInfo.availMem;
        totalMem = memoryInfo.totalMem;
        String availMem = Formatter.formatFileSize(this, memoryInfo.availMem);
        String totalMem = Formatter.formatFileSize(this, memoryInfo.totalMem);

//另一种获取总内存的方式，可以在低版本上兼容
//        try {
//            FileInputStream fileInputStream = new FileInputStream(
//                    new File("/proc/meminfo")
//            );
//            BufferedReader bufferedReader = new BufferedReader(
//                    new InputStreamReader(fileInputStream)
//            );
//
//            String line = bufferedReader.readLine();
//            StringBuffer stringBuffer = new StringBuffer();
//            for (char c : line.toCharArray()) {
//                if (Character.isDigit(c))
//                    stringBuffer.append(c);
//            }
//
//            Log.e("total", stringBuffer.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        tvLeftAll.setText("剩余/总内存" + availMem + "/" + totalMem);
    }

    private void initListViews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                taskInfos = TaskInfosParse.getTaskInfos(TaskManagerActivity.this);

                //拆分

                userDatas = new ArrayList<TaskInfo>();
                sysDatas = new ArrayList<TaskInfo>();

                for (TaskInfo t : taskInfos) {
                    if (t.isUser())
                        userDatas.add(t);
                    else
                        sysDatas.add(t);
                }


                //end

                taskManagerAdapter = new TaskManagerAdapter(TaskManagerActivity.this
                        , userDatas, sysDatas);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(taskManagerAdapter);
                    }
                });
            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0 || i == userDatas.size() + 1)
                    return;
                else if (i <= userDatas.size()) {
                    if (userDatas.get(i - 1).getPackageName().equals(getPackageName()))
                        return;
                    userDatas.get(i - 1).setIsChecked(!(userDatas.get(i - 1).isChecked()));
                } else {
                    if (sysDatas.get(i - userDatas.size() - 2).getPackageName().equals(getPackageName()))
                        return;
                    sysDatas.get(i - userDatas.size() - 2).setIsChecked(!(sysDatas.get(i - userDatas.size()
                            - 2).isChecked()));
                }
                taskManagerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void selectAll(View view) {
        for (TaskInfo t : taskInfos)
            t.setIsChecked(true);
        taskManagerAdapter.notifyDataSetChanged();
    }

    public void selectNone(View view) {
        for (TaskInfo t : taskInfos) {
            if (t.isChecked())
                t.setIsChecked(false);
            else
                t.setIsChecked(true);
        }
        taskManagerAdapter.notifyDataSetChanged();
    }

    public void killProcess(View view) {
        int count = 0;
        long countMemory = 0;
        List<TaskInfo> flagLists = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//会出现并发错误
//        for (TaskInfo t : taskInfos) {
//            if (t.isChecked()) {
//                activityManager.killBackgroundProcesses(t.getPackageName());
//                count++;
//                countMemory += t.getMemorySize();
//                taskInfos.remove(t);
//            }
//        }

        for (TaskInfo t : userDatas)
            if (t.isChecked())
                flagLists.add(t);

        for (TaskInfo t : sysDatas)
            if (t.isChecked())
                flagLists.add(t);

        count = flagLists.size();
        for (TaskInfo t : flagLists) {
            if (t.isUser())
                userDatas.remove(t);
            else
                sysDatas.remove(t);
            countMemory += t.getMemorySize();
        }
        taskManagerAdapter.notifyDataSetChanged();

        ToastUtil.showMessage("清除了" + count + "个进程," + "共释放了" + Formatter.formatFileSize(this
                , countMemory) + "空间");
        size -= count;
        availMem += countMemory;
        tvProcessCount.setText("进程" + (size));
        tvLeftAll.setText("剩余/总内存" + Formatter.formatFileSize(this, (availMem)) + "/" +
                Formatter.formatFileSize(this, totalMem));
    }

    public void goToSetting(View view) {
        startActivity(new Intent(this, TaskManagerSettingActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (taskManagerAdapter != null)
            taskManagerAdapter.notifyDataSetChanged();
    }
}
