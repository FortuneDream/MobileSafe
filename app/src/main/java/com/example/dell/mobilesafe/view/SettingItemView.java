package com.example.dell.mobilesafe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;

/**
 * Created by Q on 2016/4/5.
 */
public class SettingItemView extends RelativeLayout {
    private CheckBox checkBox;
    private TextView textView;
    //初始化布局文件
    private void initView(Context context) {
        //inflate：吧布局文件-》View
        //最后一个参数：添加谁进来就是布局文件的父亲，也就是说把布局文件挂载在传进来的参数
       View.inflate(context, R.layout.setting_item_view,this);
        checkBox=(CheckBox)findViewById(R.id.cb_status);
        textView=(TextView)findViewById(R.id.txt_desc);
    }

    //得到组合控件是否勾选
    public boolean isChecked(){
        return checkBox.isChecked();
    }

    //设置组织控件状态
    public void setChecked(boolean isChecked){
        //当checkBox被勾选或者没有被勾选的时候，也表示了组合控件是否被选中
        checkBox.setChecked(isChecked);
    }

    //设置描述信息
    public void setDecription(String decription){
        textView.setText(decription);
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
