package com.ramo.air;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ramo.air.bean.News;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.L;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.ServerUrlUtil;
import com.ramo.air.utils.T;

public class EPInformationDetailedActivity extends Activity {

	private TextView news_title;
	private TextView news_content;
	private String keyword;
	private Gson gson;

	private Handler news_details_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				News n = (News) msg.obj;
				news_title.setText(n.getNews_title());
				news_content.setText(n.getNews_content());

				break;
			case 0:
				T.shortShow(EPInformationDetailedActivity.this,
						"查询失败，请稍后重试");
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
		initData();
	}

	private void initData() {
		NetUtils.sendRequestToUrl(ServerUrlUtil.NEWS_DETAILS, new String[]{"keyword"}
				, new String[]{keyword}, new HttpCallbackListener() {
			Message msg = new Message();

			@Override
			public void onSucc(String response) {
				//L.e("response:"+response);
				if (response == null || "null".equals(response) || "".equals(response)) {
					msg.what = 2;
				} else {
					News news = gson.fromJson(response, News.class);
					msg.what = 1;
					msg.obj = news;
				}
				news_details_handler.sendMessage(msg);
			}

			@Override
			public void onError(Exception e) {
				L.e("error");
				msg.what = 0;
				news_details_handler.sendMessage(msg);
			}
		});
	}

	private void init() {
		setContentView(R.layout.ep_information_detailed_layout);

		Intent in = getIntent();
		keyword = in.getStringExtra("keyword");

		news_title = (TextView) findViewById(R.id.ep_news_title);
		news_content = (TextView) findViewById(R.id.ep_news_content);

		gson = new Gson();
	}

}
