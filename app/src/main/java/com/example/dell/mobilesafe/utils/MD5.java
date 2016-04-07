package com.example.dell.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Q on 2016/4/6.
 */
public class MD5 {
    public static String encode(String password){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            byte[] bytes=messageDigest.digest(password.getBytes());
            for (byte b:bytes){
                int number=(b&0xff);
                String numberStr=Integer.toHexString(number);
                if (numberStr.length()==1){
                    stringBuilder.append("0");
                }
                stringBuilder.append(numberStr);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
