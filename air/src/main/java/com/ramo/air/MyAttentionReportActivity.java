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


public class MyAttentionReportActivity extends Activity {
	private ListView listview;
	private List<Report> list;
	private Gson gson;
	private TextView top_bar_title;
	private int isNew=0;
	private Handler handlerAttention = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listview.setAdapter(new MySubmitComplaintsAdapter(
						MyAttentionReportActivity.this, list, isNew));
				break;
			case 0:
				T.shortShow(MyAttentionReportActivity.this,
						"获取关注列表失败，请稍后重试");
				break;

			default:
				break;
			}
		}

	};
	private Handler handlerNew = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listview.setAdapter(new MySubmitComplaintsAdapter(
						MyAttentionReportActivity.this, list, isNew));
				break;
			case 0:
				T.shortShow(MyAttentionReportActivity.this,
						"获取更新的举报失败，请稍后重试");
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
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				initDataMyAttentionNew();
				top_bar_title.setText("举报新动态");
				isNew=1;
			} else {
				initDataMyAttention();
				top_bar_title.setText("关注的举报");
				isNew=0;
			}
		}
		initEvent();
	}

	private void initEvent() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			private Intent in = new Intent();
			private Bundle bundle = new Bundle();
			private Report report;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				report = list.get(position);
				bundle.putSerializable("report", report);
				in.putExtras(bundle);
				
				in.setClass(MyAttentionReportActivity.this,
						MySubmitComplaintsDetailsActivity.class);
				startActivity(in);
				//如果是1 表示是更新的举报 要把关注的isNew的状态还原
				if(isNew==1){
					NetUtils.getIOFromUrl(ServerUrlUtil.UPDATE_ATTENTION_STATE
							+ "?report_id=" + report.getReport_id(), new HttpCallbackListener() {

						@Override
						public void onSucc(String response) {
						}

						@Override
						public void onError(Exception e) {
							e.printStackTrace();
						}
					});
				}
			}

		});
	}

	private void initDataMyAttention() {

		NetUtils.getIOFromUrl(ServerUrlUtil.MY_ATTENTION_REPORT,
				new HttpCallbackListener() {
					Message m = new Message();

					@Override
					public void onSucc(String response) {
						Type listType = new TypeToken<ArrayList<Report>>() {
						}.getType();
						// L.e("myAttention:"+response);
						list = gson.fromJson(response, listType);
						for (Report re : list) {
							L.e("report " + re);
							L.e("report " + re.getReport_content());

						}
						m.what = 1;
						handlerAttention.sendMessage(m);
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
						m.what = 0;
						handlerAttention.sendMessage(m);
					}
				});

	}

	private void initDataMyAttentionNew() {

		NetUtils.getIOFromUrl(ServerUrlUtil.MY_ATTENTION_NEW,
				new HttpCallbackListener() {
					Message m = new Message();

					@Override
					public void onSucc(String response) {
						Type listType = new TypeToken<ArrayList<Report>>() {
						}.getType();
						L.e("my new:" + response);
						list = gson.fromJson(response, listType);
						for (Report re : list) {
							L.e("report " + re);
							L.e("report " + re.getReport_content());

						}
						m.what = 1;
						handlerNew.sendMessage(m);
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
						m.what = 0;
						handlerNew.sendMessage(m);
					}
				});

	}

	private void init() {
		list = new ArrayList<Report>();
		listview = (ListView) findViewById(R.id.my_submit_listview);
		gson = new Gson();
		top_bar_title = (TextView) findViewById(R.id.top_bar_title);
	}
}
