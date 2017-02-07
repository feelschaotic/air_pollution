package com.ramo.air;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.ramo.air.bean.Report;
import com.ramo.air.databinding.MySubmitComplaintsDetailsBinding;
import com.ramo.air.utils.ImageManageUtil;


public class MySubmitComplaintsDetailsActivity extends Activity {

	private Report re;
	private Bundle bundle;

	private MySubmitComplaintsDetailsBinding binding;
	private Bitmap b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initDate();

	}

	private void initDate() {

		b = ImageManageUtil.StrToBitmap(re.getReport_img());
		if (b != null)
			binding.reportImg.setImageBitmap(b);
	}

	private void init() {
		binding= DataBindingUtil.setContentView(this,R.layout.my_submit_complaints_details);
		bundle = getIntent().getExtras();
		re = (Report) bundle.getSerializable("report");
		binding.setReport(re);
	}
}
