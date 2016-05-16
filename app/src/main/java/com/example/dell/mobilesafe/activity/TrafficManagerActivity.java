package com.example.dell.mobilesafe.activity;

import android.net.TrafficStats;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.LogUtil;

/**
 * 流量统计
 */
public class TrafficManagerActivity extends AppCompatActivity {
    private static final String TAG = "TrafficManager";
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_manager);
        initView();
        //统计总流量的接口

        TrafficStats.getMobileRxBytes();//手机（2.2.5.3.4G的流量下载总和）
        TrafficStats.getMobileTxBytes();//上传总和

        TrafficStats.getTotalRxBytes();//手机+wifi下载流量总和
        TrafficStats.getTotalTxBytes();

        //TrafficStats.getUidRxBytes(int);//根据用户id得到下载流量
        //TrafficStats.getUidTxBytes(int);
        setListener();

    }

    private void setListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                LogUtil.d(TAG,"滑动幅度："+slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                LogUtil.d(TAG,"完全打开");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                LogUtil.d(TAG,"完全关闭");
            }

            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE()
             * 2不一定会执行
             *
             */
            @Override
            public void onDrawerStateChanged(int newState) {
                LogUtil.d(TAG,"当前状态："+newState);
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
}
