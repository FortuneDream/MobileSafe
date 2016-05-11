package com.example.dell.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.dell.mobilesafe.activity.EnterAppActivity;
import com.example.dell.mobilesafe.db.APPLockDAO;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.List;

public class WatchDogService extends Service {
    private ActivityManager am;
    private String TAG = "WatchDogService";
    private boolean flag=false;
    private APPLockDAO appLockDAO;
    private String stopProtecttingPackName;
    private InnerReceiver receiver;
    private List<String> packNames;//所有加锁的应用的报名
    private MyContentObserver myContentObserver;
    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        flag=true;
        appLockDAO=APPLockDAO.getDBInstance(this);
        final Intent intent=new Intent(WatchDogService.this, EnterAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        receiver=new InnerReceiver();
        packNames=appLockDAO.queryAll();//在内存中查找,提速，但是如果新添加，就无法监听。所以用内容观察者
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.dell.mobilesafe.stopprotect");
        registerReceiver(receiver,intentFilter);
        new Thread() {
            @Override
            public void run() {
                //监听最近打开的应用的包名
                while (flag) {
                    //am.getRunningTasks(1):得到任务栈列表，只装一个；get（0）这个唯一的栈取出来（栈=进程）
                    ActivityManager.RunningTaskInfo taskInfo = am.getRunningTasks(1).get(0);
                    String packName = taskInfo.topActivity.getPackageName();//得到最上那个Activity的packname
                    LogUtil.d(TAG, "packName:"+packName);
                    if (packNames.contains(packName)){
                        if (packName.equals(stopProtecttingPackName)){
                            //什么也不做
                        }else {
                            intent.putExtra("packName",packName);
                            startActivity(intent);
                        }

                    }
                    SystemClock.sleep(100);
                }

            }
        }.start();
        myContentObserver=new MyContentObserver(new Handler());//要取消注册
        getContentResolver().registerContentObserver(Uri.parse("content://com.example.dell.dbchange"),true,myContentObserver);//内容观察者监听内容变化
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        flag=false;//关闭子线程
        unregisterReceiver(receiver);//取消注册看门狗
        receiver=null;
        //取消注册内容观察者
        getContentResolver().unregisterContentObserver(myContentObserver);
        myContentObserver=null;
    }

    private class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            stopProtecttingPackName=intent.getStringExtra("packName");

        }
    }
    private  class MyContentObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            LogUtil.d(TAG,"数据发生变化了");
            packNames=appLockDAO.queryAll();//在内存中查找,提速，但是如果新添加，就无法监听。所以用内容观察者
        }
    }
}
