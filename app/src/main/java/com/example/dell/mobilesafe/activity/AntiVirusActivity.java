package com.example.dell.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.List;

public class AntiVirusActivity extends AppCompatActivity {
    private static final String TAG = "AntiVirusActivity";
    private ImageView searchIco;
    private TextView virusStateTxt;
    private ProgressBar statePrb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        initView();
        //当对于自身=0.5f
        new Thread() {
            @Override
            public void run() {
                //得到应用程序的特征码
                PackageManager pm = getPackageManager();
                List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES + PackageManager.GET_SIGNATURES);//包括没有安装的,必须加上signature才可以得到签名信息;
                for (PackageInfo packageInfo : packageInfos) {
                    String name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    String signature = packageInfo.signatures[0].toCharsString();//得到签名信息，16进制转换
                    //软件签名可以校验软件的完整性
                    LogUtil.d(TAG, "signature:" + signature + "  name:" + name);
                    //然后，通过数据库查询对比特征值.........
                }
            }
        }.start();
    }

    private void initView() {
        searchIco = (ImageView) findViewById(R.id.search_ico);
        virusStateTxt = (TextView) findViewById(R.id.virus_state_txt);
        statePrb = (ProgressBar) findViewById(R.id.state_prb);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(800);
        ra.setRepeatCount(Animation.INFINITE);//无限循环
        searchIco.startAnimation(ra);
        statePrb.setMax(100);
    }
}
