package com.ramo.air.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ramo.air.LineActivity;
import com.ramo.air.R;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.databinding.FragmentMainLayoutBinding;
import com.ramo.air.jsonparsing.AirParseBean;
import com.ramo.air.jsonparsing.AirResultParseBean;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.presenter.MainFragmentPresenter;
import com.ramo.air.utils.Constants;
import com.ramo.air.utils.DateUtil;
import com.ramo.air.utils.L;
import com.ramo.air.utils.MyPreferenceUtils;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.PinYinUtil;
import com.ramo.air.utils.PreferenceKeyName;
import com.ramo.air.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainFragment extends Fragment {
    private Gson gson;
    AirResultParseBean air;

    FragmentMainLayoutBinding binding;

    Map<Integer, AirQuality> twoAirMap;
    private static String dataStrings[];
    private double[] aqiDoubles;
    private MainFragmentPresenter mainFragmentPresenter;
    private AirQuality air_composition;
    private LineViewListener lineViewListener;

    private Handler cityChangeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    queryFromPreference();
                    drawLine();
                    break;

                default:
                    T.shortShow(getContext(), getResources().getString(R.string.load_error));
                    break;
            }
        }
    };

    public void setLineViewListener(LineViewListener listener) {
        this.lineViewListener = listener;
    }


    public interface LineViewListener {
        void onLineViewTouch();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main_layout, container, false);
        mainFragmentPresenter = new MainFragmentPresenter(getContext());

        init();

        return binding.getRoot();
    }

    public void init() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        binding.airMap.getSettings().setJavaScriptEnabled(true);
        binding.airMap.setWebChromeClient(new WebChromeClient());
        binding.airMap.loadUrl(Constants.MAPURL);

        // 移除原有的LinearLayout中的视图控件
        binding.line.removeAllViewsInLayout();

        binding.lineRl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(getContext(), LineActivity.class));
            }
        });
    }


    public void initData() {
        queryFromPreference();
        if (air == null) {
            Toast.makeText(getContext(), "更新中。。", Toast.LENGTH_SHORT).show();
            queryCityFirst();

        } else {
            binding.setAirQuality(air.getCitynow());
            drawLine();
        }

    }

    private void drawLine() {
        // 趋势图的绘制开始

        if (air != null)
            twoAirMap = air.getLastTwoWeeks();
        if (twoAirMap == null) {
            return;
        }
        int j = 0;
        aqiDoubles = new double[twoAirMap.size()];
        dataStrings = new String[twoAirMap.size()];

        for (Integer id : twoAirMap.keySet()) {
            AirQuality air = twoAirMap.get(id);
            aqiDoubles[j] = Double.parseDouble(air.getAQI());
            dataStrings[j++] = air.getDate().substring(5);
        }
        String[] titles = new String[]{"最近28天的空气质量"};
        View child = mainFragmentPresenter.drawLine(titles, dataStrings, aqiDoubles);
        child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lineViewListener != null)
                    lineViewListener.onLineViewTouch();
                return false;
            }
        });

        binding.line.addView(child);
    }

    private void queryFromPreference() {
        air = (AirResultParseBean) MyPreferenceUtils.readObject("air_quality");

        if (air != null) {
            binding.setAirResultParseBean(air);
            if (air.getCitynow() != null) {
                int color = air.getCitynow().getColor();

                switch (color) {
                    case 1:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_1);
                        break;
                    case 2:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_2);
                        break;
                    case 3:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_3);
                        break;
                    case 4:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_4);
                        break;
                    case 5:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_5);
                        break;
                    default:
                        binding.currentCityAirQuality
                                .setBackgroundResource(R.drawable.aqi_color_bg_6);
                        break;
                }

            }
        }

    }

    private void queryCityFirst() {
        //取出定位的城市
        String cityName = getLocationCityFromPreference();
        if (cityName == null) {
            T.shortShow(getContext(), "city is null");
            return;
        } else {
            queryFromServer(cityName);
        }
        // L.e("查询到的 cityName:  "+cityName);

    }

    public void queryFromServer(String cityName) {
        // 先查询城市的未来几天空气趋势
        Map map = new HashMap<String, String>();

        String city = PinYinUtil.getFullSpell(cityName.substring(0, cityName.length() - 1));
        map.put("city", city);
        map.put("key", "61a8b0c0ac3ea2875f8d64785a4cd70e");
        L.e("city:" + city);
        NetUtils.getHttpUrlConnectionJuHe(Constants.JUHE_AQIAPI, map,
                new HttpCallbackListener() {

                    @Override
                    public void onSucc(String response) {
                        AirParseBean airParse = gson.fromJson(response,
                                AirParseBean.class);
                        if (airParse.getResultcode().equals("200")) {
                            AirResultParseBean airResultParseBean = airParse.getResult().get(0);
                            airResultParseBean.setCitynow(air_composition);
                            MyPreferenceUtils.saveObject("air_quality", airResultParseBean);
                            L.e("空气质量 resultcode==200");
                            cityChangeHandler.sendEmptyMessage(1);
                        } else {
                            cityChangeHandler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        cityChangeHandler.sendEmptyMessage(0);
                    }

                });

        // 再查询具体的空气含量
        NetUtils.getHttpUrlConnectionJuHe(Constants.JUHE_AQIPM_API, map,
                new HttpCallbackListener() {
                    @Override
                    public void onSucc(String response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("resultcode")
                                    .equals("200")) {

                                JSONArray result = jsonObject
                                        .getJSONArray("result");
                                JSONObject obj = result.getJSONObject(0);
                                air_composition = new AirQuality();
                                air_composition.setCity(obj.getString("city"));
                                air_composition.setPm25(obj.getString("PM2.5"));
                                air_composition.setAQI(obj.getString("AQI"));
                                air_composition.setQuality(obj
                                        .getString("quality"));
                                air_composition.setPM10(obj.getString("PM10"));
                                air_composition.setCO(obj.getString("CO"));
                                air_composition.setNO2(obj.getString("NO2"));
                                air_composition.setSO2(obj.getString("SO2"));
                                air_composition.setO3(obj.getString("O3"));
                                air_composition.setDate(DateUtil.getTimeFormatText(new Date(obj.getString("time"))));
                                binding.setAirQuality(air_composition);

                            } else {
                                Log.e("juhe 空气含量", "获取失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private String getLocationCityFromPreference() {
        return (String) MyPreferenceUtils.readObject(PreferenceKeyName.CITY_NAME);
    }

}
