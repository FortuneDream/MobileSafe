package com.example.dell.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Debug;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.TaskInfo;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2016/4/25.
 */
public class TaskInfoProvider {

    private static final String TAG = "TaskInfoProvider";

    //得到手机进程的所有信息,有些进程因为是用其他语言如C写，可能会没有图标，或者名字等
    public static List<TaskInfo> getAllTaskInfos(Context context) {
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        PackageManager pm = context.getPackageManager();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            TaskInfo taskInfo = new TaskInfo();
            String packageName = info.processName;//包名就是进程名
            taskInfo.setPackageName(packageName);
            Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{info.pid})[0];//用debug包
            long memInfoSize = memoryInfo.getTotalPrivateDirty() * 1024;
            taskInfo.setMemInfoSize(memInfoSize);
            try {
                int flag = pm.getPackageInfo(packageName, 0).applicationInfo.flags;
                if ((flag & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    //用户
                    taskInfo.setUser(true);
                } else {
                    //系统
                    taskInfo.setUser(false);
                }
                    Drawable ico = pm.getPackageInfo(packageName, 0).applicationInfo.loadIcon(pm);
                    taskInfo.setIco(ico);
                    String name = pm.getPackageInfo(packageName, 0).applicationInfo.loadLabel(pm).toString();
                    taskInfo.setName(name);
            } catch (Exception e) {
                //如果没有找到
                e.printStackTrace();
                taskInfo.setName(packageName);
                taskInfo.setIco(context.getResources().getDrawable(R.drawable.ico_default));
                //isUser默认是false
            }
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }
}
