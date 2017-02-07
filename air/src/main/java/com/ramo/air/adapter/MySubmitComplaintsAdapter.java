package com.ramo.air.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.air.R;
import com.ramo.air.bean.Report;
import com.ramo.air.utils.ImageManageUtil;

import java.util.List;


public class MySubmitComplaintsAdapter extends BaseAdapter {
	private Context context;
	private List<Report> list;
	private LayoutInflater inflater;
	private MySubmitHolder holder;
	private Bitmap b;
	private int isNew=0;

	public MySubmitComplaintsAdapter(Context context, List<Report> airRanks,int isNew) {
		this.context = context;
		this.list = airRanks;
		inflater = LayoutInflater.from(context);
		this.isNew=isNew;
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
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.my_submit_complaints_item, parent,
					false);

			holder = new MySubmitHolder();

			holder.title = (TextView) view.findViewById(R.id.report_title);
			holder.location = (TextView) view
					.findViewById(R.id.report_location);
			holder.time = (TextView) view.findViewById(R.id.report_time);
			holder.img = (ImageView) view.findViewById(R.id.report_img);
			holder.report_list_right_img = (ImageView) view.findViewById(R.id.report_list_right_img);

			// 把容器和View 关系保存起来
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (MySubmitHolder) view.getTag();
		}
		Report report = list.get(pos);

		holder.time.setText(report.getReport_time());
		holder.title.setText(report.getReport_content());
		holder.location.setText(report.getReport_location());
		if(isNew==1)
			holder.report_list_right_img.setImageBitmap(ImageManageUtil.RToBitmap(R.drawable.report_new));
		else
			holder.report_list_right_img.setImageBitmap(ImageManageUtil.RToBitmap(R.drawable.pop_btn_normal));
		b = ImageManageUtil.StrToBitmap(report.getReport_img());
		if (b == null)
			holder.img.setImageBitmap(ImageManageUtil
					.RToBitmap(R.drawable.error_img404));
		else
			holder.img.setImageBitmap(b);

		return view;
	}

}

class MySubmitHolder {
	TextView title;
	TextView time;
	TextView location;
	ImageView img;
	ImageView report_list_right_img;
}
