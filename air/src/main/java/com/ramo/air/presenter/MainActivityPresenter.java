package com.ramo.air.presenter;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ramo.air.AirPollutionMapActivity;
import com.ramo.air.BlowActivity;
import com.ramo.air.CityManagerActivity;
import com.ramo.air.EPInformationActivity;
import com.ramo.air.MyAttentionReportActivity;
import com.ramo.air.MySubmitComplaintsActivity;
import com.ramo.air.OtherSetActivity;
import com.ramo.air.R;
import com.ramo.air.ScanBeforeActivity;
import com.ramo.air.SubmitMapActivity;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.databinding.ActivityMainLayoutBinding;
import com.ramo.air.fragment.AirRankFragment;
import com.ramo.air.uicontrols.TopBar;
import com.ramo.air.utils.ActivityResultExtras;
import com.ramo.air.utils.LocationUtil;

import java.io.File;

import cn.sharesdk.sina.weibo.SinaWeibo;
import onekeyshare.OnekeyShare;
import onekeyshare.Shake2Share;

/**
 * Created by ramo on 2016/11/22.
 */
public class MainActivityPresenter {

    ActivityMainLayoutBinding binding;
    private Activity context;
    private AirQuality air_composition;


    public MainActivityPresenter(Activity context, ActivityMainLayoutBinding binding) {
        this.binding = binding;
        this.context = context;
        air_composition = new AirQuality();
        air_composition.setAQI("98");
        air_composition.setQuality("良");
    }

    private void setTab(int i) {
        resetImgs();
        // 设置图片为亮色
        // 切换内容区域
        switch (i) {
            case 0:
                binding.topBar.setRightBG(R.drawable.btn_main_top_share_bg);
                binding.topBar.setLeftBG(R.drawable.btn_show_city_bg);

                binding.activityMainBottomLabel.mainTabIv1.setImageResource(R.drawable.ic_main_tab_air_pressed);
                break;
            case 1:
                binding.topBar.setLeftBG(R.drawable.iv_mymap);
                binding.topBar.setRightBG(R.drawable.btn_main_top_share_bg);
                binding.activityMainBottomLabel.mainTabIv2
                        .setImageResource(R.drawable.ic_main_tab_aqi_bg_selected);
                break;
            case 2:
                binding.topBar.setLeftBG(R.drawable.iv_mymap);
                binding.topBar.setRightBG(R.drawable.btn_main_top_share_bg);
                binding.activityMainBottomLabel.mainTabIv3
                        .setImageResource(R.drawable.ic_main_tab_aqi_rank_bg_selected);
                break;
            case 3:

                binding.topBar.setRightBG(R.drawable.tucao_fragment_camera_bg);
                binding.topBar.setLeftBG(R.drawable.iv_mymap);
                binding.activityMainBottomLabel.mainTabIv4
                        .setImageResource(R.drawable.ic_main_tab_tucao_bg_selected);

                break;
        }
    }

    private void resetImgs() {
        binding.activityMainBottomLabel.mainTabIv1.setImageResource(R.drawable.ic_main_tab_air);
        binding.activityMainBottomLabel.mainTabIv2.setImageResource(R.drawable.ic_main_tab_aqi_bg_normal);
        binding.activityMainBottomLabel.mainTabIv3.setImageResource(R.drawable.ic_main_tab_aqi_rank_bg_normal);
        binding.activityMainBottomLabel.mainTabIv4.setImageResource(R.drawable.ic_main_tab_tucao_bg_normal);
    }

    public void initEvent() {
        binding.currentCityViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                int currentItem = binding.currentCityViewPager.getCurrentItem();
                setTab(currentItem);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        setTab(0);
        onListener();
    }

    public void setSelect(int i) {
        setTab(i);
        binding.currentCityViewPager.setCurrentItem(i);
    }

    private void onListener() {

        binding.topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {

            public void leftClick() {
                Intent intent = null;
                if (binding.currentCityViewPager.getCurrentItem() == 0) {
                    intent = new Intent(new Intent(context,
                            CityManagerActivity.class));
                } else if (binding.currentCityViewPager.getCurrentItem() == 2) {
                    intent = new Intent(context,
                            AirPollutionMapActivity.class);
                } else {
                    intent = new Intent(context,
                            SubmitMapActivity.class);
                }
                if (binding.currentCityViewPager.getCurrentItem() == 0)
                    context.startActivityForResult(intent, ActivityResultExtras.CITY_CHANGE);
                else
                    context.startActivity(intent);
            }

            public void RightClick() {
                if (binding.currentCityViewPager.getCurrentItem() == 3) {
                    takePhoto();
                } else if (binding.currentCityViewPager.getCurrentItem() == 1) {
                    shareMyCityMoniData();
                } else if (binding.currentCityViewPager.getCurrentItem() == 2) {
                    shareMyCityAirRank();
                } else {
                    shareMyCityAirQuality();
                }
            }
        });
        binding.currentCityLocalcityname.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                context.startActivity(new Intent(context,
                        CityManagerActivity.class));
            }
        });


    }

    private void shareMyCityAirQuality() {
        // 一键分享
        OnekeyShare share = new OnekeyShare();
        // 设置分享的信息
        share.setTitle("空气质量分享");
        // 设置信息
        share.setText("当前的空气质量指数：" + air_composition.getAQI()
                + "，今天的空气质量" + air_composition.getQuality()
                + "，除少数异常" + "敏感体质的人群外，大家可在户外正常活动。"
                + "(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        share.setImagePath(externalStorageDirectory.getPath() + "/Download/demo.png");
        // 显示分享列表
        share.show(context);
    }

    private void takePhoto() {
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        context.startActivityForResult(i, Activity.DEFAULT_KEYS_DIALER);
    }

    private void shareMyCityMoniData() {
        // 一键分享
        OnekeyShare share = new OnekeyShare();
        // 设置分享的信息
        share.setTitle("空气质量分享");
        // 设置信息
        if (air_composition.getLastMoniData() != null && air_composition.getLastMoniData().size() > 0)
            shareMyCityMoniDataNotNull(share);
        else
            shareMyCityMoniDataNull(share);
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        share.setImagePath(externalStorageDirectory.getPath() + "/Download/demo2.png");
        // 显示分享列表
        share.show(context);
    }

    private void shareMyCityMoniDataNull(OnekeyShare share) {
        share.setText("当前的空气质量指数：" + air_composition.getAQI()
                + "，今天的空气质量" + air_composition.getQuality()
                + "。" + "我周边的各大空气监测点质量情况 :暂未更新，尽请期待。 "
                + "(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");
    }

    private void shareMyCityMoniDataNotNull(OnekeyShare share) {
        share.setText("当前的空气质量指数：" + air_composition.getAQI()
                + "，今天的空气质量" + air_composition.getQuality()
                + "。" + "我周边的空气监测点质量情况 : " + air_composition.getLastMoniData().get(0).getCity() + ":" +
                air_composition.getLastMoniData().get(0).getAQI()
                + "。(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");
    }

    private void shareMyCityAirRank() {
        // 摇一摇截图分享
        Shake2Share s2s = new Shake2Share();
        // 设置回调，摇晃到一定程度就会触发分享
        s2s.setOnShakeListener(new Shake2Share.OnShakeListener() {
            public void onShake() {
                OnekeyShare oks = new OnekeyShare();
                // 设置一个用于截屏分享的View
                View windowView = context.getWindow().getDecorView();
                oks.setViewToShare(windowView);
                // 设置分享的信息
                oks.setTitle("空气质量分享");
                // 设置信息
                oks.setText("当前的空气质量指数：" + air_composition.getAQI()
                        + "，今天的空气质量" + air_composition.getQuality()
                        + "，在全国空气质量排名第" + AirRankFragment.myCityRank
                        + "(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");

                oks.setPlatform(SinaWeibo.NAME);
                oks.show(context.getBaseContext());
            }

        });
        // 启动“摇一摇分享”功能
        s2s.show(context, null);
    }

    public void initLeftMenuEvent(View v) {
        switch (v.getId()) {
            case R.id.left_menu_blowRL:
                context.startActivity(new Intent(context, BlowActivity.class));
                break;
            case R.id.left_menu_ep_RL:
                context.startActivity(new Intent(context, EPInformationActivity.class));
                break;
            case R.id.left_menu_my_attentionRL:
                context.startActivity(new Intent(context, MyAttentionReportActivity.class));
                break;
            case R.id.left_menu_my_reportRL:
                context.startActivity(new Intent(context, MySubmitComplaintsActivity.class));
                break;
            case R.id.left_menu_settingRL:
                context.startActivity(new Intent(context, OtherSetActivity.class));
                break;
            case R.id.left_menu_scanRL:
                context.startActivity(new Intent(context, ScanBeforeActivity.class));
                break;
        }
    }

    private LocationUtil locationUtil;

    public void initLocation(Handler handlerGeo, LocationManager locationManager, LocationListener locationListener) {
        if (locationUtil == null)
            locationUtil = new LocationUtil(context, handlerGeo);
        locationUtil.initLocation(locationManager, locationListener);
    }
}
