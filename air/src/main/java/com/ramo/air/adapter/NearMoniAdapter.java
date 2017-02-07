package com.ramo.air.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ramo.air.BR;
import com.ramo.air.R;
import com.ramo.air.bean.NearMonitoring;
import com.ramo.air.databinding.MainAqiMoniItemBinding;

import java.util.List;


public class NearMoniAdapter extends BaseAdapter {
    private Context context;
    private List<NearMonitoring> list;
    private LayoutInflater inflater;

    public NearMoniAdapter(Context context, List<NearMonitoring> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        MainAqiMoniItemBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.main_aqi_moni_item, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        NearMonitoring moni = list.get(pos);

        switch (moni.getColor()) {
            case 1:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_1);
                break;
            case 2:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_2);
                break;
            case 3:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_3);
                break;
            case 4:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_4);
                break;
            case 5:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_5);
                break;
            default:
                binding.aqiMoniAirnum
                        .setBackgroundResource(R.drawable.aqi_detail_bg_6);
                break;
        }
        binding.setVariable(BR.moni, list.get(pos));
        return binding.getRoot();
    }
}
