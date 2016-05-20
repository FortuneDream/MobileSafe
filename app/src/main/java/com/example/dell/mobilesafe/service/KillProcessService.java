package com.example.dell.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.dell.mobilesafe.bean.TaskInfo;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {
    private static final String TAG = "KillProcessService";
    private ScreenReceiver screenReceiver;
    private Timer timer;
    private TimerTask timerTask;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenReceiver = new ScreenReceiver();
        timer=new Timer();//定时器
        timerTask=new TimerTask() {
            @Override
            public void run() {
                LogUtil.d(TAG,"定时器开始干活");
                //只要服务还在就会一直执行定时器
            }
        };
        timer.schedule(timerTask,2000,4000);//参数二，多久开始执行run，参数三，循环间隔
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);

    }//监听锁屏事件

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);//取消监听
        screenReceiver=null;
        timer.cancel();
        timerTask.cancel();
        timer=null;
        timerTask=null;//取消定时器
    }//取消监听锁屏

    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //杀死后台进程

            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
                am.killBackgroundProcesses(processInfo.processName);
            }
        }
    }
}
