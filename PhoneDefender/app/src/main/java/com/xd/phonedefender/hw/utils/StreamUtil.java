package com.xd.phonedefender.hw.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hhhhwei on 16/1/4.
 */
public class StreamUtil {

    public static String readFromStream(InputStream inputStream) throws IOException {

        String result = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int len = 0;
        byte[] bytes = new byte[1024];

        while((len = inputStream.read(bytes)) != -1){
            byteArrayOutputStream.write(bytes,0,len);
        }

        result = byteArrayOutputStream.toString();

        if(byteArrayOutputStream != null) byteArrayOutputStream.close();

        return result;
    }

}

