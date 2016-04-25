package com.example.dell.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Q on 2016/4/7.
 */
//手机防盗页面的基类抽象类
public abstract class BaseSetupActivity  extends AppCompatActivity {
    private GestureDetector detector;//定义手势识别器，此处用于Activity滑动
    protected SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //屏蔽过慢的滑动
                if (Math.abs(velocityX)<100){
                    return true;
                }
                //屏蔽Y轴上的滑动
                if(Math.abs(e2.getY()-e1.getY())>100){
                    return true;
                }
                //velocityX，Y，XY轴的速度,单位为像素/S，从点e1滑到点e2
                if(e2.getX()-e1.getX()>50){
                    //显示上一个页面
                    showPre();
                    return  true;//要return true
                }
                if (e1.getX()-e2.getX()>50){
                    //显示下一个页面
                    showNext();
                    return  true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showNext();//变成抽象方法，让子类实现,聚体从哪个页面跳转到哪个页面
    protected abstract void showPre();


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);//手势识别器处理事件
        return super.onTouchEvent(event);
    }

    public void next(View view){
        showNext();//所有的Activity都有一个按钮是设置了next方法，点击之后，会调用子类的实现方法，所以，子类只需要实现一个showNext方法，就可以让鼠标点击和手势滑动同时起作用
    }
    public void pre(View view){
        showPre();
    }
}
