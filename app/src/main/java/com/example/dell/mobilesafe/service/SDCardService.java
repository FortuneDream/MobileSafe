package com.example.dell.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.os.storage.StorageManager;

import com.example.dell.mobilesafe.utils.LogUtil;

import java.lang.reflect.Method;

public class SDCardService extends Service {
    private static final String TAG = "SDCardService";
    private SDCardReceiver sdCardReceiver;
    public SharedPreferences sp;

    public SDCardService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "sdCardService已启动");
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sdCardReceiver = new SDCardReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addDataScheme("file");
        intentFilter.setPriority(1000);
        registerReceiver(sdCardReceiver, intentFilter);
    }

    class SDCardReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences.Editor editor = sp.edit();
            String sdCardPath = null;
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {//SD卡已经成功挂载
                try {
                    StorageManager sm = (StorageManager) getSystemService(STORAGE_SERVICE);
                    Method method = StorageManager.class.getMethod("getVolumePaths");
                    String[] paths = (String[]) method.invoke(sm);
                    sdCardPath = paths[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            editor.putString("sdcard", sdCardPath);
            editor.apply();
            LogUtil.d(TAG, "已存储sd卡路径" + sdCardPath);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sdCardReceiver);
        sdCardReceiver = null;
    }

}


