package com.ramo.air.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.ramo.air.bean.City;
import com.ramo.air.db.CityProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class HotCityUtils {

    /**
     * 获取临时城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getTmpCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(CityProvider.CityConstants.NAME));
            String postID = c
                    .getString(c.getColumnIndex(CityProvider.CityConstants.POST_ID));
            long refreshTime = c.getLong(c
                    .getColumnIndex(CityProvider.CityConstants.REFRESH_TIME));
            int isLocation = c.getInt(c
                    .getColumnIndex(CityProvider.CityConstants.ISLOCATION));
            City item = new City(name, postID, refreshTime, isLocation);
            // L.i("liweiping", "TmpCity  " + item.toString());
            if (!list.contains(item))// 如果不存在再添加
                list.add(item);
        }
        c.close();
        return list;
    }

    /**
     * 获取热门城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getHotCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(CityProvider.CityConstants.NAME));
            String postID = c
                    .getString(c.getColumnIndex(CityProvider.CityConstants.POST_ID));
            int isSelected = c.getInt(c
                    .getColumnIndex(CityProvider.CityConstants.ISSELECTED));
            City item = new City(name, postID, isSelected);
            list.add(item);
        }
        c.close();
        L.e("hot city list:" + list);
        return list;
    }

    /**
     * 获取所有城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getAllCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String province = c.getString(c
                    .getColumnIndex(CityProvider.CityConstants.PROVINCE));
            String city = c.getString(c.getColumnIndex(CityProvider.CityConstants.CITY));
            String name = c.getString(c.getColumnIndex(CityProvider.CityConstants.NAME));
            String pinyin = c.getString(c.getColumnIndex(CityProvider.CityConstants.PINYIN));
            String py = c.getString(c.getColumnIndex(CityProvider.CityConstants.PY));
            String phoneCode = c.getString(c
                    .getColumnIndex(CityProvider.CityConstants.PHONE_CODE));
            String areaCode = c.getString(c
                    .getColumnIndex(CityProvider.CityConstants.AREA_CODE));
            String postID = c
                    .getString(c.getColumnIndex(CityProvider.CityConstants.POST_ID));
            City item = new City(province, city, name, pinyin, py, phoneCode,
                    areaCode, postID);
            list.add(item);
        }
        c.close();
        return list;
    }

    public static String getDBFilePath(Context context) {
        return "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + context.getPackageName() + File.separator
                + "databases" + File.separator + CityProvider.CITY_DB_NAME;
    }

    public static String getDBDirPath(Context context) {
        return "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + context.getPackageName() + File.separator
                + "databases";
    }

    public static void copyDB(Context context) {
        L.i("liweiping", "copyDB begin....");
        // 如果不是第一次运行程序，直接返回
        if (!PreferenceUtils.getPrefBoolean(context, "isFirstRun", true))
            return;
        File dbDir = new File(getDBDirPath(context));
        if (!dbDir.exists())
            dbDir.mkdir();
        try {
            File dbFile = new File(dbDir, CityProvider.CITY_DB_NAME);
            InputStream is = context.getAssets()
                    .open(CityProvider.CITY_DB_NAME);
            FileOutputStream fos = new FileOutputStream(dbFile);
            byte[] buffer = new byte[is.available()];// 本地文件读写可用此方法
            is.read(buffer);
            fos.write(buffer);
            // int len = -1;
            // byte[] buffer = new byte[1024 * 8];
            // while ((len = is.read(buffer)) != -1) {
            // fos.write(buffer, 0, len);
            fos.close();
            is.close();
            L.i("liweiping", "copyDB finish....");
            CityProvider.createTmpCityTable(context);
            PreferenceUtils.setPrefBoolean(context, "isFirstRun", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个自定义风格的Dialog
     *
     * @param activity   上下文对象
     * @param style      风格
     * @param customView 自定义view
     * @return dialog
     */
    public static Dialog getCustomeDialog(Activity activity, int style,
                                          View customView) {
        Dialog dialog = new Dialog(activity, style);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customView);
        Window window = dialog.getWindow();
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        return dialog;
    }

    public static Dialog getCustomeDialog(Activity activity, int style,
                                          int customView) {
        Dialog dialog = new Dialog(activity, style);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customView);
        Window window = dialog.getWindow();
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        return dialog;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 反射方法获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 20;
        try {
            Class<?> _class = Class.forName("com.android.internal.R$dimen");
            Object object = _class.newInstance();
            Field field = _class.getField("status_bar_height");
            int restult = Integer.parseInt(field.get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(
                    restult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getActivity(), "StatusBarHeight = " + statusBarHeight,
        // Toast.LENGTH_SHORT).show();
        return statusBarHeight;
    }
}
