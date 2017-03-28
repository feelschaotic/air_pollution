package com.ramo.air;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.ramo.air.bean.AirQuality;
import com.ramo.air.databinding.ActivityMainLayoutBinding;
import com.ramo.air.fragment.AirRankFragment;
import com.ramo.air.fragment.AllReportFragment;
import com.ramo.air.fragment.MainFragment;
import com.ramo.air.fragment.MyFragmentPagerAdapter;
import com.ramo.air.fragment.SurroundingPollutionFragment;
import com.ramo.air.jsonparsing.AirResultParseBean;
import com.ramo.air.presenter.MainActivityPresenter;
import com.ramo.air.receiver.MessageReceiver;
import com.ramo.air.utils.ActivityResultExtras;
import com.ramo.air.utils.L;
import com.ramo.air.utils.MyPreferenceUtils;
import com.ramo.air.utils.NetWorkStatusUtil;
import com.ramo.air.utils.PreferenceKeyName;
import com.ramo.air.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends FragmentActivity implements MainFragment.LineViewListener {
    ActivityMainLayoutBinding binding;
    private MainActivityPresenter mainActivityPresenter;
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;// 推送标识
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> fragmentList;
    private LocationManager locationManager;
    private String nowCity = null;
    private AirQuality citynow;

    private Handler handlerGeo = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    T.shortShow(MainActivity.this, "定位出错，请稍后重试");
                    break;
                case 1:
                    try {
                        String cityName = ((JSONObject) msg.obj).getJSONObject("addressComponent").getString("city");

                        if (nowCity != null) {
                            MyPreferenceUtils.saveObject(PreferenceKeyName.CITY_NAME, nowCity);
                            binding.currentCityLocalcityname.setText(nowCity);
                        } else {
                            MyPreferenceUtils.saveObject(PreferenceKeyName.CITY_NAME, cityName);
                            binding.currentCityLocalcityname.setText(cityName);
                        }

                        frame1.initData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = (ActivityMainLayoutBinding) DataBindingUtil.setContentView(this, R.layout.activity_main_layout);
        binding.setPresenter(new Presenter());

        AirResultParseBean air = (AirResultParseBean) MyPreferenceUtils.readObject("air_quality");
        if (air != null) {
            citynow = air.getCitynow();
            binding.setAirQuality(citynow);
        }

        Intent intent = getIntent();
        if (intent != null) {
            nowCity = intent.getStringExtra("cityName");
        }

        init();

    }


    public void init() {
        // 初始化分享
        ShareSDK.initSDK(getApplicationContext());
        // 初始化推送
        registerMessageReceiver(); // used for receive msg
        mainActivityPresenter = new MainActivityPresenter(this, binding);
        determineNetworkStatus();
        initView();
    }

    private void determineNetworkStatus() {
        if (NetWorkStatusUtil.isNetworkConnected(this))
            initLocation();
    }

    private void initLocation() {
        mainActivityPresenter.initLocation(handlerGeo, locationManager, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }

    MainFragment frame1;

    private void initView() {

        fragmentList = new ArrayList<>();

        frame1 = new MainFragment();

        frame1.setLineViewListener(this);

        Fragment frame2 = new SurroundingPollutionFragment();
        Fragment frame3 = new AirRankFragment();
        Fragment frame4 = new AllReportFragment();

        fragmentList.add(frame1);
        fragmentList.add(frame2);
        fragmentList.add(frame3);
        fragmentList.add(frame4);

        mFragmentPagerAdapter = new

                MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList

        );
        binding.currentCityViewPager.setAdapter(mFragmentPagerAdapter);

        initEvent();
    }

    private void initEvent() {
        mainActivityPresenter.initEvent();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(mMessageReceiver.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onLineViewTouch() {
        binding.currentCityViewPager.isLineTouched = true;
    }


    public class Presenter {

        public void onHeadClick(View v) {
            startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
        }

        public void onLeftMenuClick(View v) {
            mainActivityPresenter.initLeftMenuEvent(v);
        }

        public void onBottomTagClick(View view) {
            switch (view.getId()) {
                case R.id.main_tab_Linear1:
                    mainActivityPresenter.setSelect(0);
                    break;
                case R.id.main_tab_Linear2:
                    mainActivityPresenter.setSelect(1);
                    break;
                case R.id.main_tab_Linear3:
                    mainActivityPresenter.setSelect(2);
                    break;
                case R.id.main_tab_Linear4:
                    mainActivityPresenter.setSelect(3);
                    break;
                case R.id.main_tab_iv1:
                    mainActivityPresenter.setSelect(0);
                    break;
                case R.id.main_tab_iv2:
                    mainActivityPresenter.setSelect(1);
                    break;
                case R.id.main_tab_iv3:
                    mainActivityPresenter.setSelect(2);
                    break;
                case R.id.main_tab_iv4:
                    mainActivityPresenter.setSelect(3);
                    break;

                default:
                    break;
            }
        }

    }

    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 极光推送初始化
     */
    @Override
    protected void onResume() {
        isForeground = true;
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent in = new Intent();
        switch (resultCode) {
            case RESULT_OK:
                super.onActivityResult(requestCode, resultCode, data);
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                    in.putExtras(extras);
                }
                in.setClass(MainActivity.this,
                        SubmitComplaintsActivity.class);
                startActivity(in);
                break;
            case ActivityResultExtras.CITY_CHANGE:
                String cityName = data.getStringExtra("cityName");
                L.e("on activity result:" + cityName);
                if (cityName != null) {
                    binding.currentCityLocalcityname.setText(cityName);
                    frame1.cityIsChange(cityName);
                    //模拟数据
                    if (citynow != null) {
                        citynow.setAQI("146");
                    }
                }
                break;
            default:
                break;
        }

    }

}
