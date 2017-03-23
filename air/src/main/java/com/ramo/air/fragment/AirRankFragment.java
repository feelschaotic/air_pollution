package com.ramo.air.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramo.air.R;
import com.ramo.air.adapter.AirRankAdapter;
import com.ramo.air.bean.AirRank;
import com.ramo.air.collection.SortByAirNum;
import com.ramo.air.collection.SortByAirNumDesc;
import com.ramo.air.databinding.FragmentAirRankLayoutBinding;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.Constants;
import com.ramo.air.utils.DateUtil;
import com.ramo.air.utils.L;
import com.ramo.air.utils.MyPreferenceUtils;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.PreferenceKeyName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AirRankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private List<AirRank> airRanks;
    private AirRank airRank;
    private Gson gson;
    private AirRankAdapter airRankAdapter;
    private boolean sortDesc = true;
    private int num = 0;
    public static int myCityRank = 0;

    private Handler handlerResult = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    airRankAdapter = new AirRankAdapter(getContext(), airRanks);
                    binding.airRankListview.setAdapter(airRankAdapter);
                    myCityRank = airRanks.indexOf(airRank);
                    binding.airRankListview.setSelection(myCityRank);
                    updateTime();
                    break;

                default:
                    break;
            }
        }

    };

    FragmentAirRankLayoutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_air_rank_layout, container, false);
        init();
        initData();
        initEvent();
        return binding.getRoot();
    }

    //更新发布时间
    protected void updateTime() {
        binding.airRankReportTime.setText(DateUtil.getTimeFormatText(DateUtil.StrToDate(airRanks.get(0).getTime_point(), "yyyy-MM-dd'T'HH:mm:ss'Z'")) + "发布");
    }

    private void initEvent() {
        binding.airRankSortBtn.setOnClickListener(new View.OnClickListener() {

            private Drawable drawable;

            @Override
            public void onClick(View arg0) {
                if (sortDesc) {
                    drawable = getResources().getDrawable(
                            R.drawable.aqi_rank_foot_rank_btn_selected_normal);
                } else {
                    drawable = getResources().getDrawable(
                            R.drawable.aqi_rank_foot_rank_btn_normal);


                }
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                binding.airRankSortBtn.setCompoundDrawables(drawable, null, null,
                        null);
                Collections.sort(airRanks, new SortByAirNumDesc());
                sortDesc = !sortDesc;
                airRankAdapter.notifyDataSetChanged();

            }
        });
    }

    private void initData() {
        gson = new Gson();
        NetUtils.getHttpUrlConnection(Constants.CITY_AQIRANK_API,
                new HttpCallbackListener() {

                    @Override
                    public void onSucc(String response) {
                        String json = response.substring(
                                response.indexOf("(") + 1,
                                response.length() - 1);
                        Type listType = new TypeToken<ArrayList<AirRank>>() {
                        }.getType();
                        airRanks = gson.fromJson(json, listType);

                        Message m = new Message();
                        m.what = 1;
                        handlerResult.sendMessage(m);

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("juhe 排名", e + "");
                    }
                });

        Collections.sort(airRanks, new SortByAirNum());
    }

    private void setListViewPos(int pos) {

        if (android.os.Build.VERSION.SDK_INT >= 8) {
            binding.airRankListview.smoothScrollToPosition(pos);
        } else {
            binding.airRankListview.setSelection(pos);
        }
    }

    private void init() {
        binding.airRankListview.setSelected(true);
        airRank = new AirRank();
        airRank.setArea(queryCityFirst());
/*        airRank.setArea(((AirResultParseBean) MyPreferenceUtils
                .readObject("air_quality")).getCitynow().getCity());*/
        airRanks = new ArrayList<AirRank>();
        binding.airRankRefreshLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        binding.airRankRefreshLayout.setOnRefreshListener(this);// 设置下拉的监听

    }

    private String queryCityFirst() {
        //取出定位的城市
        String cityName = (String) MyPreferenceUtils.readObject(PreferenceKeyName.CITY_NAME);
        L.e(cityName);
        if (cityName == null) {
            return "汕头";
        }
        return cityName.substring(0,cityName.length()-1);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {// 下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
            @Override
            public void run() {
                try {
                    initData();
                    refreshHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    refreshHandler.sendEmptyMessage(0);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private RefreshHandler refreshHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getContext(), "刷新出错", Toast.LENGTH_SHORT).show();
                case 1:
                    binding.airRankRefreshLayout.setRefreshing(false);
                    airRankAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
