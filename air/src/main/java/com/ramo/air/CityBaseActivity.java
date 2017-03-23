package com.ramo.air;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.ramo.air.bean.City;
import com.ramo.air.db.CityProvider;
import com.ramo.air.utils.HotCityUtils;
import com.ramo.air.utils.LocationUtils;
import com.ramo.air.utils.NetUtils;

import java.util.List;


public class CityBaseActivity extends Activity {
    public static final String AUTO_LOCATION_CITY_KEY = "auto_location";
    protected ContentResolver mContentResolver;
    protected Activity mActivity;
    protected LocationUtils mLocationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    private void initDatas() {
        mActivity = this;
        mContentResolver = getContentResolver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected List<City> getTmpCities() {
        Cursor tmpCityCursor = mContentResolver.query(
                CityProvider.TMPCITY_CONTENT_URI, null, null, null, null);
        return HotCityUtils.getTmpCities(tmpCityCursor);
    }

    protected void startLocation(LocationUtils.CityNameStatus cityNameStatus) {
        if (NetUtils.getNetworkState(this) == NetUtils.NETWORN_NONE) {
            Toast.makeText(this, "网络不给力,请稍后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mLocationUtils == null)
            mLocationUtils = new LocationUtils(this, cityNameStatus);
        if (!mLocationUtils.isStarted()) {
            //mLocationUtils.startLocation();// 开始定位
        }
    }

    protected void stopLocation() {
        if (mLocationUtils != null && mLocationUtils.isStarted())
            mLocationUtils.stopLocation();
    }

    protected City getLocationCityFromDB(String name) {
        City city = new City();
        city.setName(name);
        Cursor c = mContentResolver.query(CityProvider.CITY_CONTENT_URI,
                new String[]{CityProvider.CityConstants.POST_ID}, CityProvider.CityConstants.NAME
                        + "=?", new String[]{name}, null);
        if (c != null && c.moveToNext())
            city.setPostID(c.getString(c.getColumnIndex(CityProvider.CityConstants.POST_ID)));
        return city;
    }

    protected void addOrUpdateLocationCity(City city) {
        //先删除已定位城市
        mContentResolver.delete(CityProvider.TMPCITY_CONTENT_URI,
                CityProvider.CityConstants.ISLOCATION + "=?", new String[]{"1"});

        // 存储
        ContentValues tmpContentValues = new ContentValues();
        tmpContentValues.put(CityProvider.CityConstants.NAME, city.getName());
        tmpContentValues.put(CityProvider.CityConstants.POST_ID, city.getPostID());
        tmpContentValues.put(CityProvider.CityConstants.REFRESH_TIME, 0L);// 无刷新时间
        tmpContentValues.put(CityProvider.CityConstants.ISLOCATION, 1);// 手动选择的城市存储为0
        mContentResolver.insert(CityProvider.TMPCITY_CONTENT_URI,
                tmpContentValues);

        // 更新热门城市表已选择
        ContentValues hotContentValues = new ContentValues();
        hotContentValues.put(CityProvider.CityConstants.ISSELECTED, 1);
        mContentResolver.update(CityProvider.HOTCITY_CONTENT_URI,
                hotContentValues, CityProvider.CityConstants.POST_ID + "=?",
                new String[]{city.getPostID()});
    }
}
