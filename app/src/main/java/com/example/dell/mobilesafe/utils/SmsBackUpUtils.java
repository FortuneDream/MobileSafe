package com.example.dell.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Q on 2016/4/20.
 */
public class SmsBackUpUtils {


    private static final String TAG = "SmsBackUpUtils";

    //短信备份接口
    public interface SmsBackUpCallBack {
        //短信备份前调用，total短信总条数
        void smsBackUpBefore(int total);
        void smsBackUpFinish();
        //备份过程中执行
        void smsBackUpProgress(int progress);
    }

    //短信备份
    public static void smsBackUp(Context context, String path, SmsBackUpCallBack callBack) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        //把所有短信备份
        //XMl序列化器，XML保存短信
        XmlSerializer xmlSerializer = Xml.newSerializer();
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        xmlSerializer.setOutput(fileOutputStream, "UTF-8");//这是参数
        xmlSerializer.startDocument("UTF-8", true);//文档开头，是否独立
        xmlSerializer.startTag(null, "smss");//命名空间
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        callBack.smsBackUpBefore(cursor.getCount());//回调的方法
        int progress = 0;//设置进度
        while (cursor.moveToNext()) {
            xmlSerializer.startTag(null, "sms");

            xmlSerializer.startTag(null, "address");
            String address = cursor.getString(cursor.getColumnIndex("address"));
            xmlSerializer.text(address);
            xmlSerializer.endTag(null, "address");

            xmlSerializer.startTag(null, "date");
            String date = cursor.getString(cursor.getColumnIndex("date"));
            xmlSerializer.text(date);
            xmlSerializer.endTag(null, "date");

            xmlSerializer.startTag(null, "type");
            String type = cursor.getString(cursor.getColumnIndex("type"));
            xmlSerializer.text(type);
            xmlSerializer.endTag(null, "type");

            xmlSerializer.startTag(null, "body");
            String body = cursor.getString(cursor.getColumnIndex("body"));
            xmlSerializer.text(body);
            xmlSerializer.endTag(null, "body");

            xmlSerializer.endTag(null, "sms");

            progress++;
            callBack.smsBackUpProgress(progress);//回调的方法
        }
        callBack.smsBackUpFinish();
        cursor.close();
        xmlSerializer.endTag(null, "smss");
        xmlSerializer.endDocument();
    }
}
