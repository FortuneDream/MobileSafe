package com.example.dell.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Q on 2016/4/15.
 */
public class APPLockDBHelper extends SQLiteOpenHelper {

    //创建数据库，只传入一个context
    public APPLockDBHelper(Context context) {
        super(context, "appLock.db", null, 1);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //已加锁应用
        db.execSQL("create table appLock("
                +"id integer primary key autoincrement,"
                +"packageName varchar(20)"
                +")");

    }
    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
