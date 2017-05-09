package com.nux.dhoan9.firstmvvm.utils;

/**
 * Created by hoang on 27/03/2017.
 */

public class StringUtils {
    public static boolean isEmpty(String string){
        return null == string && "".equals(string);
    }
    public static boolean isBlank(String string){
        return 0 == string.trim().length();
    }
}
