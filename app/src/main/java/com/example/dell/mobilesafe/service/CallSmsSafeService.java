package com.example.dell.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.example.dell.mobilesafe.db.BlackNumberDAO;


public class CallSmsSafeService extends Service {
    private InnerSMSReceiver smsReceiver;
    private BlackNumberDAO blackNumberDAO;
    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    //监听拦截短信
    public CallSmsSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumberDAO=BlackNumberDAO.getDBInstance(this);
        listener=new MyPhoneStateListener();
        smsReceiver=new InnerSMSReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(smsReceiver,filter);

        //监听来电
        tm= (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends  PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING://电话打进来
                    //电话挂断
//                    if (blackNumberDAO.query(incomingNumber)){
//                        String mode= blackNumberDAO.queryMode(incomingNumber);
//                        if ("0".equals(mode)||"2".equals(mode)){
//                            //调用系统隐藏API
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
        smsReceiver=null;
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        listener=null;
    }

    private class InnerSMSReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
        Object[] pdus= (Object[]) intent.getExtras().get("pdus");
            for (Object pdu:pdus){
                SmsMessage sms=SmsMessage.createFromPdu((byte[]) pdu);
                String sender=sms.getOriginatingAddress();
                if (blackNumberDAO.query(sender)){
                    String mode=blackNumberDAO.queryMode(sender);
                    if ("1".equals(mode)||"2".equals(mode)){
                        System.out.println("拦截到一条短信");
                        abortBroadcast();
                    }
                }
                String body=sms.getMessageBody();
                //按照内容拦截
                if (body.contains("发票")){
                    System.out.println("拦截到一条广告短信");
                    abortBroadcast();
                }
            }
        }
    }

    //挂断电话，未完成
//    public void endCall(){
////        IBinder b=ServiceManager.getService(TELEPHONY_SERVICE);
//        //用反射得到ServiceManager的实例
//        /**
//         * 1.得到字节码
//         * 2.得到对应的方法getService
//         * 3.得到实例
//         * 4.执行这个方法
//         * 以上四步就是反射的过程
//         * 5.拷贝.aidl文件
//         * 6.生成java代码
//         * 7.执行java中的endCall();
//         */
//        try {
//            Class clazz=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
//            Method method=clazz.getMethod("getService",String.class);
//            IBinder iBinder= (IBinder) method.invoke(null,TELEPHONY_SERVICE);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
