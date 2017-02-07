package com.ramo.air;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ramo.air.utils.Constants;


public class SubmitMapActivity extends Activity{
	WebView submit_map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_map_layout);
		init();
	}

	private void init() {
		submit_map=(WebView) findViewById(R.id.submit_map_webview);
		submit_map.getSettings().setJavaScriptEnabled(true);
		submit_map.setWebChromeClient(new WebChromeClient());
		submit_map.loadUrl(Constants.REPORTMAPURL);
	}
}
