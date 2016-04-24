package com.example.dell.mobilesafe.activity;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.SystemInfoUtils;

public class TaskManagerActivity extends AppCompatActivity {
    private TextView runProcessTxt;
    private TextView availRamTxt;
    private ListView listView;
    private LinearLayout llLoading;
    private ActivityManager am;
    private int runningProcessCount;
    private long availRam;
    private long totalRam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        initView();
        runningProcessCount= SystemInfoUtils.getRunningProcessCount(this);
        availRam=SystemInfoUtils.getAvailRam(this);
        totalRam=SystemInfoUtils.getTotalRam(this);
        runProcessTxt.setText("运行中的进程数量:("+runningProcessCount+")");
        availRamTxt.setText("剩余/总内存:"+ Formatter.formatFileSize(this,availRam)+"/"+Formatter.formatFileSize(this,totalRam));

    }

    private void initView() {
        runProcessTxt= (TextView) findViewById(R.id.txt_run_process_count);
        availRamTxt= (TextView) findViewById(R.id.txt_avail_ram);
        llLoading= (LinearLayout) findViewById(R.id.ll_loading);
        listView= (ListView) findViewById(R.id.list_view);
    }

    private class TaskListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
    static  class ViewHolder{

    }
}
