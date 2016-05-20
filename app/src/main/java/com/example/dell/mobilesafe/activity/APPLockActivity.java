package com.example.dell.mobilesafe.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.AppInfo;
import com.example.dell.mobilesafe.db.APPLockDAO;
import com.example.dell.mobilesafe.engine.AppInfoProvider;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class APPLockActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "APPLockActivity ";
    private TextView lockTxt;
    private TextView unLockTxt;
    private LinearLayout unLockLL;
    private TextView lockNumberTxt;
    private TextView unLockNumberTxt;
    private List<AppInfo> appInfoList;
    private List<AppInfo> unLockAppList;
    private List<AppInfo> lockAppList;
    private ListView unLockLv;
    private ListView lockLv;
    private APPLockDAO appLockDAO;
    private ListAdapter unLockAdapter;
    private ListAdapter lockAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);

        initView();
        LogUtil.d(TAG, "initView后");
        setListener();
        initDate();
    }

    private void initDate() {
        appInfoList = AppInfoProvider.getAppAppInfos(APPLockActivity.this);
        appLockDAO = APPLockDAO.getDBInstance(this);
        unLockAppList = new ArrayList<>();
        lockAppList = new ArrayList<>();
        for (AppInfo appInfo : appInfoList) {
            //如果在加锁库里面中查到的所有信息app中有某个包名;
            if (appLockDAO.query(appInfo.getPackName())) {
                lockAppList.add(appInfo);
            } else {
                unLockAppList.add(appInfo);
            }
    }
        unLockAdapter = new ListAdapter(true);
        unLockLv.setAdapter(unLockAdapter);
        lockAdapter = new ListAdapter(false);
        lockLv.setAdapter(lockAdapter);
    }

    private void setListener() {
        unLockTxt.setOnClickListener(this);
        lockTxt.setOnClickListener(this);
    }

    private void initView() {
        lockTxt = (TextView) findViewById(R.id.txt_lock);
        unLockTxt = (TextView) findViewById(R.id.txt_unlock);
        lockNumberTxt = (TextView) findViewById(R.id.txt_lock_number);
        unLockNumberTxt = (TextView) findViewById(R.id.txt_unlock_number);
        unLockLL = (LinearLayout) findViewById(R.id.ll_unlock);
        unLockLv = (ListView) findViewById(R.id.unlock_list);
        lockLv = (ListView) findViewById(R.id.lock_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_unlock:
                unLockLL.setVisibility(View.VISIBLE);//如果已经被gone，那么此时会重新创建
                unLockTxt.setBackgroundResource(R.drawable.frame_on);
                lockTxt.setBackgroundResource(R.drawable.frame_off);
                break;
            case R.id.txt_lock:
                unLockLL.setVisibility(View.GONE);
                unLockTxt.setBackgroundResource(R.drawable.frame_off);
                lockTxt.setBackgroundResource(R.drawable.frame_on);
                break;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private boolean isFlag = true;//true=未加锁

        public ListAdapter(boolean isFlag) {
            this.isFlag = isFlag;
        }

        @Override
        public int getCount() {
            if (isFlag) {
                unLockNumberTxt.setText("未加锁软件：" + unLockAppList.size());
                return unLockAppList.size();
            } else {
                lockNumberTxt.setText("已加锁软件：" + lockAppList.size());
                return lockAppList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view;
            ViewHolder viewHolder;
            if (convertView != null) {//如果没有判断convert是否为Relative的子类，那么getTag会为空，因为convertView没有setTag
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(APPLockActivity.this, R.layout.item_app_lock, null);
                viewHolder = new ViewHolder();
                viewHolder.icoImg = (ImageView) view.findViewById(R.id.img_ico);
                viewHolder.nameTxt = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.statusImg = (ImageView) view.findViewById(R.id.img_status);
                view.setTag(viewHolder);
            }
            final AppInfo appInfo;
            if (isFlag) {
                viewHolder.statusImg.setBackgroundResource(R.drawable.ico_addlock);
                appInfo = unLockAppList.get(position);
            } else {
                appInfo = lockAppList.get(position);
                viewHolder.statusImg.setImageResource(R.drawable.ico_release_lock);
            }
            viewHolder.nameTxt.setText(appInfo.getName());
            viewHolder.icoImg.setImageDrawable(appInfo.getIco());
            viewHolder.statusImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFlag) {
                        //位移动画,不能直接设置，因为notifyDataSetChange会让当前的引用下移一个位置，所以动画显示错误,需用延迟的方式
                        //等动画播放完毕后再执行逻辑操作
                        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        translateAnimation.setDuration(300);
                        view.startAnimation(translateAnimation);
                        //此处的handler在哪个线程，run方法就在哪个线程执行
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //1.添加到已加锁数据库
                                appLockDAO.add(appInfo.getPackName());
                                //2.添加到已加锁当前列表
                                lockAppList.add(appInfo);
                                //3.未枷锁列表要把这个应用移除
                                unLockAppList.remove(appInfo);
                                //4.两边同时刷新页面
                                unLockAdapter.notifyDataSetChanged();
                                lockAdapter.notifyDataSetChanged();
                            }
                        }, 300);
                    } else {
                        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        translateAnimation.setDuration(300);
                        view.startAnimation(translateAnimation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //1.移除数据库
                                //2.添加未加锁列表
                                //3.已加锁列表移除
                                appLockDAO.delete(appInfo.getPackName());
                                unLockAppList.add(appInfo);
                                lockAppList.remove(appInfo);
                                //4.两边同时刷新页面
                                unLockAdapter.notifyDataSetChanged();
                                lockAdapter.notifyDataSetChanged();
                            }
                        }, 300);
                    }

                }
            });
            return view;
        }
    }

    static class ViewHolder {
        ImageView icoImg;
        TextView nameTxt;
        ImageView statusImg;
    }


}
