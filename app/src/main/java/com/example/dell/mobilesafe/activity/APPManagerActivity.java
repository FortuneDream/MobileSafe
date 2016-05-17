package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.AppInfo;
import com.example.dell.mobilesafe.engine.AppInfoProvider;
import com.example.dell.mobilesafe.utils.DensityUtil;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class APPManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AppManagerActivity";
    private TextView romTxt;
    private TextView sdCardTxt;
    private TextView stateTxt;
    private ListView listView;
    private TextView popLaunchTxt;
    private TextView popDeleteTxt;
    private TextView popShareTxt;
    private LinearLayout llLoading;
    private PopupWindow popupWindow;
    private List<AppInfo> appInfoList;
    private List<AppInfo> systemAppInfoList = new ArrayList<AppInfo>();
    private List<AppInfo> userAppInfoList = new ArrayList<AppInfo>();
    private AppInfo appInfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppManagerAdapter adapter = new AppManagerAdapter();
            listView.setAdapter(adapter);
            llLoading.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        initView();
        setListener();
        romTxt.setText("内存可用：" + getAvailSpace(Environment.getDataDirectory().getAbsolutePath()));//绝对路径
        sdCardTxt.setText("sd卡可用：" + getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath()));
        loadingData();
    }

    private void initView() {
        stateTxt = (TextView) findViewById(R.id.txt_state);
        romTxt = (TextView) findViewById(R.id.txt_rom);
        sdCardTxt = (TextView) findViewById(R.id.txt_sd_card);
        listView = (ListView) findViewById(R.id.list_view);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
    }

    private void setListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if (systemAppInfoList == null || userAppInfoList == null) {
                    return;//一开始就被回调，所以要判空
                }
                if (firstVisibleItem > userAppInfoList.size()) {//第一条看得见的的位置
                    stateTxt.setText("系统程序(" + systemAppInfoList.size() + ")");
                } else {
                    stateTxt.setText("用户程序(" + userAppInfoList.size() + ")");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = listView.getItemAtPosition(position);//通过position得到item，要重写getItem的方法
                if (obj != null) {
                    dismissPopupWindow();
                    View contentView = View.inflate(APPManagerActivity.this, R.layout.item_popup_window, null);
                    appInfo = (AppInfo) obj;//每点击一次都会获得一个新的appInfo，实时更新
                    popLaunchTxt = (TextView) contentView.findViewById(R.id.pop_item_launch);
                    popDeleteTxt = (TextView) contentView.findViewById(R.id.pop_item_delete);
                    popShareTxt = (TextView) contentView.findViewById(R.id.pop_item_share);
                    popLaunchTxt.setOnClickListener(APPManagerActivity.this);//放到外面，也可以用内部类的内部类的方式，但不方便维护
                    popDeleteTxt.setOnClickListener(APPManagerActivity.this);
                    popShareTxt.setOnClickListener(APPManagerActivity.this);
                    LogUtil.d(TAG, "appInfo==" + appInfo.getPackName());
                    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //用ViewGroup的LayoutParams

                    //popupWindow播放动画，必须要加上背景，必须在showAtLocation之前
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int[] location = new int[2];
                    view.getLocationInWindow(location);//得到listView中某条的坐标,按照x，y的顺序存入location数组中

                    //所以把60当做dip，根据不同屏幕，转换成不同的像素
                    int px=DensityUtil.dipToPx(APPManagerActivity.this,60);

                    popupWindow.showAtLocation(parent, Gravity.START + Gravity.TOP, location[0] + px, location[1]);
                    //在代码写的长度单位都是像素，而不是dp


                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);//渐变动画
                    alphaAnimation.setDuration(500);//设置时常
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);//拉伸动画
                    scaleAnimation.setDuration(500);
                    AnimationSet set = new AnimationSet(false);//动画集合
                    set.addAnimation(alphaAnimation);
                    set.addAnimation(scaleAnimation);
                    contentView.startAnimation(set);
                }
            }
        });
    }

    //调出popupWindow
    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;//释放资源
        }
    }

    //得到某个目录的可用空间
    private String getAvailSpace(String path) {
        StatFs fs = new StatFs(path);
        long blocks = fs.getAvailableBlocksLong();//得到多少块可用空间
        long size = fs.getBlockSizeLong();//得到每一块多大
        return Formatter.formatFileSize(this, blocks * size);//n字节转化成MB,GB等
    }

    private void loadingData() {
        llLoading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppAppInfos(APPManagerActivity.this);
                //划分用户和系统应用
                for (AppInfo appInfo : appInfoList) {
                    if (appInfo.isUser()) {
                        userAppInfoList.add(appInfo);
                    } else {
                        systemAppInfoList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    //每次点击都会得到一个新appInfo
    @Override
    public void onClick(View v) {
        dismissPopupWindow();
        switch (v.getId()) {
            case R.id.pop_item_launch:
                LogUtil.d(TAG, "LaunchPopupWindow");
                launch();
                break;
            case R.id.pop_item_delete:
                LogUtil.d(TAG, "DeletePopupWindow");
                uninstall();
                break;
            case R.id.pop_item_share:
                LogUtil.d(TAG, "SharePopupWindow");
                share();
                break;
            default:
                break;
        }
    }

    private void launch() {
        //包管理器
        Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackName());
        startActivity(intent);
    }

    //分享到微博，微信，陌陌,qq空间
    private void share() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐一款app：" + appInfo.getName() + "。下载地址为:" + "....");
        startActivity(intent);
    }

    private void uninstall() {
        if (appInfo.isUser()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + appInfo.getPackName()));
            startActivityForResult(intent, 0);//使得卸载之后刷新页面
        } else {
            Toast.makeText(APPManagerActivity.this, "系统应用需要ROOT权限才能卸载", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        loadingData();//刷新数据
    }

    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return systemAppInfoList.size() + userAppInfoList.size() + 2;//添加两个TextView
        }

        @Override
        public Object getItem(int position) {
            AppInfo appInfo;
            //数据源
            if (position == 0) {
                return null;
            } else if (position == (userAppInfoList.size() + 1)) {
                return null;
            } else if (position <= userAppInfoList.size()) {
                //用户程序
                int newPosition = position - 1;
                appInfo = userAppInfoList.get(newPosition);
            } else {
                int newPosition = position - userAppInfoList.size() - 2;
                appInfo = systemAppInfoList.get(newPosition);
            }
            return appInfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo;
            View view;
            ViewHolder viewHolder;
            //数据源
            if (position == 0) {
                view = View.inflate(APPManagerActivity.this, R.layout.item_txt_list, null);
                TextView textView = (TextView) view.findViewById(R.id.txt_state);
                textView.setText("用户程序(" + userAppInfoList.size() + ")");
                return view;
            } else if (position == (userAppInfoList.size() + 1)) {
                view = View.inflate(APPManagerActivity.this, R.layout.item_txt_list, null);
                TextView textView = (TextView) view.findViewById(R.id.txt_state);
                textView.setText("系统程序(" + systemAppInfoList.size() + ")");
                return view;
            }


            if (convertView != null && convertView instanceof RelativeLayout) {//如果没有判断convert是否为Relative的子类，那么getTag会为空，因为convertView没有setTag
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(APPManagerActivity.this, R.layout.item_app_manager, null);
                viewHolder = new ViewHolder();
                viewHolder.icoImg = (ImageView) view.findViewById(R.id.img_ico);
                viewHolder.nameTxt = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.locationTxt = (TextView) view.findViewById(R.id.txt_location);
                view.setTag(viewHolder);
            }

            appInfo= (AppInfo) getItem(position);
            viewHolder.nameTxt.setText(appInfo.getName());
            viewHolder.icoImg.setImageDrawable(appInfo.getIco());
            if (appInfo.isRom()) {
                viewHolder.locationTxt.setText("手机内部");
            } else {
                viewHolder.locationTxt.setText("外部外部");
            }

            return view;

        }
    }

    static class ViewHolder {
        ImageView icoImg;
        TextView nameTxt;
        TextView locationTxt;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPopupWindow();
    }
}
