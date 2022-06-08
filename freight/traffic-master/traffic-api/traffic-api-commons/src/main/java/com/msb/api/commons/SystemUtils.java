package com.msb.api.commons;

public class SystemUtils {
    public static boolean isNull(Object object){
        return null==object;
    }
    public static boolean isNullOrEmpty(String str){
        return null==str || str.trim().equals("");
    }
}
