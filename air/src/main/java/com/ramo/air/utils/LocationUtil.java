package com.ramo.air.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

import com.ramo.air.listener.HttpCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramo on 2017/1/22.
 */
public class LocationUtil {
    private Context context;
    private Location location;
    private String provider;
    private Handler handler;

    public LocationUtil(Context context,final Handler handler){
        this.context=context;
        this.handler=handler;
    }
    public Location getLocation() {
        return location;
    }

    public void initLocation(LocationManager locationManager, LocationListener locationListener) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }  else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            T.shortShow(context, "没有可用的位置提供器");
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(provider, 5000, 1,
                locationListener);
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // 反向地理位置编码
            reverseGeocoding(location);
        } else {
            T.shortShow(context, "location==null");
        }
    }

    // 把经纬度转为地理位置
    public void reverseGeocoding(Location location) {
        L.e("地理编码..");

        Map<String, Object> map = new HashMap<>();
        map.put("ak", Constants.BAI_DU_GEO_AK);
        map.put("output", "json");
        map.put("location", location.getLatitude()+","+location.getLongitude());
        NetUtils.getHttpUrlConnectionJuHe(Constants.BAI_DU_GEOAPI, map, new HttpCallbackListener() {
            Message msg = new Message();

            @Override
            public void onSucc(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status")
                            .equals("0")) {
                        JSONObject result = jsonObject
                                .getJSONObject("result");

                        msg.obj = result;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    msg.what = 0;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                msg.what = 0;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        });
    }
}
