package com.ramo.air.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.ramo.air.application.MyApplication;
import com.ramo.air.bean.AirQuality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

public class MyPreferenceUtils {

    /**
     * 保存数据 （多个数据）
     *
     * @param name
     * @param value
     */
    public static void save(String[] name, String[] value) {
        SharedPreferences pre = MyApplication.getContext()
                .getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        Editor ed = pre.edit();
        for (int i = 0; i < name.length && i < value.length && name != null
                && value != null; i++) {
            ed.putString(name[i], value[i]);
        }
        ed.commit();
    }

    /**
     * 获取数据（多参数）
     *
     * @param name
     * @return
     */
    public Map<String, String> getPreferences(String[] name) {
        Map<String, String> map = new HashMap<String, String>();

        SharedPreferences pre = MyApplication.getContext()
                .getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        for (int i = 0; i < name.length && name != null; i++) {
            map.put(name[i], pre.getString(name[i], ""));
        }
        return map;
    }

    /**
     * 获取单数据
     *
     * @param name
     * @return
     */
    public int getAPreference(String name) {

        SharedPreferences pre = MyApplication.getContext()
                .getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        return pre.getInt(name, 0);
    }

    /**
     * 保存Int数据
     *
     * @param name
     * @param value
     */
    public void saveInt(String name, int value) {
        SharedPreferences pre = MyApplication.getContext()
                .getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        Editor ed = pre.edit();
        ed.putInt(name, value);
        ed.commit();
    }

    public void saveAirQuality(AirQuality air) {
        SharedPreferences pre = MyApplication.getContext()
                .getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(air);
            // 将字节流编码成base64的字符窜
            String airQuality_Base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(baos
                    .toByteArray()));
            Editor editor = pre.edit();
            editor.putString("airQuality", airQuality_Base64);

            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated
        }
        Log.i("juhe", "存储成功");
    }

    /**
     * desc:保存对象
     *
     * @param context
     * @param key
     * @param obj     要保存的对象，只能保存实现了serializable的对象 modified:
     */
    public static void saveObject(String key, Object obj) {
        try {
            // 保存对象
            SharedPreferences pre = MyApplication.getContext()
                    .getSharedPreferences(Constants.FILENAME,
                            Context.MODE_PRIVATE);
            Editor ed = pre.edit(); // 先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            // 将对象序列化写入byte缓存
            os.writeObject(obj);
            // 将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            // 保存该16进制数组
            ed.putString(key, bytesToHexString);
            ed.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param context
     * @param key
     * @return modified:
     */
    public static Object readObject(String key) {
        try {
            SharedPreferences pre = MyApplication.getContext()
                    .getSharedPreferences(Constants.FILENAME,
                            Context.MODE_PRIVATE);
            if (pre.contains(key)) {
                String string = pre.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    // 将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(
                            stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    // 返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 所有异常返回null
        return null;

    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @param data
     * @return modified:
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch; // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); // //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16; // // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); // /两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; // // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;// 将转化后的数放入Byte里
        }
        return retData;
    }
}
