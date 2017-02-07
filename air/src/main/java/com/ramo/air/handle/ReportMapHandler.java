package com.ramo.air.handle;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.ramo.air.bean.ReportDataBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import amap.yuntu.core.ProtocalHandler;
import amap.yuntu.core.PushDataListener;

public class ReportMapHandler extends ProtocalHandler {
	private PushDataListener mListener;
	private boolean isPush = true;
	private CopyOnWriteArrayList<ReportDataBean> mDataList = new CopyOnWriteArrayList<ReportDataBean>();

	public ReportMapHandler(PushDataListener listener) {
		this.mListener = listener;
		upload.start();
	}

	/**
	 * 加入上传列表
	 * 
	 * @param bean
	 */
	public void addTask(ReportDataBean bean) {
		mDataList.add(bean);
	}

	/**
	 * 销毁上传线程
	 */
	public void destroy() {
		isPush = false;
	}

	/**
	 * 用户建立的数据表id http://yuntu.amap.com/datamanager/
	 */
	@Override
	protected String getTableID() {
		return "5691c4c87bbf196a67174b6d";
	}

	/**
	 * 用户申请且绑定的key，需要在网站开启云存储功能
	 */
	@Override
	protected String getKEY() {
		return "0f4579b0ed0e66ff7ca3049e824dc4a9";
	}

	/**
	 * 获取上传单条数据
	 */
	@Override
	protected String getUserJSONString() {
		if (mDataList.size() > 0) {
			Gson gson = new Gson();
			ReportDataBean bean = mDataList.get(0);
			mDataList.remove(0);
			return gson.toJson(bean);
		}
		return new String();
	}

	/**
	 * 接收上传数据结果
	 */
	Handler upHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String result = (String) msg.obj;
				boolean succeed = false;
				String info = "";
				try {
					JSONObject jobj = new JSONObject(result);
					int status = jobj.getInt("status");
					info = jobj.getString("info");
					if (status == 0) {
						succeed = false;
					} else {
						succeed = true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					if (mListener != null) {
						mListener.onPushFinish(succeed, info);
					}
				}

			}
		}

	};
	/**
	 * 上传数据线程
	 */
	Thread upload = new Thread() {
		@Override
		public void run() {
			while (isPush) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mDataList.size() <= 0) {
					continue;
				}
				String result = getData();
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				upHandler.sendMessage(msg);

			}

		}
	};
}
