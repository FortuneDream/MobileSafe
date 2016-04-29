package com.example.dell.mobilesafe.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.TaskInfo;
import com.example.dell.mobilesafe.engine.TaskInfoProvider;
import com.example.dell.mobilesafe.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
    private static final String TAG = "TaskManagerActivity";
    private TextView runProcessTxt;
    private TextView availRamTxt;
    private TextView stateTxt;
    private ListView listView;
    private LinearLayout llLoading;
    private ActivityManager am;
    private SharedPreferences sharedPreferences;
    private int runningProcessCount;
    private long availRam;
    private long totalRam;
    private List<TaskInfo> taskInfoList;
    private List<TaskInfo> userInfoList;
    private List<TaskInfo> systemInfoList;
    private TaskListAdapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.INVISIBLE);
            adapter = new TaskListAdapter();
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        initView();
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
        availRam = SystemInfoUtils.getAvailRam(this);
        totalRam = SystemInfoUtils.getTotalRam(this);
        runProcessTxt.setText("运行中的进程数量:(" + runningProcessCount + ")");
        availRamTxt.setText("剩余/总内存:" + Formatter.formatFileSize(this, availRam) + "/" + Formatter.formatFileSize(this, totalRam));
        setListener();
        getAllTaskData();
    }

    private void setListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userInfoList == null || systemInfoList == null) {
                    return;
                }
                if (firstVisibleItem <= userInfoList.size()) {
                    stateTxt.setText("用户进程(" + userInfoList.size() + ")");
                } else if (firstVisibleItem > userInfoList.size()) {

                    stateTxt.setText("系统进程(" + systemInfoList.size() + ")");
                }
            }
        });

        //设置一整条的点击事件,取消掉checkbox的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = listView.getItemAtPosition(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_selected);
                if (object != null) {
                    TaskInfo taskInfo = (TaskInfo) object;
                    checkBox.setChecked(!taskInfo.isChecked());
                    taskInfo.setChecked(!taskInfo.isChecked());
                }
            }
        });
    }

    private void getAllTaskData() {
        llLoading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                userInfoList = new ArrayList<TaskInfo>();
                systemInfoList = new ArrayList<TaskInfo>();
                taskInfoList = TaskInfoProvider.getAllTaskInfos(TaskManagerActivity.this);
                for (TaskInfo info : taskInfoList) {
                    if (info.isUser()) {
                        userInfoList.add(info);
                    } else {
                        systemInfoList.add(info);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initView() {
        stateTxt = (TextView) findViewById(R.id.txt_state);
        runProcessTxt = (TextView) findViewById(R.id.txt_run_process_count);
        availRamTxt = (TextView) findViewById(R.id.txt_avail_ram);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        listView = (ListView) findViewById(R.id.list_view);
    }

    private class TaskListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            boolean isHideSystem = sharedPreferences.getBoolean("hide_system", false);
            if (isHideSystem) {
                return userInfoList.size() + 1;
            } else {
                return userInfoList.size() + systemInfoList.size() + 2;
            }
        }

        //在这里动态设置数据源
        @Override
        public Object getItem(int position) {
            TaskInfo taskInfo;
            if (position == 0) {
                return null;
            } else if (position == (userInfoList.size() + 1)) {
                return null;
            } else if (position < (userInfoList.size() + 1)) {
                int newPosition = position - 1;
                taskInfo = userInfoList.get(newPosition);
            } else {
                int newPosition = position - userInfoList.size() - 2;
                taskInfo = systemInfoList.get(newPosition);
            }
            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (position == 0) {
                view = View.inflate(TaskManagerActivity.this, R.layout.item_txt_list, null);
                TextView textView = (TextView) view.findViewById(R.id.txt_state);
                textView.setText("用户进程(" + userInfoList.size() + ")");
                return view;
            }
            if (position == userInfoList.size() + 1) {
                view = View.inflate(TaskManagerActivity.this, R.layout.item_txt_list, null);
                TextView textView = (TextView) view.findViewById(R.id.txt_state);
                textView.setText("系统进程(" + systemInfoList.size() + ")");
                return view;
            }


            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
                viewHolder = new ViewHolder();
                viewHolder.ico = (ImageView) view.findViewById(R.id.img_ico);
                viewHolder.memInfoSizeTxt = (TextView) view.findViewById(R.id.txt_process_size);
                viewHolder.nameTxt = (TextView) view.findViewById(R.id.txt_run_process_name);
                viewHolder.isUserCb = (CheckBox) view.findViewById(R.id.cb_selected);
                view.setTag(viewHolder);
            }

            TaskInfo taskInfo = (TaskInfo) getItem(position);

            //用实体类检验checkbox的状态
            if (taskInfo.isChecked()) {
                viewHolder.isUserCb.setChecked(true);
            } else {
                viewHolder.isUserCb.setChecked(false);
            }
            viewHolder.ico.setImageDrawable(taskInfo.getIco());//用setImageDrawable
            viewHolder.nameTxt.setText(taskInfo.getName());
            viewHolder.memInfoSizeTxt.setText(Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemInfoSize()));

            return view;
        }
    }

    private static class ViewHolder {
        ImageView ico;
        TextView nameTxt;
        TextView memInfoSizeTxt;
        CheckBox isUserCb;
    }

    public void selectAll(View view) {
        for (TaskInfo taskInfo : taskInfoList) {
            taskInfo.setChecked(true);
        }
        adapter.notifyDataSetChanged();//getCount()->getView();
    }

    public void unSelect(View view) {
        for (TaskInfo taskInfo : taskInfoList) {
            taskInfo.setChecked(!taskInfo.isChecked());
        }
        adapter.notifyDataSetChanged();//getCount()->getView();
    }

    public void killAll(View view) {
        for (TaskInfo taskInfo : taskInfoList) {
//            LogUtil.e(TAG, "TaskManagerActivity---killAll:  " + taskInfo.getPackageName());
            if (taskInfo.isChecked()) {
                am.killBackgroundProcesses(taskInfo.getPackageName());//杀死后台进程
            }
        }
        getAllTaskData();
        Toast.makeText(TaskManagerActivity.this, "已经清理完毕", Toast.LENGTH_SHORT).show();
    }

    public void reEnterSetting(View view) {
        Intent intent = new Intent(TaskManagerActivity.this, TaskManagerSettingActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//设置返回刷新数据
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();//getCount->getView()
    }
}
