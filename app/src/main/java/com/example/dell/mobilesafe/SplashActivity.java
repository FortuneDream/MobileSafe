package com.example.dell.mobilesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.activity.HomeActivity;
import com.example.dell.mobilesafe.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private static final int JSON_ERROR =1;
    private static final int ENTER_HOME = 2;
    private static final int SHOW_UPDATE_DIALOG = 3;
    private static final int URL_ERROR = 4;
    private static final int NETWORK_ERROR = 5;

    private TextView splashVersionTxt;

    //升级描述信息
    private String description;

    //最新apk升级地址
    private String apkurl;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ENTER_HOME:
                    enterHome();
                    break;
                case JSON_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(),"JSON解析异常",Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_UPDATE_DIALOG:
                    Log.e(TAG,"有新版本，弹出对话框");
                    showUpdateDialog();
                    break;
                case URL_ERROR:
                    enterHome();
                    Toast.makeText(SplashActivity.this,"URL异常",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(description);
        builder.setNegativeButton("下次再说", null);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**1.下载apk-Final
                 * 2.替换安装
                 */
            }
        });
        builder.show();//这行代码不能忘记
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashVersionTxt=(TextView)findViewById(R.id.txt_splash_version);
        assert splashVersionTxt != null;
        //设置版本名称
        splashVersionTxt.setText("版本名"+getVersionName());
        //软件的升级
        checkVersion();
    }

    //进入主页面
    private void enterHome() {
        Intent intent=new Intent(this, HomeActivity.class);
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
        new Thread(){
            @Override
            public void run() {
                //优先从消息池中取
                Message msg=Message.obtain();
                //请求网络，得到最新版本信息，这里可以用开源类OKhttp
                URL url= null;
                try {
                    url = new URL(getString(R.string.serverurl));
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(4000);
                    int code=httpURLConnection.getResponseCode();
                    if (code==200){
                        //请求成功，把流转换成String类型
                        String result=StreamTools.readFromStream(httpURLConnection.getInputStream());
                        Log.e(TAG,"result=="+result);
                        //解析JSON
                        try {
                            JSONObject object=new JSONObject(result);
                            String version=(String)object.get("version");
                            description=(String)object.get("decription");
                            apkurl=(String)object.get("apkurl");
                            if (getVersionName().equals(version)){
                                //没有新版本
                                msg.what=ENTER_HOME;
                            }else {
                                msg.what=SHOW_UPDATE_DIALOG;
                                //弹出升级对话框
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.what=JSON_ERROR;
                        }
                    }
                } catch (MalformedURLException e) {
                    //URL错位
                    msg.what=URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网路异常
                    msg.what=NETWORK_ERROR;
                    e.printStackTrace();
                }finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }

    private String getVersionName(){
        //包管理器，得到功能清单文件
        PackageManager pm=getPackageManager();
        try {
            PackageInfo packageInfo=pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }



}
