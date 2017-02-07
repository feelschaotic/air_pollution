package com.ramo.air;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ramo.air.utils.Constants;


public class AirPollutionMapActivity extends Activity {
	WebView air_map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_pollution_map_layout);
		init();
	}

	private void init() {
		air_map = (WebView) findViewById(R.id.air_pollution_map_webview);
		air_map.getSettings().setJavaScriptEnabled(true);
		air_map.setWebChromeClient(new WebChromeClient());
		air_map.loadUrl(Constants.AIR_POLLUTION_MAPAPI);
	}
}
