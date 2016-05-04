package com.example.dell.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Q on 2016/4/24.
 */
public class SystemInfoUtils {
    //得到当前手机运行进程的总数
    public static  int getRunningProcessCount(Context context){
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }
    //得到可用内存
    public static long getAvailRam(Context context){
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    //得到总内存
    public static long getTotalRam(Context context){
        //4.1以上可用
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
        }
    }

