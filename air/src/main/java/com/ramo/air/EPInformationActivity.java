package com.ramo.air;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class EPInformationActivity extends Activity
implements OnClickListener{
	private ImageView shuoming_1;
	private ImageView shuoming_2;
	private ImageView shuoming_3;
	private ImageView shuoming_4;
	private ImageView shuoming_5;
	private ImageView shuoming_6;
	private ImageView shuoming_7;
	private ImageView shuoming_8;
	private ImageView shuoming_9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.ep_information_layout);
		
		shuoming_1=(ImageView) findViewById(R.id.shuoming1);
		shuoming_2=(ImageView) findViewById(R.id.shuoming2);
		shuoming_3=(ImageView) findViewById(R.id.shuoming3);
		shuoming_4=(ImageView) findViewById(R.id.shuoming4);
		shuoming_5=(ImageView) findViewById(R.id.shuoming5);
		shuoming_6=(ImageView) findViewById(R.id.shuoming6);
		shuoming_7=(ImageView) findViewById(R.id.shuoming7);
		shuoming_8=(ImageView) findViewById(R.id.shuoming8);
		shuoming_9=(ImageView) findViewById(R.id.shuoming9);
		
		shuoming_1.setOnClickListener(this);
		shuoming_2.setOnClickListener(this);
		shuoming_3.setOnClickListener(this);
		shuoming_4.setOnClickListener(this);
		shuoming_5.setOnClickListener(this);
		shuoming_6.setOnClickListener(this);
		shuoming_7.setOnClickListener(this);
		shuoming_8.setOnClickListener(this);
		shuoming_9.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent=new Intent();
		String keyword = null;
		switch (arg0.getId()) {
		case R.id.shuoming1:
			keyword="产品";
			break;
		case R.id.shuoming2:
			keyword="环保";
			break;
		case R.id.shuoming3:
			keyword="什么是污染";
			break;
		case R.id.shuoming4:
			keyword="随手拍";
			break;
		case R.id.shuoming5:
			keyword="污染线索";
			break;
		case R.id.shuoming6:
			keyword="固体污染";
			break;
		case R.id.shuoming7:
			keyword="城市污染";			
			break;
		case R.id.shuoming8:
			keyword="车窗垃圾";			
			break;
		case R.id.shuoming9:
			keyword="野生动物制品";			
			break;

		default:
			break;
		}
		intent.putExtra("keyword",keyword);
		intent.setClass(EPInformationActivity.this, EPInformationDetailedActivity.class);
		startActivity(intent);
	}
	
}
