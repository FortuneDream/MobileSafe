package com.example.dell.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2016/4/15.
 */
//对于数据库的增删改查
//需要优化（如重复删除，重复加入）
public class APPLockDAO {
    private SQLiteDatabase db;
    private APPLockDBHelper dbOpenHelper;
    private static APPLockDAO appLockDAO;

    private APPLockDAO(Context context) {
        dbOpenHelper = new APPLockDBHelper(context);
        db = dbOpenHelper.getWritableDatabase();
    }

    public synchronized static APPLockDAO getDBInstance(Context context) {
        if (appLockDAO == null) {
            appLockDAO = new APPLockDAO(context);
        }
        return appLockDAO;
    }

    //添加
    public void add(String packageName) {
        ContentValues values = new ContentValues();
        values.put("packageName", packageName);
        db.insert("appLock", null, values);
    }

    public void delete(String packageName) {
        db.delete("appLock", "packageName=?", new String[]{packageName});
    }


    //是否加锁
    public boolean query(String packageName) {
        boolean result = false;
        Cursor cursor = db.query("appLock", null, "packageName=?", new String[]{packageName}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        return result;//返回true表示已找到
    }


    //得到所有的已加锁应用的包名
    public List<String> queryAll() {
        List<String> infos = new ArrayList<>();
        Cursor cursor = db.query("appLock", new String[]{"packageName"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndex("packageName"));
            infos.add(packageName);
        }//得到所有的app信息
        cursor.close();
        return infos;
    }

}
