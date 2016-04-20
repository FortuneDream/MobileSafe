package com.example.dell.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Q on 2016/4/15.
 */
public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

    //创建数据库，只传入一个context
    public BlackNumberDBOpenHelper(Context context) {
        super(context, "BlackNumber.db", null, 1);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //mode拦截模式:0拦截电话,1短信拦截，2全部拦截
        db.execSQL("create table black_number("
                +"id integer primary key autoincrement,"
                +"number varchar(20),"
                +"mode varchar(10)"
                +")");

    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
