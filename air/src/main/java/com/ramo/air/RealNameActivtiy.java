package com.ramo.air;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.Constants;
import com.ramo.air.utils.L;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.ServerUrlUtil;
import com.ramo.air.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

public class RealNameActivtiy extends Activity {

	private Button verify_btn;
	private String url;
	private EditText verify_realname_et;
	private EditText verify_idcard_et;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				T.shortShow(RealNameActivtiy.this, "验证出错，请稍后重试");
				break;
			case 1:
				T.shortShow(RealNameActivtiy.this, "实名认证成功，请继续提交举报");
				updateRealName();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						finish();
						overridePendingTransition(R.anim.keep,
								R.anim.base_slide_right_out);
					}
				}, 1500);

			case 2:
				T.shortShow(RealNameActivtiy.this, "实名认证失败，核验不一致");
				break;
			case 3:
				T.shortShow(RealNameActivtiy.this, "实名认证失败，不存在此身份证号");

			default:
				break;
			}
		}

	};
	private Handler handlerUpdateRealName = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				L.d("服务器端更新身份证失败");
				break;
			case 1:
				L.d("服务器端更新身份证成功");
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify_real_name_layout);
		init();
		initEvent();
	}

	private void initEvent() {
		verify_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				url = Constants.VERIFY_IDCARD_API + "&realname="
						+ verify_realname_et.getText().toString() + "&idcard="
						+ verify_idcard_et.getText().toString();
				NetUtils.getIOFromUrl(url, new HttpCallbackListener() {
					Message msg = new Message();
					JSONObject obj;
					String error_code;
					String code;

					@Override
					public void onSucc(String response) {
						try {
							L.d("身份认证。。");
							obj = new JSONObject(response);
							error_code = obj.getString("error_code");
							L.e("error_code should =0 but= " + error_code);
							if ("0".equals(error_code)) {
								code = obj.getJSONObject("result").getString(
										"code");
								L.e("code:" + code);
								if ("1000".equals(code))
									msg.what = 1;
								else if ("1001".equals(code))
									msg.what = 2;
								else if ("1002".equals(code))
									msg.what = 3;
								else
									msg.what = 0;
							} else
								msg.what = 2;
						} catch (JSONException e) {
							e.printStackTrace();
						}
						// 结果值（1000：核验一致，1001：核验不一致，1002，不存在此身份证号）
						handler.sendMessage(msg);
					}

					@Override
					public void onError(Exception e) {
						msg.what = 0;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				});
			}
		});
	}

	private void init() {
		verify_btn = (Button) findViewById(R.id.verify_btn);
		verify_realname_et = (EditText) findViewById(R.id.verify_realname_et);
		verify_idcard_et = (EditText) findViewById(R.id.verify_idcard_et);
	}

	// 更新服务器端的身份证信息
	private void updateRealName() {
		NetUtils.getIOFromUrl(ServerUrlUtil.UPDATE_REALNAME
				+ "?user_real_name=" + verify_realname_et.getText().toString()
				+ "&user_id_card=" + verify_idcard_et.getText().toString()
				+ "&user_id=1", new HttpCallbackListener() {
			Message msg = new Message();

			@Override
			public void onSucc(String response) {
				if (response.equals("true"))
					msg.what = 1;
				else
					msg.what = 0;
				handlerUpdateRealName.sendMessage(msg);
			}

			@Override
			public void onError(Exception e) {
				msg.what = 0;
				handlerUpdateRealName.sendMessage(msg);
				e.printStackTrace();
			}
		});
	}
}
