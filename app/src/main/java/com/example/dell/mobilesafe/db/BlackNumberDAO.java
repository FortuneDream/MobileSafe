package com.example.dell.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Q on 2016/4/15.
 */
//对于数据库的增删改查
    //需要优化（如重复删除，重复加入）
public class BlackNumberDAO {
    private SQLiteDatabase db;
    private BlackNumberDBOpenHelper dbOpenHelper;
    private static BlackNumberDAO blackNumberDAO;
    private BlackNumberDAO(Context context) {
        dbOpenHelper = new BlackNumberDBOpenHelper(context);
        db = dbOpenHelper.getWritableDatabase();
    }
    public synchronized static BlackNumberDAO getDBInstance(Context context){
        if (blackNumberDAO==null){
            blackNumberDAO=new BlackNumberDAO(context);
        }
        return blackNumberDAO;
    }

    //添加
    public void add(String number, String mode) {
        ContentValues values=new ContentValues();
        values.put("number",number);
        values.put("mode",mode);
        db.insert("black_number",null,values);
    }

    public void delete(String number){
        db.delete("black_number","number=?",new String[]{number});

    }

    public void update(String number,String newMode){
        ContentValues values=new ContentValues();
        values.put("mode",newMode);
        db.update("black_number",values,"number=?",new String[]{number});
    }

    //cursor的第一行是属性名字
    public boolean query(String number){
        boolean result=false;
        Cursor cursor=db.query("black_number",null,"number=?",new String[]{number},null,null,null);
        if (cursor.moveToNext()){
            result=true;
        }
        cursor.close();
        return  result;//返回true表示已找到
    }

    public String queryMode(String number){
        String result=null;
        Cursor cursor=db.query("black_number",new String[]{"mode"},"number=?",new String[]{number},null,null,null);
        if (cursor.moveToNext()){
            result=cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    public List<BlackNumberInfo> queryAll(){
        List<BlackNumberInfo> infos= new ArrayList<BlackNumberInfo>();
        Cursor cursor=db.query("black_number",new String[]{"number,mode"},null,null,null,null,null);
        while (cursor.moveToNext()){
            BlackNumberInfo info=new BlackNumberInfo();
            info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            info.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            infos.add(info);
        }//得到所有的黑名单信息
        cursor.close();
        return  infos;
    }

    //部分加载,倒叙查询
    public List<BlackNumberInfo> queryPart(int dex) {
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        Cursor cursor=db.rawQuery("select number,mode from black_number order by id desc limit 20 offset ?",new String[]{String.valueOf(dex)});
        //直接用SQL语句,limit 20 ,每次加载20条， offset index每次从第 index开始加载,并且倒叙，这样最新添加的都可以限制到最前面，
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            info.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            infos.add(info);
        }//得到所有的黑名单信息
        cursor.close();
        return infos;
    }
    public int queryCount(){
        int result=0;
        Cursor cursor=db.rawQuery("select count(*) from black_number",null);
        while(cursor.moveToNext()){
            result=cursor.getInt(0);//得到第0列
        }
        cursor.close();
        return  result;
    }

}
