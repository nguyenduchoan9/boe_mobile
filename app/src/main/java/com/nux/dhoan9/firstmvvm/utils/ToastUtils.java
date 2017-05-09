package com.nux.dhoan9.firstmvvm.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hoang on 27/03/2017.
 */

public class ToastUtils {
    public static void toastShortMassage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLongMassage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
