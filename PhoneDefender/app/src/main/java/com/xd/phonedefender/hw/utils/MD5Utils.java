package com.xd.phonedefender.hw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class MD5Utils {
    public static String encouder(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for(byte b : bytes){
                int i = b & 0xff;
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
