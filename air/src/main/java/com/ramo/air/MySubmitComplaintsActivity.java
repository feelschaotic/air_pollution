package com.ramo.air;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramo.air.adapter.MySubmitComplaintsAdapter;
import com.ramo.air.bean.Report;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.L;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.ServerUrlUtil;
import com.ramo.air.utils.T;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MySubmitComplaintsActivity extends Activity {
	private ListView listview;
	private List<Report> list;
	private Gson gson;
	private TextView top_bar_title;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listview.setAdapter(new MySubmitComplaintsAdapter(MySubmitComplaintsActivity.this, list,0));
				break;
			case 0:
				T.shortShow(MySubmitComplaintsActivity.this, "获取举报列表失败，请稍后重试");
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_submit_complaints_layout);
		init();
		initData();
		initEvent();
	}

	private void initEvent() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			private Intent in=new Intent();
			private Bundle bundle=new Bundle();
			private Report report;
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				report = list.get(position);
				bundle.putSerializable("report",report);
	    		in.putExtras(bundle);
	    		in.setClass(MySubmitComplaintsActivity.this,
						MySubmitComplaintsDetailsActivity.class);
				startActivity(in);
			}

		});
	}

	private void initData() {
	
		NetUtils.getIOFromUrl(ServerUrlUtil.MY_REPORT, new HttpCallbackListener() {
			Message m = new Message();

			@Override
			public void onSucc(String response) {
				Type listType = new TypeToken<ArrayList<Report>>() {
				}.getType();
				L.e("myReport+:" + response);
				list = gson.fromJson(response, listType);
				m.what = 1;
				handler.sendMessage(m);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				m.what = 0;
				handler.sendMessage(m);
			}
		});
		
	}

	private void init() {
		list = new ArrayList<Report>();
		listview = (ListView) findViewById(R.id.my_submit_listview);
		gson=new Gson();
		top_bar_title=(TextView) findViewById(R.id.top_bar_title);
		top_bar_title.setText("我的举报");
	}
}
