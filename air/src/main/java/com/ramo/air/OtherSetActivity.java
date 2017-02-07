package com.ramo.air;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.ramo.air.adapter.OtherSetAdapter;


public class OtherSetActivity extends Activity {
	
	private ListView other_set_listview;
	private String systemText[] = {  
			"推送设置","清除缓存","语音播报空气（每天8点）","音效","戴口罩提醒","晃动分享","意见反馈","给应用评分" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_set_layout);
		initView();
	}


	private void initView() {
		
		other_set_listview = (ListView) findViewById(R.id.other_set_listview);
		other_set_listview.setAdapter(new OtherSetAdapter(OtherSetActivity.this, systemText));

	}

	
}
