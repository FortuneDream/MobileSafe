package com.example.dell.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Q on 2016/4/5.
 */
public class FocuseTestView extends TextView {

    /**
     *实现功能，自带焦点
     * @param context 只有这个，用于普通的new
     * @param attrs 加上这个，布局文件使用某个控件，默认会调用带有两个参数的构造方法
     * @param defStyleAttr 再加上这个，样式
     */
    public FocuseTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //java默认的构造器是空的，所以必须要加这个构造器
    public FocuseTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
