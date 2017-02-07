package com.ramo.air.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ramo on 2016/11/21.
 */
public class T {
    public static  void shortShow(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    public static void longShow(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
