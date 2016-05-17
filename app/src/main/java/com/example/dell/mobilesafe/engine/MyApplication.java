package com.example.dell.mobilesafe.engine;

import android.app.Application;
import android.os.Environment;

import com.example.dell.mobilesafe.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Q on 2016/5/17.
 */
//Manifest里面设置ApplicationName
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    //这是程序的入口，比任何组件被调用都要前
    @Override
    public void onCreate() {
        super.onCreate();
        //异常捕获器
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                LogUtil.d(TAG,"捕获一个异常");
                File file=new File(Environment.getExternalStorageDirectory(),"cash.log");
                try {
                    PrintWriter pw=new PrintWriter(file);
                    ex.printStackTrace(pw);
                    //还可以添加手机型号，android版本，当前网络，当前运行的软件（可能因为其他软件的），当前时间,软件的版本
                    pw.flush();
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //处理异常后会卡死，所以要关闭
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    //当系统资源耗尽的时候，程序被系统关闭，少见
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
