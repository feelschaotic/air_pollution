package com.ramo.air;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ScanBeforeActivity extends Activity {
	public static final int CameraRequestCode = 1001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(ScanBeforeActivity.this, ScanActivity.class);
		startActivityForResult(intent, CameraRequestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CameraRequestCode && resultCode == RESULT_OK) {
			String path = data.getStringExtra(ScanActivity.kPhotoPath);
			ScanActivityAddTags.open(this, path);
			return;
		}
	}
}
