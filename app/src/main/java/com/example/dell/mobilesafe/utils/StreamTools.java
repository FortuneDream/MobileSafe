package com.example.dell.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Q on 2016/3/29.
 */
public class StreamTools {
    public static String readFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len=inputStream.read(buffer))!=-1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        inputStream.close();
        String result=byteArrayOutputStream.toString();
        byteArrayOutputStream.close();
        return result;
    }
}
