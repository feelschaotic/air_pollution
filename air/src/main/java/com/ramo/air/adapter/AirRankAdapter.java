package com.ramo.air.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ramo.air.BR;
import com.ramo.air.R;
import com.ramo.air.bean.AirRank;
import com.ramo.air.databinding.MainAqiRankItemBinding;

import java.util.List;


public class AirRankAdapter extends BaseAdapter {
	private Context context;
	private List<AirRank> list;
	private LayoutInflater inflater;


	public AirRankAdapter(Context context, List<AirRank> airRanks) {
		this.context = context;
		this.list = airRanks;
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
		MainAqiRankItemBinding binding;
		if (convertView == null) {
			binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.main_aqi_rank_item, parent, false);
		} else {
			binding = DataBindingUtil.getBinding(convertView);
		}
		AirRank rank = list.get(pos);
		binding.aqiRankNum.setText(pos+1+"");

		switch (rank.getColor()) {
		case 1:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_1);
			break;
		case 2:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_2);
			break;
		case 3:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_3);
			break;
		case 4:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_4);
			break;
		case 5:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_5);
			break;
		default:
			binding.aqiRankAirnum.setBackgroundResource(R.drawable.aqi_detail_bg_6);
			break;
		}

		binding.setVariable(BR.airRank, list.get(pos));
		return binding.getRoot();
	}
}

