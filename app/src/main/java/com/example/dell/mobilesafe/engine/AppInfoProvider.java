package com.example.dell.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.dell.mobilesafe.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2016/4/21.
 */
public class AppInfoProvider {

    private static final String TAG = "AppInfoProvider";

    public static List<AppInfo> getAppAppInfos(Context context){
        /**
         * 得到手机所有的应用信息
         */
        List<AppInfo> appInfoList=new ArrayList<AppInfo>();
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> packageInfos=pm.getInstalledPackages(0);//得到已经安装的包;
        for (PackageInfo pi:packageInfos){
            AppInfo appInfo=new AppInfo();
            appInfo.setName(pi.applicationInfo.loadLabel(pm).toString());
            appInfo.setIco(pi.applicationInfo.loadIcon(pm));
            appInfo.setPackName(pi.packageName);
            int flag=pi.applicationInfo.flags;//应用程序的表示，答题卡模型
            //与运算->和，11=1,00=0,10=0
            if ((flag&ApplicationInfo.FLAG_SYSTEM)==0){
                //用于用户程序
                appInfo.setUser(true);
            }else {
                //系统程序
                appInfo.setUser(false);
            }

            if ((flag&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
                //内部存储
                appInfo.setRom(true);
            }else {
                //sd卡存储
                appInfo.setRom(false);
            }
            appInfoList.add(appInfo);

        }
        return  appInfoList;

    }
}
