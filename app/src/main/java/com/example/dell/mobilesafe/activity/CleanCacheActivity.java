package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CancellationException;

public class CleanCacheActivity extends AppCompatActivity {

    private static final int SCANNING = 0;
    private static final int OVER = 1;
    private static final int APPCATION_INFO = 2;
    private static final String TAG = "CleanCacheActivity";
    private TextView stateTxt;
    private ProgressBar cleanPrb;
    private LinearLayout contentLl;
    private PackageManager pm;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANNING:
                    stateTxt.setText("正在扫描:" + msg.obj);
                    break;
                case OVER:
                    stateTxt.setText("扫描结束");
                    break;
                case APPCATION_INFO:
                    final CacheInfo cacheInfo = (CacheInfo) msg.obj;
                    View view = View.inflate(CleanCacheActivity.this, R.layout.item_app_cache, null);
                    ImageView ico = (ImageView) view.findViewById(R.id.ico_img);
                    TextView name = (TextView) view.findViewById(R.id.name_txt);
                    TextView cache = (TextView) view.findViewById(R.id.cache_txt);
                    ImageView clean= (ImageView) view.findViewById(R.id.clean_img);
                    //动态生成View，并且给View添加点击事件
                    clean.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                        Intent localIntent = new Intent();
                                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if (Build.VERSION.SDK_INT >= 9) {
                                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            localIntent.setData(Uri.fromParts("package", cacheInfo.packName,null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName",cacheInfo.packName);
                                        }
                                        startActivity(localIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    ico.setImageDrawable(cacheInfo.icon);
                    name.setText(cacheInfo.name);
                    cache.setText(Formatter.formatFileSize(CleanCacheActivity.this, cacheInfo.cacheSize));
                    contentLl.addView(view);
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        initView();
        scanningCacheInfo();

    }

    private void scanningCacheInfo() {
        new Thread() {
            @Override
            public void run() {
                pm = getPackageManager();
                List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                cleanPrb.setMax(packageInfos.size());
                int progress = 0;
                for (PackageInfo packageInfo : packageInfos) {
                    SystemClock.sleep(50);
                    String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                    try {
                        Method method = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        method.invoke(pm, appName, new MyIPackageStatsObserver());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Message msg = Message.obtain();
                    msg.obj = appName;
                    msg.what = SCANNING;
                    mHandler.sendMessage(msg);
                    cleanPrb.setProgress(++progress);
                }
                Message msg = Message.obtain();
                msg.what = OVER;
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    private void initView() {
        stateTxt = (TextView) findViewById(R.id.state_txt);
        cleanPrb = (ProgressBar) findViewById(R.id.clean_prb);
        contentLl = (LinearLayout) findViewById(R.id.content_ll);
    }

    public void cleanRightNow(View view) {
        Method[] methods = PackageManager.class.getMethods();
        LogUtil.d(TAG, "清理");
        for (Method method : methods) {
            if ("freeStorageAndNotify".equals(method.getName())) {
                try {
                    method.invoke(pm, Integer.MAX_VALUE, new IPackageDataObserver.Stub() {
                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            LogUtil.d(TAG, String.valueOf(succeeded)+packageName);
                        }


                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded) throws RemoteException {
            if (pStats.cacheSize > 0) {
                CacheInfo cacheInfo = new CacheInfo();
                cacheInfo.cacheSize = pStats.cacheSize;
                cacheInfo.packName = pStats.packageName;
                try {
                    cacheInfo.name = pm.getApplicationInfo(pStats.packageName, 0).loadLabel(pm).toString();
                    cacheInfo.icon = pm.getApplicationInfo(pStats.packageName, 0).loadIcon(pm);
                    Message message = Message.obtain();
                    message.what = APPCATION_INFO;
                    message.obj = cacheInfo;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    class CacheInfo {
        long cacheSize;
        String name;
        String packName;
        Drawable icon;
    }
}
