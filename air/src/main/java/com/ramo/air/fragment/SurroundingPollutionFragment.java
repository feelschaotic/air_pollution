package com.ramo.air.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramo.air.R;
import com.ramo.air.adapter.NearMoniAdapter;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.bean.NearMonitoring;
import com.ramo.air.databinding.FragmentSurroundingPollutionLayoutBinding;
import com.ramo.air.jsonparsing.AirResultParseBean;
import com.ramo.air.utils.L;
import com.ramo.air.utils.MyPreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurroundingPollutionFragment extends Fragment {


    private List<NearMonitoring> nearMoniList;
    private NearMoniAdapter adapter;
    private AirResultParseBean air;
    private Map<Integer, NearMonitoring> lastMoniData;
    private Map<Integer, AirQuality> lastTwoWeeks;
    private FragmentSurroundingPollutionLayoutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_surrounding_pollution_layout, container, false);
        init();
        initData();
        return binding.getRoot();
    }

    private void init() {
        nearMoniList = new ArrayList<NearMonitoring>();
        binding.airLastMoniRefreshLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        binding.airLastMoniRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                refreshHandler.sendEmptyMessage(1);
                            }
                        }).start();
                    }
                });// 设置下拉的监听
    }

    private RefreshHandler refreshHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    binding.airLastMoniRefreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }

    private void initData() {

        air = (AirResultParseBean) MyPreferenceUtils.readObject("air_quality");
        if (air != null) {
            lastMoniData = air.getLastMoniData();
            lastTwoWeeks = air.getLastTwoWeeks();
        }
        if (lastMoniData != null) {
            for (Integer id : lastMoniData.keySet()) {
                NearMonitoring moni = lastMoniData.get(id);
                nearMoniList.add(moni);
            }
            adapter = new NearMoniAdapter(getContext(), nearMoniList);
            binding.airLastMoniListview.setAdapter(adapter);
        } else {
            L.e("lastMoniMap==null!!");
        }

    }
}
