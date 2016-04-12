package com.example.dell.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.service.GPSService;

import java.util.Objects;

public class SMSReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    public SMSReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String format=intent.getStringExtra("format");
        Object[] pdus=(Object[])intent.getExtras().get("pdus");//pdus是一条短信的所有短信，因为短信会因为太长而被拆分
        SmsMessage[] messages=new SmsMessage[pdus.length];
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0;i<messages.length;i++){
            messages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
            stringBuilder.append(messages[i].getMessageBody());//通过拼接，得到短信内容
        }
            //得到发送者
            String sender=messages[0].getOriginatingAddress();
            String number=sharedPreferences.getString("number","");
            String body=stringBuilder.toString();
            if (sender.contains(number)){
                if ("#Location#".equals(body)){
                    //得到手机GPS定位
                    Intent gpsService=new Intent(context, GPSService.class);
                    context.startService(intent);//context.
                    String longtitude=sharedPreferences.getString("longtitude","");
                    System.out.println("经度"+longtitude);
                    if (TextUtils.isEmpty(longtitude)){
                        SmsManager.getDefault().sendTextMessage(sender,null,"没有得到地理信息",null,null);
                    }else {
                        SmsManager.getDefault().sendTextMessage(sender,null,longtitude,null,null);
                    }
                    Toast.makeText(context,"得到手机位置",Toast.LENGTH_SHORT).show();
                    System.out.print("得到手机位置");
                    abortBroadcast();
                }else if ("#Alarm#".equals(body)){
                    Toast.makeText(context,"播放报警音乐",Toast.LENGTH_SHORT).show();
                    System.out.print("播放报警音乐");
                    MediaPlayer mediaPlayer=MediaPlayer.create(context, R.raw.maplestory);//create有prepare
                    mediaPlayer.setVolume(1.0f,1.0f);//音量
                    mediaPlayer.start();
                    abortBroadcast();
                }else if ("#WipeData#".equals(body)){
                    Toast.makeText(context,"远程删除数据",Toast.LENGTH_SHORT).show();
                    System.out.print("远程删除数据");
                    abortBroadcast();
                }else if ("#LockScreen#".equals(body)){
                    Toast.makeText(context,"远程锁屏幕",Toast.LENGTH_SHORT).show();
                    System.out.print("远程锁屏幕");
                    abortBroadcast();
                }
            }
        }
}
