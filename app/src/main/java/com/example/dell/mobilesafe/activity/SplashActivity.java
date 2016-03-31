package com.example.dell.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.UpdateInfo;
import com.example.dell.mobilesafe.utils.StreamTools;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 启动页面
 * 1.显示logo-设计logo用动物
 * 2.判断是否合法性
 * 3.是否升级
 * 4.判断合法性
 * 5.校验是否有sdcard
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";//表示在哪个类打印这个日志
    private static final int JSON_ERROR = 1;
    private static final int ENTER_HOME = 2;
    private static final int SHOW_UPDATE_DIALOG = 3;
    private static final int URL_ERROR = 4;
    private static final int NETWORK_ERROR = 5;

    private TextView splashVersionTxt;
    private ProgressBar updateApkPrb;

    private UpdateInfo updateInfo;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_HOME:
                    enterHome();
                    break;
                case SHOW_UPDATE_DIALOG:
                    Log.e(TAG, "有新版本，弹出对话框");
                    showUpdateDialog();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashVersionTxt = (TextView) findViewById(R.id.txt_splash_version);
        updateApkPrb = (ProgressBar) findViewById(R.id.prb_updateapk);
        assert splashVersionTxt != null;
        //设置版本名称
        splashVersionTxt.setText("版本名" + getVersionName());
        //软件的升级
        checkVersion();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(updateInfo.getDecription());
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //首先判断SD是否存在
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FinalHttp finalHttp = new FinalHttp();
                    Log.e("SplashActivity", updateInfo.getApkurl());

                    finalHttp.download(updateInfo.getApkurl(), Environment.getExternalStorageDirectory() + "/mobilesafe2.0.apk", new AjaxCallBack<File>() {
                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            updateApkPrb.setVisibility(View.VISIBLE);
                            int progress = (int) (current / count * 100);
                            updateApkPrb.setProgress(progress);
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(File file) {
                            super.onSuccess(file);
                            Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                            updateApkPrb.setVisibility(View.INVISIBLE);
                            installApk(file);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "sdcard不可用", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();//这行代码不能忘记!
    }

    private void installApk(File t) {
        //系统内部PackageIntaller
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
        startActivity(intent);

    }

    //进入主页面
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //校验新版本，若有则升级
    private void checkVersion() {
        /**升级流程：
         * 手机客户端:请求服务器数据判断，是否有新版本，若无则进入主界面，若有则弹出对话框，让用户选择是否
         *            升级，否则进入主页面，是则下载最新APK，替换，安装，启动
         *服务器：最新版本信息：2.0，最新版本描述信息：手机卫士最新版本，下载地址http://.......
         */
        new Thread() {
            @Override
            public void run() {
                //优先从消息池中取
                Message msg = Message.obtain();
                //请求网络，得到最新版本信息，这里可以用开源类OKhttp
                URL url = null;
                try {
                    OkHttpClient okHttpClient=new OkHttpClient();
                    final Request request=new Request.Builder().url(getString(R.string.serverurl)).build();
                    Call call=okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"获取新版本失败",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            Message msg = Message.obtain();
                                String result=response.body().string();
                            //解析JSON
                            Gson gson = new Gson();
                            updateInfo = gson.fromJson(result, UpdateInfo.class);
                            System.out.println("updataInfo.getApkurl:"+updateInfo.getApkurl());

                            if (getVersionName().equals(updateInfo.getVersion())) {
                                //没有新版本
                                msg.what = ENTER_HOME;
                            } else {
                                msg.what = SHOW_UPDATE_DIALOG;
                                //弹出升级对话框
                            }
                            handler.sendMessage(msg);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }

    private String getVersionName() {
        //包管理器，得到功能清单文件
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
