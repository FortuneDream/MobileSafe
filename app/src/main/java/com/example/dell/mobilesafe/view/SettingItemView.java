package com.example.dell.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.dell.mobilesafe.R;

/**
 * Created by Q on 2016/4/5.
 */
public class SettingItemView extends RelativeLayout {

    //初始化布局文件
    private void initView(Context context) {
        //inflate：吧布局文件-》View
        //最后一个参数：添加谁进来就是布局文件的父亲，也就是说把布局文件挂载在传进来的参数
       View.inflate(context, R.layout.setting_item_view,this);
    }


    //设置样式
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //布局文件中实例化
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    //代码中实例化
    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }
}
